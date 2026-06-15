# Phân tích Yêu cầu Hệ thống — KPI Management Platform

---

## 1. Tổng quan hệ thống

### 1.1 Mục tiêu
Xây dựng nền tảng quản lý KPI nội bộ giúp doanh nghiệp:
- Thiết lập mục tiêu chiến lược từ cấp công ty, lan xuống phòng ban, rồi đến từng cá nhân (**KPI Cascade**)
- Theo dõi tiến độ thực hiện theo thời gian thực
- Đánh giá kết quả cuối kỳ một cách minh bạch, có căn cứ

### 1.2 Phạm vi
| Phạm vi | Có | Không có |
|---------|----|----------|
| Quản lý tài khoản, phòng ban, chức vụ, vai trò | ✅ | |
| Tạo và quản lý chu kỳ đánh giá | ✅ | |
| Bộ tiêu chí mẫu (template) | ✅ | |
| KPI Cascade 3 cấp | ✅ | |
| Phê duyệt KPI do NV đề xuất | ✅ | |
| Cập nhật tiến độ + đính kèm minh chứng | ✅ | |
| Đánh giá cuối kỳ có trọng số | ✅ | |
| Quản lý task/todo để đạt KPI | | ❌ |
| Tích hợp lương/thưởng tự động | | ❌ |
| Chat nội bộ | | ❌ |

---

## 2. Phân cấp tổ chức

### 2.1 Cấu trúc 3 cấp

```
Cấp 1 — Công ty (COMPANY)
    Mục tiêu chiến lược toàn công ty
    VD: "Doanh thu 2025: 50 tỷ", "Tỷ lệ giữ chân KH: 85%"
         │
         ▼
Cấp 2 — Phòng ban (DEPARTMENT)
    Mục tiêu từng phòng, đóng góp vào cấp 1
    VD: Phòng KD: "Doanh thu: 30 tỷ"
        Phòng IT: "Uptime hệ thống: 99.9%"
         │
         ▼
Cấp 3 — Cá nhân (EMPLOYEE)
    Mục tiêu từng nhân viên, đóng góp vào cấp 2
    VD: NV Sales A: "Doanh thu cá nhân: 2 tỷ"
        Developer B: "Story points: 40/sprint"
```

### 2.2 Phòng ban có thể phân cấp cha - con

```
Phòng Kinh Doanh (parent_id = NULL)
├── Nhóm Sales Miền Bắc (parent_id = PKD)
└── Nhóm Sales Miền Nam (parent_id = PKD)

Phòng Công nghệ (parent_id = NULL)
├── Nhóm Backend (parent_id = PCT)
└── Nhóm Frontend (parent_id = PCT)
```

---

## 3. Phân tích theo Role

### 3.1 Tổng hợp 4 Role

| Role | Đại diện | Quyền chính |
|------|----------|-------------|
| `ADMIN` | Bộ phận IT / HR quản trị | Quản lý toàn hệ thống |
| `DIRECTOR` | Ban Giám đốc, Giám đốc bộ phận | Tạo KPI công ty, giao cho phòng |
| `MANAGER` | Trưởng phòng, Trưởng nhóm | Giao KPI cho nhân viên, duyệt đề xuất |
| `EMPLOYEE` | Nhân viên | Xem KPI, cập nhật tiến độ, đề xuất |

> **Lưu ý:** Trưởng phòng thường có cả `MANAGER` + `EMPLOYEE` vì họ vừa giao KPI cho nhân viên, vừa có KPI cá nhân do cấp trên giao.

---

### 3.2 ADMIN — Quản trị viên hệ thống

**Chức năng:**

| STT | Chức năng | Mô tả |
|-----|-----------|-------|
| A01 | Quản lý tài khoản | Tạo, sửa, khóa tài khoản người dùng |
| A02 | Phân quyền | Gán / thu hồi role cho tài khoản |
| A03 | Quản lý phòng ban | CRUD phòng ban, cấu hình cây tổ chức |
| A04 | Quản lý chức vụ | CRUD chức vụ và cấp bậc |
| A05 | Quản lý chu kỳ | Tạo chu kỳ, chuyển trạng thái chu kỳ |
| A06 | Quản lý template | CRUD bộ tiêu chí KPI mẫu và danh mục |
| A07 | Xem báo cáo tổng hợp | Dashboard toàn hệ thống |

**Quy tắc nghiệp vụ:**
- ADMIN không tự tạo KPI — chỉ quản trị hệ thống
- Chỉ ADMIN mới được chuyển trạng thái `kpi_cycles` (PLANNING → ACTIVE → EVALUATING → CLOSED)
- Khi khóa tài khoản, các KPI đang IN_PROGRESS vẫn giữ nguyên, không tự huỷ

---

### 3.3 DIRECTOR — Ban Giám đốc

**Chức năng:**

| STT | Chức năng | Mô tả |
|-----|-----------|-------|
| D01 | Tạo KPI cấp công ty | Tạo `kpi_document` với `target_type='COMPANY'` |
| D02 | Giao KPI cho phòng ban | Tạo `kpi_document` với `target_type='DEPARTMENT'`, liên kết `parent_doc_id` |
| D03 | Theo dõi tiến độ toàn công ty | Xem dashboard tổng hợp tất cả phòng ban |
| D04 | Xem báo cáo so sánh phòng ban | So sánh % hoàn thành KPI giữa các phòng |
| D05 | Chốt KPI cuối kỳ cấp công ty | Đánh giá và đóng phiếu KPI công ty |

**Quy tắc nghiệp vụ:**
- DIRECTOR chỉ tạo được KPI khi `kpi_cycle.status = 'ACTIVE'`
- 1 phòng ban chỉ có đúng 1 phiếu KPI trong 1 chu kỳ (UNIQUE constraint)
- KPI phòng ban nên được tạo trước, để MANAGER dựa vào đó giao cho nhân viên

---

### 3.4 MANAGER — Trưởng phòng / Trưởng nhóm

**Chức năng:**

| STT | Chức năng | Mô tả |
|-----|-----------|-------|
| M01 | Xem KPI phòng ban của mình | Xem phiếu KPI phòng do DIRECTOR giao |
| M02 | Giao KPI cho nhân viên | Tạo `kpi_document` với `target_type='EMPLOYEE'` cho NV thuộc quyền |
| M03 | Thêm tiêu chí vào phiếu KPI | Tạo `kpi_items` cho phiếu vừa tạo |
| M04 | Duyệt KPI do NV đề xuất | Chuyển `source_type='PROPOSED'` từ PENDING_APPROVAL → APPROVED hoặc REJECTED |
| M05 | Theo dõi tiến độ nhân viên | Xem `current_value` và lịch sử tracking của từng NV |
| M06 | Đánh giá cuối kỳ | Chấm `manager_score` cho từng `kpi_item` của NV thuộc quyền |
| M07 | Chốt điểm tổng kết | Tạo `kpi_document_evaluation` với `total_final_score` và `rating` |

**Quy tắc nghiệp vụ:**
- MANAGER chỉ được giao/xem KPI của nhân viên **thuộc phòng ban mình quản lý**
- Tổng `weight` của tất cả `kpi_items` trong 1 phiếu phải bằng **100%**
- Chỉ được duyệt KPI của nhân viên đề xuất khi `cycle.status = 'ACTIVE'`
- Chỉ được chấm điểm khi `cycle.status = 'EVALUATING'`

---

### 3.5 EMPLOYEE — Nhân viên

**Chức năng:**

| STT | Chức năng | Mô tả |
|-----|-----------|-------|
| E01 | Xem KPI được giao | Xem phiếu và từng tiêu chí KPI của mình |
| E02 | Đề xuất KPI cá nhân | Tạo phiếu với `source_type='PROPOSED'`, chờ manager duyệt |
| E03 | Cập nhật tiến độ | Ghi `kpi_tracking_log` với `value_delta` và ghi chú |
| E04 | Đính kèm minh chứng | Upload file vào `kpi_attachments` gắn với tracking log |
| E05 | Tự đánh giá cuối kỳ | Điền `self_score` và `self_comment` cho từng `kpi_item` |
| E06 | Xem lịch sử tiến độ | Xem biểu đồ `current_value` theo thời gian |

**Quy tắc nghiệp vụ:**
- Chỉ được cập nhật tiến độ khi `kpi_document.status = 'IN_PROGRESS'` và `cycle.status = 'ACTIVE'`
- Chỉ được tự đánh giá khi `cycle.status = 'EVALUATING'`
- Không được sửa lại tracking log đã ghi — chỉ được ghi log mới để điều chỉnh
- Đề xuất KPI chỉ được tạo khi `cycle.status = 'ACTIVE'` và hệ thống cho phép (`allow_proposal = TRUE` trong cycle)

---

## 4. Luồng nghiệp vụ chi tiết

### 4.1 Luồng Thiết lập KPI (đầu kỳ)

```
[ADMIN] Tạo chu kỳ mới
   kpi_cycle: status = PLANNING
        │
        ▼
[ADMIN] Kích hoạt chu kỳ
   kpi_cycle: status = PLANNING → ACTIVE
        │
        ▼
[DIRECTOR] Tạo KPI cấp Công ty
   kpi_document: target_type=COMPANY, status=APPROVED (tự duyệt)
   kpi_items: thêm các tiêu chí chiến lược
        │
        ▼
[DIRECTOR] Giao KPI cho từng Phòng ban
   kpi_document: target_type=DEPARTMENT, parent_doc_id=KPI công ty
   kpi_items: phân bổ mục tiêu cụ thể cho phòng
        │
        ▼
[MANAGER] Giao KPI cho từng Nhân viên
   kpi_document: target_type=EMPLOYEE, parent_doc_id=KPI phòng
   kpi_items: tiêu chí đo lường cá nhân
   → Gửi phiếu: status = IN_PROGRESS
```

---

### 4.2 Luồng Đề xuất KPI (nhân viên tự đề xuất)

```
[EMPLOYEE] Tạo phiếu đề xuất
   kpi_document: source_type=PROPOSED, status=DRAFT
   kpi_items: thêm tiêu chí muốn đề xuất
        │
        ▼
[EMPLOYEE] Gửi đề xuất
   kpi_document: status = DRAFT → PENDING_APPROVAL
        │
        ├─── [MANAGER] Duyệt ──────→ status = APPROVED → IN_PROGRESS
        │
        └─── [MANAGER] Từ chối ───→ status = REJECTED
                                     (EMPLOYEE sửa lại → DRAFT → gửi lại)
```

---

### 4.3 Luồng Cập nhật Tiến độ (trong kỳ)

```
Điều kiện: cycle.status = ACTIVE
           kpi_document.status = IN_PROGRESS

[EMPLOYEE] Báo cáo tiến độ định kỳ (tuần/tháng)
        │
        ▼
   Chọn kpi_item cần cập nhật
        │
        ▼
   Nhập value_delta (lượng tăng/giảm lần này)
   Nhập ghi chú (notes)
        │
        ▼
   Hệ thống tự tính:
     value_before = kpi_item.current_value
     value_after  = value_before + value_delta
     → Lưu kpi_tracking_log
     → Cập nhật kpi_item.current_value = value_after
        │
        ▼
   [Tùy chọn] Đính kèm minh chứng
     Upload file → lưu kpi_attachments
     (ảnh chụp HĐ, báo cáo, screenshot...)
        │
        ▼
[MANAGER] Xem tiến độ realtime trên dashboard
```

---

### 4.4 Luồng Đánh giá Cuối kỳ

```
[ADMIN] Chuyển chu kỳ sang giai đoạn đánh giá
   kpi_cycle: status = ACTIVE → EVALUATING
        │
        ▼
[EMPLOYEE] Tự đánh giá (self-evaluation)
   Với từng kpi_item: điền self_score (0-100) + self_comment
   kpi_document: status = IN_PROGRESS → SELF_EVALUATED
        │
        ▼
[MANAGER] Đánh giá của quản lý
   Với từng kpi_item: điền manager_score (0-100) + manager_comment
   Tham khảo: self_score của NV, lịch sử tracking, minh chứng
   kpi_document: status = SELF_EVALUATED → MANAGER_EVALUATED
        │
        ▼
[MANAGER] Chốt điểm tổng kết
   Hệ thống tính total_final_score (theo công thức có trọng số)
   Gán xếp loại (rating)
   kpi_document: status = MANAGER_EVALUATED → CLOSED
        │
        ▼
[ADMIN] Đóng chu kỳ
   kpi_cycle: status = EVALUATING → CLOSED
   Toàn bộ dữ liệu chu kỳ chuyển sang read-only
```

---

## 5. Cơ chế Đánh giá cuối kỳ

### 5.1 Công thức tính điểm từng KPI Item

```
completion_rate = (current_value / target_value) × 100%

item_score = f(completion_rate, target_type)
```

Hàm tính điểm theo `target_type`:

| target_type | Ý nghĩa | Công thức item_score |
|-------------|---------|---------------------|
| `HIGHER_BETTER` | Càng cao càng tốt (doanh thu, số HĐ) | `MIN(completion_rate, 100)` |
| `LOWER_BETTER` | Càng thấp càng tốt (tỷ lệ lỗi, chi phí) | `MAX(0, 100 - (actual/target - 1) × 100)` |
| `EXACT` | Đạt đúng mục tiêu (tỷ lệ % chuẩn) | `MAX(0, 100 - ABS(actual - target) × hệ số)` |

**Ví dụ `HIGHER_BETTER`:**
```
target_value  = 10 hợp đồng
current_value = 8 hợp đồng
completion_rate = 8/10 × 100 = 80%
item_score = 80 điểm
```

**Ví dụ `LOWER_BETTER`:**
```
target_value  = 5 bug nghiêm trọng (mục tiêu tối đa)
current_value = 3 bug thực tế
→ Tốt hơn mục tiêu → item_score = 100 điểm

current_value = 8 bug thực tế
→ Vượt mục tiêu xấu → item_score = MAX(0, 100 - (8/5-1)×100) = MAX(0, 40) = 40 điểm
```

---

### 5.2 Công thức tính điểm tổng phiếu

```
total_score = Σ (item_final_score × item_weight / 100)
```

Trong đó `item_final_score` có thể là:
- `manager_score` (ưu tiên)
- `self_score` nếu manager chưa chấm

**Ví dụ:**
```
kpi_item #1: "Doanh thu"       weight=40%, manager_score=80
kpi_item #2: "Số HĐ ký mới"   weight=30%, manager_score=90
kpi_item #3: "Hài lòng KH"    weight=30%, manager_score=75

total_final_score = (80×40 + 90×30 + 75×30) / 100
                  = (3200 + 2700 + 2250) / 100
                  = 81.5 điểm
```

---

### 5.3 Thang xếp loại

| Điểm | Xếp loại | Mô tả |
|------|----------|-------|
| ≥ 90 | `EXCELLENT` | Hoàn thành xuất sắc |
| 75 – 89 | `GOOD` | Hoàn thành tốt |
| 60 – 74 | `SATISFACTORY` | Hoàn thành |
| 40 – 59 | `NEEDS_IMPROVEMENT` | Cần cải thiện |
| < 40 | `POOR` | Không hoàn thành |

---

### 5.4 Quy tắc đặc biệt trong đánh giá

| Quy tắc | Mô tả |
|---------|-------|
| **Tiêu chí bắt buộc** | Nếu 1 item được đánh dấu `is_mandatory=TRUE` mà đạt < 50%, tự động hạ 1 bậc xếp loại dù tổng điểm cao |
| **Chặn đánh giá** | NV không được nhận `EXCELLENT` nếu có item nào đạt < 60% |
| **Điểm làm tròn** | Làm tròn đến 2 chữ số thập phân |
| **Self vs Manager** | Nếu chênh lệch `|self_score - manager_score| > 20`, hệ thống cảnh báo để đôi bên thảo luận lại |

---

## 6. Quy tắc nghiệp vụ tổng hợp

### 6.1 Ràng buộc dữ liệu

| Ràng buộc | Mô tả |
|-----------|-------|
| Unique phiếu KPI | Mỗi `(target_type, target_id, cycle_id)` chỉ có 1 phiếu |
| Tổng trọng số | Tổng `weight` của các item trong 1 document = 100 |
| Thứ tự thao tác | Không cập nhật tiến độ khi cycle chưa `ACTIVE` |
| Không sửa log | `kpi_tracking_log` chỉ được INSERT, không UPDATE/DELETE |
| Read-only sau CLOSED | Khi `cycle.status = CLOSED`, không được thay đổi bất kỳ dữ liệu nào trong chu kỳ đó |

### 6.2 Phân quyền dữ liệu

| Role | Xem được | Tạo/Sửa được |
|------|----------|--------------|
| ADMIN | Tất cả | Tài khoản, phòng ban, chu kỳ, template |
| DIRECTOR | Tất cả KPI trong hệ thống | KPI cấp COMPANY và DEPARTMENT |
| MANAGER | KPI của phòng mình + nhân viên thuộc quyền | KPI EMPLOYEE cho NV dưới quyền |
| EMPLOYEE | Chỉ KPI của chính mình | Tracking log, self-evaluation, đề xuất |

### 6.3 Trạng thái hợp lệ để thao tác

| Thao tác | cycle.status | document.status |
|----------|-------------|----------------|
| Tạo phiếu KPI | ACTIVE | — |
| Thêm KPI items | ACTIVE | DRAFT hoặc APPROVED |
| Cập nhật tiến độ | ACTIVE | IN_PROGRESS |
| Tự đánh giá | EVALUATING | IN_PROGRESS |
| Manager đánh giá | EVALUATING | SELF_EVALUATED |
| Xem báo cáo | Bất kỳ | Bất kỳ |

---

## 7. Màn hình chính theo Role

### ADMIN
- **Dashboard hệ thống**: Tổng số user, phòng ban, chu kỳ đang active
- **Quản lý tài khoản**: Danh sách, tìm kiếm, tạo/sửa/khóa
- **Quản lý tổ chức**: Cây phòng ban, chức vụ
- **Quản lý chu kỳ**: Tạo chu kỳ, chuyển trạng thái
- **Thư viện template**: CRUD tiêu chí mẫu và danh mục

### DIRECTOR
- **Dashboard tổng quan**: % hoàn thành KPI toàn công ty, từng phòng ban
- **Tạo KPI công ty**: Form tạo phiếu + thêm tiêu chí
- **Giao KPI phòng ban**: Giao từng phòng, xem cascade tree
- **Báo cáo so sánh**: Biểu đồ so sánh % hoàn thành giữa các phòng

### MANAGER
- **Dashboard phòng ban**: % hoàn thành của từng NV trong phòng
- **Danh sách phiếu KPI NV**: Xem, giao mới, theo dõi tiến độ
- **Duyệt đề xuất**: Danh sách KPI đề xuất cần duyệt, APPROVE/REJECT
- **Đánh giá cuối kỳ**: Form chấm điểm từng item cho từng NV

### EMPLOYEE
- **Phiếu KPI của tôi**: Xem chi tiết các tiêu chí, % hoàn thành, deadline
- **Cập nhật tiến độ**: Chọn item → nhập delta → đính kèm minh chứng
- **Lịch sử cập nhật**: Timeline tracking log theo từng item
- **Đề xuất KPI**: Form tạo đề xuất, theo dõi trạng thái duyệt
- **Tự đánh giá**: Form điền self_score + comment cuối kỳ

---

## 8. Bảng tóm tắt Use Case

| Use Case | ADMIN | DIRECTOR | MANAGER | EMPLOYEE |
|----------|-------|----------|---------|----------|
| Quản lý tài khoản | ✅ | | | |
| Quản lý phòng ban / chức vụ | ✅ | | | |
| Tạo / chuyển trạng thái chu kỳ | ✅ | | | |
| Quản lý template KPI | ✅ | | | |
| Tạo KPI cấp công ty | | ✅ | | |
| Giao KPI cho phòng ban | | ✅ | | |
| Giao KPI cho nhân viên | | | ✅ | |
| Duyệt / từ chối KPI đề xuất | | | ✅ | |
| Theo dõi tiến độ phòng/NV | | ✅ | ✅ | |
| Đánh giá cuối kỳ (manager) | | | ✅ | |
| Chốt điểm, xếp loại | | | ✅ | |
| Xem KPI được giao | | | | ✅ |
| Đề xuất KPI cá nhân | | | | ✅ |
| Cập nhật tiến độ + minh chứng | | | | ✅ |
| Tự đánh giá cuối kỳ | | | | ✅ |
| Xem báo cáo tổng hợp | ✅ | ✅ | ✅ (phòng mình) | ✅ (của mình) |
