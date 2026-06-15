# Luồng KPI Cascade 3 Cấp — Chi tiết đầy đủ

---

## Bức tranh tổng thể

```
PHASE 1: SETUP          PHASE 2: CASCADE            PHASE 3: TRACKING       PHASE 4: EVALUATE
──────────────          ────────────────            ─────────────────       ─────────────────
ADMIN tạo chu kỳ   →   DIRECTOR tạo KPI cty   →   EMPLOYEE cập nhật   →   EMPLOYEE tự chấm
ADMIN kích hoạt    →   DIRECTOR giao phòng    →   MANAGER theo dõi    →   MANAGER chấm điểm
                   →   MANAGER giao NV        →                       →   MANAGER chốt kết quả
                                                                       →   ADMIN đóng chu kỳ
```

---

## PHASE 1 — Khởi tạo chu kỳ

### Bước 1.1 — ADMIN tạo chu kỳ mới

**Actor:** ADMIN

**Action:** Tạo chu kỳ đánh giá Q3/2025

**Dữ liệu tạo ra:**
```sql
INSERT INTO kpi_cycles VALUES (
  cycle_code = 'Q3-2025',
  name       = 'Quý 3 năm 2025',
  type       = 'QUARTERLY',
  start_date = '2025-07-01',
  end_date   = '2025-09-30',
  status     = 'PLANNING',     ← chưa ai làm được gì
  created_by = admin_id
)
```

**Trạng thái:** `PLANNING` — toàn hệ thống chưa ai tạo KPI được

---

### Bước 1.2 — ADMIN kích hoạt chu kỳ

**Actor:** ADMIN

**Action:** Chuyển trạng thái cycle sang ACTIVE

```sql
UPDATE kpi_cycles SET status = 'ACTIVE' WHERE id = 1
```

**Trạng thái:** `ACTIVE` — hệ thống mở cửa để tạo KPI và cập nhật tiến độ

> ✅ **Từ bước này, DIRECTOR mới được tạo KPI**

---

## PHASE 2 — KPI Cascade: Công ty → Phòng ban → Cá nhân

### Bước 2.1 — DIRECTOR tạo KPI cấp Công ty

**Actor:** DIRECTOR (Ban Giám đốc)

**Action:** Xác định mục tiêu chiến lược toàn công ty cho Q3

**Dữ liệu tạo ra — `kpi_documents`:**
```sql
INSERT INTO kpi_documents VALUES (
  document_code = 'DOC-COMPANY-Q3-2025',
  cycle_id      = 1,
  target_type   = 'COMPANY',   ← KPI cấp công ty
  target_id     = NULL,        ← không gắn với ai cụ thể
  parent_doc_id = NULL,        ← đây là gốc của cascade
  source_type   = 'ASSIGNED',
  created_by    = director_id,
  approver_id   = NULL,        ← tự phê duyệt
  status        = 'APPROVED'   ← DIRECTOR tự duyệt KPI công ty
)
```

**Dữ liệu tạo ra — `kpi_items` (các tiêu chí chiến lược):**
```sql
-- Tiêu chí 1
INSERT INTO kpi_items VALUES (
  document_id  = 1,
  name         = 'Doanh thu toàn công ty Q3',
  unit         = 'VND',
  target_type  = 'HIGHER_BETTER',
  weight       = 60,
  target_value = 50000000000,   -- 50 tỷ
  current_value = 0
)

-- Tiêu chí 2
INSERT INTO kpi_items VALUES (
  document_id  = 1,
  name         = 'Tỷ lệ giữ chân khách hàng',
  unit         = '%',
  target_type  = 'HIGHER_BETTER',
  weight       = 40,
  target_value = 85,
  current_value = 0
)
```

**Kết quả:**
```
kpi_document #1 [COMPANY / Q3-2025]
├── item #1: Doanh thu toàn công ty    weight=60%   target=50 tỷ
└── item #2: Tỷ lệ giữ chân KH        weight=40%   target=85%
```

---

### Bước 2.2 — DIRECTOR giao KPI cho từng Phòng ban

**Actor:** DIRECTOR

**Action:** Phân bổ mục tiêu doanh thu cho Phòng Kinh Doanh

**Dữ liệu tạo ra — `kpi_documents` (KPI Phòng KD):**
```sql
INSERT INTO kpi_documents VALUES (
  document_code = 'DOC-DEPT-KD-Q3-2025',
  cycle_id      = 1,
  target_type   = 'DEPARTMENT',   ← KPI cấp phòng ban
  target_id     = 3,              ← Phòng Kinh Doanh id=3
  parent_doc_id = 1,              ← con của KPI công ty #1
  source_type   = 'ASSIGNED',
  created_by    = director_id,
  approver_id   = director_id,
  status        = 'APPROVED'
)
```

**`kpi_items` cho Phòng KD:**
```sql
INSERT INTO kpi_items VALUES (
  document_id  = 2,
  name         = 'Doanh thu Phòng Kinh Doanh Q3',
  unit         = 'VND',
  target_type  = 'HIGHER_BETTER',
  weight       = 70,
  target_value = 30000000000   -- 30 tỷ (đóng góp 60% mục tiêu công ty)
)

INSERT INTO kpi_items VALUES (
  document_id  = 2,
  name         = 'Số hợp đồng mới ký kết',
  unit         = 'Hợp đồng',
  target_type  = 'HIGHER_BETTER',
  weight       = 30,
  target_value = 50
)
```

**Tương tự, DIRECTOR giao KPI cho Phòng IT:**
```sql
INSERT INTO kpi_documents VALUES (
  target_type   = 'DEPARTMENT',
  target_id     = 5,              ← Phòng IT id=5
  parent_doc_id = 1,              ← cùng gốc KPI công ty #1
  status        = 'APPROVED'
)

-- item: Uptime hệ thống, weight=50%, target=99.9%
-- item: Số bug nghiêm trọng, weight=50%, target=5 (LOWER_BETTER)
```

**Kết quả cascade sau bước 2.2:**
```
kpi_document #1 [COMPANY]
├── item: Doanh thu công ty    50 tỷ
└── item: Giữ chân KH          85%
     │
     ├── kpi_document #2 [DEPARTMENT: Phòng KD]
     │   ├── item: Doanh thu phòng KD    30 tỷ
     │   └── item: Số HĐ ký mới          50 HĐ
     │
     └── kpi_document #3 [DEPARTMENT: Phòng IT]
         ├── item: Uptime hệ thống        99.9%
         └── item: Bug nghiêm trọng       ≤ 5
```

---

### Bước 2.3 — MANAGER giao KPI cho Nhân viên

**Actor:** Trưởng Phòng KD (có role MANAGER)

**Điều kiện:** `cycle.status = ACTIVE`, đã có KPI phòng ban từ bước 2.2

**Action:** Tạo KPI cá nhân cho từng nhân viên trong phòng

**Dữ liệu tạo ra — `kpi_documents` (KPI Nhân viên A):**
```sql
INSERT INTO kpi_documents VALUES (
  document_code = 'DOC-EMP-12-Q3-2025',
  cycle_id      = 1,
  target_type   = 'EMPLOYEE',   ← KPI cấp cá nhân
  target_id     = 12,           ← Nhân viên Sales A id=12
  parent_doc_id = 2,            ← con của KPI Phòng KD #2
  source_type   = 'ASSIGNED',
  created_by    = manager_id,
  approver_id   = manager_id,
  status        = 'DRAFT'       ← bắt đầu từ DRAFT, cần gửi cho NV xác nhận
)
```

**`kpi_items` cho Nhân viên A:**
```sql
INSERT INTO kpi_items VALUES (
  document_id  = 4,
  template_id  = 7,              ← dùng từ template mẫu "Doanh thu cá nhân"
  name         = 'Doanh thu cá nhân Q3',
  unit         = 'VND',
  target_type  = 'HIGHER_BETTER',
  weight       = 50,
  target_value = 2000000000     -- 2 tỷ
)

INSERT INTO kpi_items VALUES (
  document_id  = 4,
  template_id  = NULL,           ← tạo thủ công không theo template
  name         = 'Số cuộc gặp khách hàng mới/tuần',
  unit         = 'Lần',
  target_type  = 'HIGHER_BETTER',
  weight       = 30,
  target_value = 5
)

INSERT INTO kpi_items VALUES (
  document_id  = 4,
  name         = 'Tỷ lệ chuyển đổi lead thành HĐ',
  unit         = '%',
  target_type  = 'HIGHER_BETTER',
  weight       = 20,
  target_value = 30
)
-- Kiểm tra: 50 + 30 + 20 = 100 ✅
```

**Action:** MANAGER gửi phiếu cho nhân viên
```sql
UPDATE kpi_documents
SET status = 'IN_PROGRESS', approved_at = NOW()
WHERE id = 4
-- DRAFT → IN_PROGRESS (hoặc qua PENDING_APPROVAL nếu NV cần xác nhận trước)
```

**Kết quả cascade đầy đủ:**
```
kpi_document #1 [COMPANY / Q3-2025]
└── kpi_document #2 [Phòng KD]
    ├── kpi_document #4 [Nhân viên A - Sales]
    │   ├── item: Doanh thu cá nhân        weight=50%  target=2 tỷ
    │   ├── item: Số cuộc gặp KH/tuần      weight=30%  target=5 lần
    │   └── item: Tỷ lệ chuyển đổi lead    weight=20%  target=30%
    │
    └── kpi_document #5 [Nhân viên B - Sales]
        ├── item: Doanh thu cá nhân        weight=50%  target=1.5 tỷ
        └── ...
```

---

## PHASE 3 — Tracking: Nhân viên cập nhật tiến độ

### Bước 3.1 — EMPLOYEE báo cáo tiến độ hàng tuần

**Actor:** Nhân viên A (id=12)

**Điều kiện:** `cycle.status = ACTIVE`, `document.status = IN_PROGRESS`

**Tuần 1 — Ký được 1 HĐ, doanh thu 300 triệu:**

```sql
-- Bước 3.1a: Tạo tracking log
INSERT INTO kpi_tracking_logs VALUES (
  kpi_item_id  = 6,               -- item "Doanh thu cá nhân"
  reporter_id  = 12,
  value_before = 0,
  value_delta  = 300000000,       -- +300 triệu tuần này
  value_after  = 300000000,
  notes        = 'Ký hợp đồng với Công ty XYZ ngày 05/07'
)

-- Bước 3.1b: Hệ thống tự cập nhật current_value
UPDATE kpi_items SET current_value = 300000000 WHERE id = 6
```

**Bước 3.1c — Đính kèm minh chứng (ảnh HĐ):**
```sql
INSERT INTO kpi_attachments VALUES (
  tracking_log_id  = 1,
  file_name        = 'hop-dong-xyz-05072025.pdf',
  file_url         = 'https://storage.company.com/kpi/2025/Q3/emp12/hd-xyz.pdf',
  file_type        = 'application/pdf',
  file_size_bytes  = 1024000,
  uploaded_by      = 12
)
```

**Tuần 2, 3, 4 — NV tiếp tục cập nhật:**
```
Tuần 1: +300tr   → current = 300tr    (15% mục tiêu)
Tuần 2: +450tr   → current = 750tr    (37.5%)
Tuần 3: +600tr   → current = 1350tr   (67.5%)
Tuần 4: +400tr   → current = 1750tr   (87.5%)
...
Cuối Q3: current = 2100tr             (105% — vượt mục tiêu)
```

---

### Bước 3.2 — MANAGER theo dõi tiến độ realtime

**Actor:** Trưởng Phòng KD

**Xem tổng quan phòng (query):**
```sql
SELECT
  e.full_name,
  d.document_code,
  SUM(i.weight * (i.current_value / i.target_value)) / 100 AS completion_pct
FROM kpi_documents d
JOIN employees e    ON d.target_type = 'EMPLOYEE' AND d.target_id = e.id
JOIN kpi_items i    ON i.document_id = d.id
WHERE d.cycle_id    = 1
  AND d.target_type = 'EMPLOYEE'
  AND e.department_id = 3   -- Phòng KD
GROUP BY e.id, d.id;

-- Kết quả:
-- Nhân viên A: 87.5%
-- Nhân viên B: 62.0%
-- Nhân viên C: 45.0%  ← cần chú ý
```

**MANAGER can thiệp nếu cần:** Có thể nhắn nhủ, điều chỉnh target nếu có lý do chính đáng (sửa `kpi_items.target_value` khi còn trong `IN_PROGRESS`)

---

## PHASE 4 — Đánh giá cuối kỳ

### Bước 4.1 — ADMIN mở giai đoạn đánh giá

```sql
UPDATE kpi_cycles SET status = 'EVALUATING' WHERE id = 1
-- Từ đây: không ai được cập nhật tracking log nữa
-- Nhân viên chuyển sang tự đánh giá
```

---

### Bước 4.2 — EMPLOYEE tự đánh giá (Self-evaluation)

**Actor:** Nhân viên A (id=12)

**Điều kiện:** `cycle.status = EVALUATING`

```sql
-- Tự chấm từng item
INSERT INTO kpi_item_evaluations VALUES (
  kpi_item_id   = 6,   -- "Doanh thu cá nhân"
  self_score    = 100, -- đạt 105%, tự chấm 100
  self_comment  = 'Vượt mục tiêu doanh thu nhờ ký được HĐ lớn với XYZ và ABC',
  self_eval_at  = NOW()
)

INSERT INTO kpi_item_evaluations VALUES (
  kpi_item_id   = 7,   -- "Số cuộc gặp KH/tuần"
  self_score    = 80,
  self_comment  = 'Trung bình 4 cuộc/tuần, thiếu 1 so với mục tiêu do 2 tuần công tác',
  self_eval_at  = NOW()
)

INSERT INTO kpi_item_evaluations VALUES (
  kpi_item_id   = 8,   -- "Tỷ lệ chuyển đổi"
  self_score    = 90,
  self_comment  = 'Đạt 32%, vượt mục tiêu 30%',
  self_eval_at  = NOW()
)

-- Cập nhật trạng thái phiếu
UPDATE kpi_documents SET status = 'SELF_EVALUATED' WHERE id = 4
```

---

### Bước 4.3 — MANAGER đánh giá và chốt điểm

**Actor:** Trưởng Phòng KD

**Xem self_score của NV, lịch sử tracking, minh chứng → chấm điểm:**

```sql
-- Cập nhật manager_score cho từng item
UPDATE kpi_item_evaluations SET
  manager_score   = 100,
  manager_comment = 'Xác nhận vượt chỉ tiêu doanh thu. Hợp đồng XYZ và ABC đã được xác thực.',
  manager_eval_at = NOW(),
  final_score     = 100
WHERE kpi_item_id = 6;

UPDATE kpi_item_evaluations SET
  manager_score   = 75,   -- chấm thấp hơn NV tự chấm (80)
  manager_comment = 'Công nhận khó khăn khi công tác, nhưng cần cải thiện sắp xếp thời gian.',
  final_score     = 75
WHERE kpi_item_id = 7;

UPDATE kpi_item_evaluations SET
  manager_score   = 90,
  manager_comment = 'Tỷ lệ chuyển đổi tốt, duy trì phong độ.',
  final_score     = 90
WHERE kpi_item_id = 8;
```

**Hệ thống cảnh báo nếu chênh lệch > 20:**
```
item #7: self_score=80, manager_score=75 → chênh 5 → OK
item #6: self_score=100, manager_score=100 → OK
```

---

### Bước 4.4 — Tính điểm tổng và xếp loại

**Công thức:**
```
total_final_score = Σ (final_score × weight / 100)

= (100 × 50/100) + (75 × 30/100) + (90 × 20/100)
= 50 + 22.5 + 18
= 90.5 điểm

→ rating = 'EXCELLENT'  (≥ 90)
```

```sql
INSERT INTO kpi_document_evaluations VALUES (
  document_id         = 4,
  total_self_score    = (100×50 + 80×30 + 90×20) / 100,   -- 91.0
  total_manager_score = (100×50 + 75×30 + 90×20) / 100,   -- 90.5
  total_final_score   = 90.5,
  rating              = 'EXCELLENT',
  evaluator_id        = manager_id,
  evaluated_at        = NOW()
)

-- Đóng phiếu NV
UPDATE kpi_documents SET status = 'CLOSED', closed_at = NOW() WHERE id = 4
```

---

### Bước 4.5 — ADMIN đóng chu kỳ

```sql
UPDATE kpi_cycles SET status = 'CLOSED' WHERE id = 1
-- Toàn bộ dữ liệu chu kỳ Q3-2025 chuyển sang READ-ONLY
```

---

## Sơ đồ toàn bộ luồng

```
┌─────────────────────────────────────────────────────────────────────┐
│  PHASE 1: SETUP                                                     │
│  ADMIN: Tạo cycle (PLANNING) → Kích hoạt (ACTIVE)                  │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ cycle = ACTIVE
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│  PHASE 2: CASCADE                                                   │
│                                                                     │
│  DIRECTOR                                                           │
│    [1] Tạo KPI Công ty → doc#1 [COMPANY, APPROVED]                 │
│        items: Doanh thu 50 tỷ (60%), Giữ chân KH 85% (40%)        │
│                                                                     │
│    [2] Giao Phòng KD  → doc#2 [DEPARTMENT=3, APPROVED]             │
│        items: DT phòng 30 tỷ (70%), Số HĐ 50 (30%)                │
│                                                                     │
│    [3] Giao Phòng IT  → doc#3 [DEPARTMENT=5, APPROVED]             │
│        items: Uptime 99.9% (50%), Bug ≤5 (50%)                     │
│                                                                     │
│  MANAGER (Trưởng Phòng KD)                                         │
│    [4] Giao NV A → doc#4 [EMPLOYEE=12, IN_PROGRESS]                │
│        items: DT 2 tỷ (50%), Gặp KH 5/tuần (30%), Chuyển đổi (20%)│
│                                                                     │
│    [5] Giao NV B → doc#5 [EMPLOYEE=15, IN_PROGRESS]                │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ cycle = ACTIVE
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│  PHASE 3: TRACKING (suốt thời gian cycle ACTIVE)                   │
│                                                                     │
│  EMPLOYEE (NV A):                                                   │
│    Tuần 1: log +300tr → attach PDF hợp đồng XYZ                    │
│    Tuần 2: log +450tr → attach ảnh biên bản nghiệm thu             │
│    Tuần 3: log +600tr → ...                                         │
│    Tuần 4: log +400tr → current_value = 1.75 tỷ                    │
│    ...                  current_value = 2.1 tỷ (105%)              │
│                                                                     │
│  MANAGER: Xem dashboard tiến độ realtime                           │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ ADMIN: cycle = EVALUATING
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│  PHASE 4: EVALUATE                                                  │
│                                                                     │
│  EMPLOYEE (NV A):                                                   │
│    Self-eval từng item → doc#4: IN_PROGRESS → SELF_EVALUATED        │
│                                                                     │
│  MANAGER:                                                           │
│    Chấm manager_score từng item                                     │
│    Tính total_final_score = 90.5                                    │
│    rating = EXCELLENT                                               │
│    doc#4: SELF_EVALUATED → MANAGER_EVALUATED → CLOSED              │
│                                                                     │
│  ADMIN: cycle = CLOSED → toàn bộ READ-ONLY                         │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Snapshot dữ liệu DB cuối luồng

| Table | Records tạo ra |
|-------|---------------|
| `kpi_cycles` | 1 bản ghi (Q3-2025) |
| `kpi_documents` | 5+ bản ghi (1 COMPANY + 2 DEPARTMENT + 2+ EMPLOYEE) |
| `kpi_items` | ~10-15 bản ghi (mỗi document 2-3 items) |
| `kpi_tracking_logs` | Hàng chục bản ghi (cập nhật hàng tuần × số NV × số items) |
| `kpi_attachments` | Hàng chục file minh chứng |
| `kpi_item_evaluations` | 1 bản/item (khi đánh giá cuối kỳ) |
| `kpi_document_evaluations` | 1 bản/document (khi chốt điểm) |
