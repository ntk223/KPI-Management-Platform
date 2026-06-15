# Kế hoạch Phát triển — KPI Management Platform

> Sắp xếp theo độ quan trọng: module nào bị phụ thuộc nhiều nhất thì làm trước.

---

## Tổng quan 5 Giai đoạn

```
PHASE 1          PHASE 2          PHASE 3          PHASE 4          PHASE 5
────────         ────────         ────────         ────────         ────────
Nền tảng    →   Chu kỳ &    →   KPI Core     →   Tracking &   →   Nâng cao &
IAM             Template         (Document +       Evaluation       Hoàn thiện
                                  Item)
```

---

## PHASE 1 — Nền tảng IAM 🔴 (Ưu tiên cao nhất)

> **Lý do:** Tất cả các module khác đều phụ thuộc vào Employee, Account, Role.
> Không có IAM thì không có authentication, không phân quyền được.

### Thứ tự implement trong phase:

```
1.1  Position   →  1.2  Department  →  1.3  Employee
                                            ↓
                               1.4  Account + Role + AccountRole
                                            ↓
                                   1.5  Auth (Login + JWT + Roles)
```

### Chi tiết từng module:

#### 1.1 Position (Chức vụ)
- [ ] `PositionRequest` DTO (positionCode, title, level)
- [ ] `PositionResponse` DTO
- [ ] Implement `PositionService`: getAll, getById, create, update, delete
- [ ] Test API: `GET /positions`, `POST /positions`

#### 1.2 Department (Phòng ban)
- [ ] `DepartmentRequest` DTO (departmentCode, name, parentId)
- [ ] `DepartmentResponse` DTO (kèm tên phòng cha)
- [ ] Implement `DepartmentService`: CRUD + hỗ trợ cây cha-con
- [ ] Logic: khi xoá phòng ban, kiểm tra còn nhân viên không
- [ ] Test API: `GET /departments`, `POST /departments`

#### 1.3 Employee (Nhân viên)
- [ ] `EmployeeRequest` DTO (employeeCode, fullName, email, departmentId, positionId)
- [ ] `EmployeeResponse` DTO (kèm tên phòng ban, chức vụ)
- [ ] Implement `EmployeeService`: CRUD
- [ ] Test API: `GET /employees`, `POST /employees`

#### 1.4 Account + Role + AccountRole
- [ ] `CreateAccountRequest` DTO (username, password, employeeId, roleCodes[])
- [ ] Implement tạo tài khoản + gán role
- [ ] Đảm bảo seed data: 4 role cố định (ADMIN, DIRECTOR, MANAGER, EMPLOYEE)
- [ ] Test: tạo tài khoản, gán 2 role cùng lúc

#### 1.5 Auth — Hoàn thiện
- [ ] Sửa `AuthService.login()`: trả về danh sách roles trong response
- [ ] Cập nhật `LoginInfoDTO` thêm field `roles: List<String>`
- [ ] Cập nhật `JwtTokenProvider`: đưa roles vào JWT claims
- [ ] Test: login → nhận token → gọi API protected

**✅ Điều kiện hoàn thành Phase 1:**
- Tạo được nhân viên, tài khoản, gán role
- Login thành công, token có chứa role
- `CustomUserDetailsService` load đúng authorities từ DB

---

## PHASE 2 — Chu kỳ & Template 🟠 (Phụ thuộc Phase 1)

> **Lý do:** KpiDocument cần `cycle_id`. KpiItem cần `template_id`.
> Phải có Template trước khi Manager tạo KPI cho nhân viên.

### Thứ tự implement:

```
2.1  KpiCycle  →  2.2  KpiCategory  →  2.3  KpiTemplate
```

#### 2.1 KpiCycle (Chu kỳ đánh giá)
- [ ] `KpiCycleRequest` DTO (cycleCode, name, type, startDate, endDate)
- [ ] `KpiCycleResponse` DTO
- [ ] `ChangeStatusRequest` DTO (newStatus)
- [ ] Implement `KpiCycleService`:
  - `create()`: validate ngày hợp lệ, mã không trùng
  - `changeStatus()`: validate đúng luồng PLANNING→ACTIVE→EVALUATING→CLOSED
- [ ] Test: tạo chu kỳ → kích hoạt → kiểm tra không cho quay lui

#### 2.2 KpiCategory (Danh mục tiêu chí)
- [ ] `KpiCategoryRequest` DTO (categoryCode, name, description)
- [ ] `KpiCategoryResponse` DTO
- [ ] Implement `KpiTemplateService.createCategory()`, `updateCategory()`, `deleteCategory()`
- [ ] Logic: không xoá category nếu còn template đang dùng

#### 2.3 KpiTemplate (Tiêu chí mẫu)
- [ ] `KpiTemplateRequest` DTO (templateCode, categoryId, name, unit, targetType, defaultWeight)
- [ ] `KpiTemplateResponse` DTO
- [ ] Implement `KpiTemplateService`: CRUD + `toggleActive()`
- [ ] Test: tạo template → lấy theo category → bật/tắt active

**✅ Điều kiện hoàn thành Phase 2:**
- Tạo được chu kỳ, kích hoạt thành công
- Tạo được category và template
- Chuyển trạng thái cycle đúng luồng, không cho quay lui

---

## PHASE 3 — KPI Core: Document + Item 🟡 (Phụ thuộc Phase 1 + 2)

> **Lý do:** Đây là trái tim nghiệp vụ. Document + Item là nơi KPI được định nghĩa và cascade.

### Thứ tự implement:

```
3.1  KpiDocument (CRUD + cascade + submit/approve/reject)
        ↓
3.2  KpiItem (thêm tiêu chí vào phiếu)
        ↓
3.3  Security: phân quyền theo role cho từng endpoint
```

#### 3.1 KpiDocument
- [ ] `CreateDocumentRequest` DTO (cycleId, targetType, targetId, parentDocId, sourceType)
- [ ] `DocumentResponse` DTO (kèm tên target: tên phòng / tên nhân viên)
- [ ] Implement `KpiDocumentService`:
  - `create()`: validate cycle ACTIVE, validate polymorphic target, check unique
  - `submit()`: DRAFT → PENDING_APPROVAL
  - `approve()`: PENDING_APPROVAL → IN_PROGRESS
  - `reject()`: PENDING_APPROVAL → REJECTED
  - `getPendingApprovals()`: lấy phiếu chờ duyệt thuộc phòng manager
- [ ] Test cascade: tạo COMPANY → DEPARTMENT → EMPLOYEE, kiểm tra parent_doc_id

#### 3.2 KpiItem
- [ ] `CreateItemRequest` DTO (documentId, templateId?, name, unit, targetType, weight, targetValue)
- [ ] `KpiItemResponse` DTO
- [ ] Implement `KpiItemService`:
  - `create()`: validate document DRAFT, validate tổng weight ≤ 100
  - `update()`: chỉ khi document DRAFT
  - `cancel()`: chuyển status = CANCELLED
- [ ] Validate: tổng weight của items trong 1 document = 100 khi submit

#### 3.3 Phân quyền (Security)
- [ ] Cập nhật `SecurityConfig`: bổ sung rule theo role
  ```
  ADMIN     → /positions/**, /departments/**, /kpi-cycles/**, /kpi-templates/**
  DIRECTOR  → /kpi-documents/** (target=COMPANY, DEPARTMENT)
  MANAGER   → /kpi-documents/** (target=EMPLOYEE), /kpi-documents/*/approve
  EMPLOYEE  → /kpi-documents/my, /kpi-documents/*/submit
  ```
- [ ] Validate trong Service: MANAGER chỉ thao tác được NV thuộc phòng mình

**✅ Điều kiện hoàn thành Phase 3:**
- DIRECTOR tạo KPI công ty và giao cho phòng ban (cascade đúng)
- MANAGER tạo KPI cho nhân viên, NV đề xuất được duyệt/từ chối
- Tổng weight items = 100 mới cho submit
- Phân quyền hoạt động đúng theo role

---

## PHASE 4 — Tracking & Evaluation 🟢 (Phụ thuộc Phase 3)

> **Lý do:** Chỉ implement được sau khi có document và item.
> Đây là phần nhân viên tương tác hàng ngày.

### Thứ tự implement:

```
4.1  KpiTracking (cập nhật tiến độ + minh chứng)
        ↓
4.2  KpiEvaluation — Self (nhân viên tự chấm)
        ↓
4.3  KpiEvaluation — Manager (manager chấm + chốt điểm)
```

#### 4.1 KpiTracking (Cập nhật tiến độ)
- [ ] `AddProgressRequest` DTO (kpiItemId, valueDelta, notes)
- [ ] `TrackingLogResponse` DTO (valueBefore, valueDelta, valueAfter, notes, attachments)
- [ ] Implement `KpiTrackingService.addProgress()`:
  - Validate cycle ACTIVE + document IN_PROGRESS
  - Tính value_before/after, cập nhật kpi_item.current_value
  - Lưu tracking_log
- [ ] Upload minh chứng: `POST /kpi-tracking/{logId}/attachments`
  - Nhận file multipart, lưu vào storage, ghi `kpi_attachments`
- [ ] Test: cập nhật tiến độ → xem lịch sử → % hoàn thành tính đúng

#### 4.2 KpiEvaluation — Self
- [ ] `SelfEvalRequest` DTO (selfScore, selfComment)
- [ ] Implement `selfEvaluateItem()`, `completeSelfEvaluation()`
- [ ] Validate: chỉ khi cycle EVALUATING + document IN_PROGRESS
- [ ] Test: tự chấm từng item → gọi complete → status = SELF_EVALUATED

#### 4.3 KpiEvaluation — Manager
- [ ] `ManagerEvalRequest` DTO (managerScore, managerComment)
- [ ] Implement `managerEvaluateItem()`:
  - Cảnh báo nếu |self_score - manager_score| > 20
  - Set final_score = manager_score
- [ ] Implement `completeManagerEvaluation()`:
  - Tính `total_final_score = Σ(final_score × weight / 100)`
  - Xác định `rating` theo thang điểm
  - Lưu `KpiEvaluation`
  - Chuyển document status → MANAGER_EVALUATED
- [ ] Test: manager chấm → chốt điểm → rating đúng

**✅ Điều kiện hoàn thành Phase 4:**
- NV cập nhật tiến độ, đính kèm file thành công
- Self-evaluation và Manager-evaluation hoạt động đúng luồng
- Điểm tổng kết tính đúng công thức có trọng số
- Xếp loại rating đúng thang điểm

---

## PHASE 5 — Nâng cao & Hoàn thiện 🔵 (Optional)

> Tính năng bổ sung, tăng trải nghiệm người dùng.

#### 5.1 Dashboard & Báo cáo
- [ ] `GET /dashboard/company?cycleId=` — Tổng quan toàn công ty (DIRECTOR)
- [ ] `GET /dashboard/department/{deptId}?cycleId=` — Tiến độ phòng ban (MANAGER)
- [ ] `GET /dashboard/employee/{empId}?cycleId=` — Phiếu KPI cá nhân (EMPLOYEE)

#### 5.2 Báo cáo so sánh
- [ ] So sánh % hoàn thành giữa các phòng ban trong cùng chu kỳ
- [ ] Lịch sử xếp loại qua nhiều chu kỳ của 1 nhân viên

#### 5.3 Tìm kiếm & Phân trang
- [ ] Thêm `Pageable` vào các API danh sách
- [ ] Filter nhân viên theo phòng, chức vụ, trạng thái

#### 5.4 Thông báo (Notification)
- [ ] Nhắc nhở cập nhật tiến độ khi sắp đến hạn
- [ ] Thông báo khi phiếu KPI được duyệt / từ chối

#### 5.5 Audit Log
- [ ] Bảng `audit_logs`: ghi lại mọi thay đổi trạng thái phiếu KPI
- [ ] Ai thay đổi, lúc nào, từ trạng thái gì sang gì

---

## Tóm tắt Roadmap

| Phase | Module | Độ phụ thuộc | Thời gian ước tính |
|-------|--------|-------------|-------------------|
| **1** | IAM (Position, Department, Employee, Account, Auth) | Không phụ thuộc | ~3-4 ngày |
| **2** | KpiCycle, KpiCategory, KpiTemplate | Phụ thuộc Phase 1 | ~2-3 ngày |
| **3** | KpiDocument, KpiItem, Security | Phụ thuộc Phase 1+2 | ~4-5 ngày |
| **4** | KpiTracking, KpiEvaluation | Phụ thuộc Phase 3 | ~3-4 ngày |
| **5** | Dashboard, Báo cáo, Notification | Phụ thuộc Phase 1-4 | ~3-5 ngày |

> **Tổng:** ~15-21 ngày nếu làm full-time, tập trung từng phase.

---

## Quy tắc implement cho mỗi task

Với mỗi module, luôn đi theo thứ tự:

```
1. DTO (Request + Response)
2. Service (logic + validate)
3. Controller (route + response wrapper)
4. Test API thủ công (Postman/curl)
5. Review: đúng error code, đúng message tiếng Việt, không lộ stack trace
```
