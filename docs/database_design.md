# Thiết kế Database — KPI Management Platform (v2)

## 1. Tổng quan kiến trúc

Hệ thống bao gồm **5 nhóm chức năng chính**:

| Nhóm | Chức năng | Bảng liên quan |
|------|-----------|----------------|
| **IAM** | Tài khoản, phòng ban, chức vụ, vai trò | `accounts`, `employees`, `roles`, `account_roles`, `departments`, `positions` |
| **Cycle** | Chu kỳ đánh giá KPI | `kpi_cycles` |
| **Template** | Bộ tiêu chí KPI mẫu | `kpi_categories`, `kpi_templates` |
| **Assignment** | KPI cascade 3 cấp + đề xuất | `kpi_documents`, `kpi_items` |
| **Tracking & Eval** | Tiến độ, minh chứng, đánh giá cuối kỳ | `kpi_tracking_logs`, `kpi_attachments`, `kpi_item_evaluations`, `kpi_document_evaluations` |

---

## 2. Luồng KPI Cascade 3 cấp

```
Ban Giám đốc (role: DIRECTOR)
  └─ tạo KPI Công ty (target_type='COMPANY')
       │  VD: "Doanh thu toàn công ty Q3: 10 tỷ"
       │
       ▼
Giám đốc/BGĐ giao cho Phòng (target_type='DEPARTMENT', parent_doc_id=KPI công ty)
  └─ KPI Phòng KD: "Doanh thu phòng: 6 tỷ"
  └─ KPI Phòng IT: "Uptime: 99.9%"
       │
       ▼
Trưởng phòng (role: MANAGER) giao cho Nhân viên (target_type='EMPLOYEE', parent_doc_id=KPI phòng)
  └─ KPI Nhân viên A: "Doanh thu cá nhân: 1 tỷ"
  └─ KPI Nhân viên B: "Số bug fix: 20/sprint"
```

**Điểm mấu chốt:** Cặp `(target_type, target_id)` trong `kpi_documents` xác định KPI giao cho ai.

---

## 3. Schema đầy đủ

### 3.1 Nhóm IAM

```sql
-- Phòng ban (hỗ trợ phân cấp cha - con)
CREATE TABLE departments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(50)  NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL,
    parent_id       BIGINT       NULL,          -- phòng ban cha (NULL = cấp cao nhất)
    manager_id      BIGINT       NULL,          -- FK → employees.id (set sau khi tạo employee)
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    CONSTRAINT fk_dept_parent FOREIGN KEY (parent_id) REFERENCES departments(id)
);

-- Chức vụ
CREATE TABLE positions (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    position_code   VARCHAR(50)  NOT NULL UNIQUE,
    title           VARCHAR(100) NOT NULL,
    level           INT          NOT NULL,
    -- 1=Nhân viên, 2=Trưởng nhóm, 3=Trưởng phòng, 4=Phó GĐ, 5=Giám đốc
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL
);

-- Nhân viên
CREATE TABLE employees (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_code   VARCHAR(50)  NOT NULL UNIQUE,
    full_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    phone_number    VARCHAR(15)  NULL,
    department_id   BIGINT       NULL,
    position_id     BIGINT       NULL,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    CONSTRAINT fk_emp_dept     FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_emp_position FOREIGN KEY (position_id)  REFERENCES positions(id)
);

-- Tài khoản đăng nhập (1-1 với employee)
CREATE TABLE accounts (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE', -- ACTIVE | INACTIVE | LOCKED
    provider        VARCHAR(30)  NULL,                      -- LOCAL | GOOGLE | ...
    employee_id     BIGINT       NOT NULL UNIQUE,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    CONSTRAINT fk_acc_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Vai trò trong hệ thống
CREATE TABLE roles (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    -- ADMIN | DIRECTOR | MANAGER | EMPLOYEE
    name        VARCHAR(100) NOT NULL,
    description TEXT         NULL
);

-- Tài khoản ↔ vai trò (N-N, 1 người có thể có nhiều role)
CREATE TABLE account_roles (
    account_id  BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    PRIMARY KEY (account_id, role_id),
    CONSTRAINT fk_ar_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_ar_role    FOREIGN KEY (role_id)    REFERENCES roles(id)
);
```

**Giá trị `roles.code` và ý nghĩa:**

| Code | Được làm gì |
|------|------------|
| `ADMIN` | Quản trị hệ thống, tài khoản, phòng ban, chu kỳ |
| `DIRECTOR` | Tạo KPI cấp công ty, giao KPI cho phòng ban |
| `MANAGER` | Nhận KPI phòng, giao KPI cho nhân viên dưới quyền, duyệt KPI đề xuất |
| `EMPLOYEE` | Xem KPI được giao, cập nhật tiến độ, đề xuất KPI cá nhân |

> **Lưu ý:** Trưởng phòng thường có cả `MANAGER` + `EMPLOYEE` vì họ vừa quản lý nhân viên vừa có KPI cá nhân.

---

### 3.2 Chu kỳ đánh giá

```sql
CREATE TABLE kpi_cycles (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cycle_code      VARCHAR(50)  NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL,
    type            VARCHAR(20)  NOT NULL, -- MONTHLY | QUARTERLY | SEMI_ANNUAL | ANNUAL
    start_date      DATE         NOT NULL,
    end_date        DATE         NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'PLANNING',
    -- PLANNING → ACTIVE → EVALUATING → CLOSED
    created_by      BIGINT       NOT NULL,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    CONSTRAINT fk_cycle_creator FOREIGN KEY (created_by) REFERENCES employees(id)
);
```

---

### 3.3 Tiêu chí KPI mẫu (Template)

```sql
-- Danh mục tiêu chí (VD: Hiệu suất, Thái độ, Sáng tạo...)
CREATE TABLE kpi_categories (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_code   VARCHAR(50)  NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL,
    description     TEXT         NULL,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL
);

-- Tiêu chí KPI mẫu
CREATE TABLE kpi_templates (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_code   VARCHAR(50)  NOT NULL UNIQUE,
    category_id     BIGINT       NULL,
    name            VARCHAR(150) NOT NULL,
    description     TEXT         NULL,
    unit            VARCHAR(30)  NOT NULL,    -- %, lần, sản phẩm, điểm...
    target_type     VARCHAR(20)  NOT NULL,    -- HIGHER_BETTER | LOWER_BETTER | EXACT
    default_weight  DECIMAL(5,2) NULL,        -- trọng số gợi ý mặc định
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    CONSTRAINT fk_tmpl_category FOREIGN KEY (category_id) REFERENCES kpi_categories(id)
);
```

---

### 3.4 KPI Documents — Trái tim của hệ thống

```sql
CREATE TABLE kpi_documents (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_code   VARCHAR(100) NOT NULL UNIQUE,
    cycle_id        BIGINT       NOT NULL,

    -- ===== POLYMORPHIC: xác định KPI giao cho ai =====
    target_type     VARCHAR(20)  NOT NULL,
    -- 'COMPANY'    → KPI toàn công ty (do DIRECTOR tạo), target_id = NULL
    -- 'DEPARTMENT' → KPI phòng ban,   target_id = departments.id
    -- 'EMPLOYEE'   → KPI cá nhân,     target_id = employees.id
    target_id       BIGINT       NULL,
    -- =================================================

    parent_doc_id   BIGINT       NULL,        -- KPI cha trong cascade (NULL = gốc)
    source_type     VARCHAR(20)  NOT NULL DEFAULT 'ASSIGNED',
    -- 'ASSIGNED'  → do cấp trên giao xuống
    -- 'PROPOSED'  → nhân viên tự đề xuất
    created_by      BIGINT       NOT NULL,    -- người tạo phiếu
    approver_id     BIGINT       NULL,        -- người duyệt (NULL khi còn DRAFT)
    status          VARCHAR(30)  NOT NULL DEFAULT 'DRAFT',
    -- DRAFT → PENDING_APPROVAL → APPROVED → IN_PROGRESS
    -- → SELF_EVALUATED → MANAGER_EVALUATED → CLOSED
    -- (REJECTED quay về DRAFT)
    submitted_at    DATETIME(6)  NULL,
    approved_at     DATETIME(6)  NULL,
    closed_at       DATETIME(6)  NULL,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,

    CONSTRAINT fk_doc_cycle      FOREIGN KEY (cycle_id)      REFERENCES kpi_cycles(id),
    CONSTRAINT fk_doc_parent     FOREIGN KEY (parent_doc_id) REFERENCES kpi_documents(id),
    CONSTRAINT fk_doc_creator    FOREIGN KEY (created_by)    REFERENCES employees(id),
    CONSTRAINT fk_doc_approver   FOREIGN KEY (approver_id)   REFERENCES employees(id),

    -- Mỗi target chỉ có 1 phiếu KPI trong 1 chu kỳ
    UNIQUE KEY uq_doc_target_cycle (cycle_id, target_type, target_id)
);
```

**Ví dụ dữ liệu thực tế:**

| id | target_type | target_id | parent_doc_id | source_type | Ý nghĩa |
|----|-------------|-----------|---------------|-------------|---------|
| 1 | COMPANY | NULL | NULL | ASSIGNED | KPI công ty Q3 do BGĐ tạo |
| 2 | DEPARTMENT | 3 | 1 | ASSIGNED | KPI Phòng KD, cascade từ #1 |
| 3 | DEPARTMENT | 5 | 1 | ASSIGNED | KPI Phòng IT, cascade từ #1 |
| 4 | EMPLOYEE | 12 | 2 | ASSIGNED | KPI nhân viên A thuộc Phòng KD |
| 5 | EMPLOYEE | 15 | NULL | PROPOSED | Nhân viên B tự đề xuất KPI |

---

### 3.5 KPI Items — Từng tiêu chí trong phiếu

```sql
CREATE TABLE kpi_items (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id     BIGINT        NOT NULL,           -- thuộc phiếu KPI nào
    template_id     BIGINT        NULL,               -- NULL nếu tạo thủ công
    parent_item_id  BIGINT        NULL,               -- tiêu chí cha (phân cấp trong 1 phiếu)
    name            VARCHAR(200)  NOT NULL,
    description     TEXT          NULL,
    unit            VARCHAR(30)   NOT NULL,
    target_type     VARCHAR(20)   NOT NULL,           -- HIGHER_BETTER | LOWER_BETTER | EXACT
    weight          DECIMAL(5,2)  NOT NULL,           -- trọng số (tổng các item trong doc = 100)
    target_value    DECIMAL(15,2) NOT NULL,
    current_value   DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE', -- ACTIVE | CANCELLED
    is_deleted      BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)   NOT NULL,
    updated_at      DATETIME(6)   NOT NULL,

    CONSTRAINT fk_item_document FOREIGN KEY (document_id)    REFERENCES kpi_documents(id),
    CONSTRAINT fk_item_template FOREIGN KEY (template_id)    REFERENCES kpi_templates(id),
    CONSTRAINT fk_item_parent   FOREIGN KEY (parent_item_id) REFERENCES kpi_items(id)
);
```

---

### 3.6 Tracking & Evaluation

```sql
-- Lịch sử cập nhật tiến độ
CREATE TABLE kpi_tracking_logs (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    kpi_item_id     BIGINT        NOT NULL,
    reporter_id     BIGINT        NOT NULL,           -- nhân viên báo cáo tiến độ
    value_before    DECIMAL(15,2) NOT NULL,           -- giá trị trước khi cập nhật
    value_delta     DECIMAL(15,2) NOT NULL,           -- lượng thay đổi lần này
    value_after     DECIMAL(15,2) NOT NULL,           -- giá trị sau khi cập nhật
    notes           TEXT          NULL,
    is_deleted      BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)   NOT NULL,
    updated_at      DATETIME(6)   NOT NULL,

    CONSTRAINT fk_log_item     FOREIGN KEY (kpi_item_id) REFERENCES kpi_items(id),
    CONSTRAINT fk_log_reporter FOREIGN KEY (reporter_id) REFERENCES employees(id)
);

-- Minh chứng đính kèm (nhiều file cho 1 lần cập nhật)
CREATE TABLE kpi_attachments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_log_id BIGINT        NOT NULL,
    file_name       VARCHAR(255)  NOT NULL,
    file_url        VARCHAR(500)  NOT NULL,           -- URL trỏ tới storage (S3/MinIO/local)
    file_type       VARCHAR(50)   NULL,               -- image/png, application/pdf...
    file_size_bytes BIGINT        NULL,
    uploaded_by     BIGINT        NOT NULL,
    is_deleted      BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)   NOT NULL,
    updated_at      DATETIME(6)   NOT NULL,

    CONSTRAINT fk_att_log      FOREIGN KEY (tracking_log_id) REFERENCES kpi_tracking_logs(id),
    CONSTRAINT fk_att_uploader FOREIGN KEY (uploaded_by)     REFERENCES employees(id)
);

-- Đánh giá từng tiêu chí KPI cuối kỳ
CREATE TABLE kpi_item_evaluations (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    kpi_item_id     BIGINT        NOT NULL UNIQUE,
    self_score      DECIMAL(5,2)  NULL,
    self_comment    TEXT          NULL,
    self_eval_at    DATETIME(6)   NULL,
    manager_score   DECIMAL(5,2)  NULL,
    manager_comment TEXT          NULL,
    manager_eval_at DATETIME(6)   NULL,
    final_score     DECIMAL(5,2)  NULL,              -- điểm chốt cuối cùng
    is_deleted      BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)   NOT NULL,
    updated_at      DATETIME(6)   NOT NULL,

    CONSTRAINT fk_iev_item FOREIGN KEY (kpi_item_id) REFERENCES kpi_items(id)
);

-- Tổng kết đánh giá cả phiếu (tổng hợp có trọng số từ kpi_item_evaluations)
CREATE TABLE kpi_document_evaluations (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id         BIGINT        NOT NULL UNIQUE,
    total_self_score    DECIMAL(5,2)  NULL,
    total_manager_score DECIMAL(5,2)  NULL,
    total_final_score   DECIMAL(5,2)  NULL,
    rating              VARCHAR(20)   NULL,
    -- EXCELLENT | GOOD | SATISFACTORY | NEEDS_IMPROVEMENT
    evaluator_id        BIGINT        NULL,
    evaluated_at        DATETIME(6)   NULL,
    note                TEXT          NULL,
    is_deleted          BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at          DATETIME(6)   NOT NULL,
    updated_at          DATETIME(6)   NOT NULL,

    CONSTRAINT fk_dev_document  FOREIGN KEY (document_id)  REFERENCES kpi_documents(id),
    CONSTRAINT fk_dev_evaluator FOREIGN KEY (evaluator_id) REFERENCES employees(id)
);
```

---

## 4. ERD tổng quan

```
departments ──────┐
(parent_id self)  ├── employees ──── accounts ──── account_roles ──── roles
positions ────────┘       │                         (ADMIN/DIRECTOR/MANAGER/EMPLOYEE)
                          │
kpi_categories ──── kpi_templates
                               │ (template_id nullable)
kpi_cycles ──── kpi_documents ──────────── kpi_items
                (target_type + target_id)       │
                (parent_doc_id cascade)     kpi_item_evaluations
                        │                  kpi_tracking_logs ──── kpi_attachments
                kpi_document_evaluations
```

---

## 5. Luồng trạng thái

### `kpi_cycles.status`
```
PLANNING ──→ ACTIVE ──→ EVALUATING ──→ CLOSED
```

### `kpi_documents.status`
```
                    ┌── REJECTED ──→ (về DRAFT để sửa)
DRAFT ──→ PENDING_APPROVAL ──→ APPROVED ──→ IN_PROGRESS
                                                  │
                                        SELF_EVALUATED
                                                  │
                                       MANAGER_EVALUATED
                                                  │
                                              CLOSED
```

---

## 6. Index cần tạo

```sql
-- Tra cứu KPI theo target (dùng thường xuyên nhất)
CREATE INDEX idx_doc_target        ON kpi_documents(target_type, target_id);
CREATE INDEX idx_doc_cycle_target  ON kpi_documents(cycle_id, target_type, target_id);
CREATE INDEX idx_doc_parent        ON kpi_documents(parent_doc_id);

-- KPI items theo document
CREATE INDEX idx_item_document     ON kpi_items(document_id);

-- Tiến độ theo item
CREATE INDEX idx_log_item          ON kpi_tracking_logs(kpi_item_id);
CREATE INDEX idx_log_reporter      ON kpi_tracking_logs(reporter_id);

-- Minh chứng theo log
CREATE INDEX idx_att_log           ON kpi_attachments(tracking_log_id);

-- Nhân viên theo phòng ban
CREATE INDEX idx_emp_department    ON employees(department_id);
```

---

## 7. Query ví dụ quan trọng

```sql
-- 1. Lấy tất cả KPI của phòng IT (dept_id=3) trong chu kỳ Q3 (cycle_id=5)
SELECT * FROM kpi_documents
WHERE target_type = 'DEPARTMENT'
  AND target_id = 3
  AND cycle_id = 5
  AND is_deleted = FALSE;

-- 2. Lấy KPI cá nhân của nhân viên id=12, kèm thông tin phiếu phòng ban cha
SELECT
    emp_doc.*,
    dept_doc.target_id AS parent_department_id
FROM kpi_documents emp_doc
LEFT JOIN kpi_documents dept_doc ON emp_doc.parent_doc_id = dept_doc.id
WHERE emp_doc.target_type = 'EMPLOYEE'
  AND emp_doc.target_id   = 12
  AND emp_doc.is_deleted  = FALSE;

-- 3. Lấy toàn bộ cây KPI cascade từ công ty → phòng → cá nhân trong chu kỳ
SELECT
    d.id, d.target_type, d.target_id, d.parent_doc_id, d.status,
    CASE d.target_type
        WHEN 'DEPARTMENT' THEN dept.name
        WHEN 'EMPLOYEE'   THEN emp.full_name
        ELSE 'Công ty'
    END AS target_name
FROM kpi_documents d
LEFT JOIN departments dept ON d.target_type = 'DEPARTMENT' AND d.target_id = dept.id
LEFT JOIN employees   emp  ON d.target_type = 'EMPLOYEE'   AND d.target_id = emp.id
WHERE d.cycle_id   = 5
  AND d.is_deleted = FALSE
ORDER BY d.parent_doc_id NULLS FIRST, d.id;

-- 4. Tính tiến độ hoàn thành KPI của nhân viên id=12
SELECT
    i.name,
    i.target_value,
    i.current_value,
    ROUND(i.current_value / i.target_value * 100, 2) AS completion_pct,
    i.weight
FROM kpi_items i
JOIN kpi_documents d ON i.document_id = d.id
WHERE d.target_type = 'EMPLOYEE'
  AND d.target_id   = 12
  AND d.cycle_id    = 5
  AND i.is_deleted  = FALSE;
```

---

## 8. Validation tầng Service (bù cho thiếu FK constraint)

Vì `target_id` là polymorphic (không có FK cứng), tầng Service phải validate:

```java
// Khi tạo kpi_document mới
switch (targetType) {
    case "DEPARTMENT" -> departmentRepository.findById(targetId)
        .orElseThrow(() -> new NotFoundException("Phòng ban không tồn tại"));
    case "EMPLOYEE" -> employeeRepository.findById(targetId)
        .orElseThrow(() -> new NotFoundException("Nhân viên không tồn tại"));
    case "COMPANY" -> {
        if (targetId != null) throw new BadRequestException("KPI công ty không cần target_id");
    }
    default -> throw new BadRequestException("target_type không hợp lệ");
}
```

---

## 9. Khuyến nghị triển khai

| Mục | Khuyến nghị |
|-----|------------|
| Migration | Dùng **Flyway** hoặc **Liquibase**, không để `ddl-auto=update` |
| Soft delete | Tất cả bảng có `is_deleted`, mọi query phải thêm `WHERE is_deleted = FALSE` |
| File lưu trữ | `kpi_attachments.file_url` trỏ tới MinIO/S3, không lưu binary trong DB |
| Enum | Dùng `VARCHAR` thay vì MySQL `ENUM` để dễ mở rộng |
| Audit log | Xem xét thêm bảng `audit_logs` (actor, action, entity_type, entity_id, diff_json) |
| `target_type` | Validate bằng Java enum trong Service, không để string tự do |
