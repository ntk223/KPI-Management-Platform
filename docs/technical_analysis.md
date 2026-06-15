# Phân tích Kỹ thuật — KPI Management Platform

---

## 1. Stack công nghệ hiện tại

### 1.1 Backend Core

| Thành phần | Công nghệ | Phiên bản | Vai trò |
|------------|-----------|-----------|---------|
| **Ngôn ngữ** | Java | 17 (LTS) | Ngôn ngữ nền tảng |
| **Framework** | Spring Boot | 4.0.6 | Web MVC, DI, Auto-config |
| **Web layer** | Spring Web MVC | (bundled) | REST API, servlet-based |
| **ORM / Data** | Spring Data JPA + Hibernate | (bundled) | Mapping entity ↔ DB |
| **Database** | MySQL 8.0 | 8.0 | RDBMS chính |
| **JDBC Driver** | MySQL Connector/J + MariaDB JDBC | latest | Kết nối DB |
| **Security** | Spring Security | (bundled) | Authentication & Authorization |
| **Auth token** | JJWT (io.jsonwebtoken) | 0.11.5 | Phát hành & xác thực JWT |
| **Code gen** | Lombok | latest | Giảm boilerplate (getter/setter/builder) |
| **Object mapping** | MapStruct | 1.6.3 | Entity ↔ DTO mapping |
| **Build tool** | Maven | 3.x | Dependency management, build lifecycle |
| **Container** | Docker + Docker Compose | — | Containerize app + DB |

### 1.2 Kiến trúc package (đã triển khai)

```
vdt.kpimanagement/
├── controller/      ← Nhận request, trả response, delegate xuống Service
├── service/         ← Business logic, orchestration
├── repository/      ← JPA Repository, query DB
├── entity/          ← JPA Entity, ánh xạ bảng DB
├── dto/             ← Request/Response objects
├── mapper/          ← MapStruct: Entity ↔ DTO
├── exception/       ← Custom exception, global error handler
├── config/          ← SecurityConfig, JwtConfig, ...
├── constant/        ← Enum, hằng số dùng chung
└── common/          ← Wrapper response, utility chung
```

> **Nhận xét:** Package structure rõ ràng, theo đúng mô hình Layered Architecture chuẩn Spring.

---

## 2. Kiến trúc hệ thống

### 2.1 Layered Architecture (Controller → Service → Repository)

```
┌─────────────────────────────────────────────────────┐
│                    Client / Frontend                │
│              (Web App / Mobile / HRM System)        │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP REST (JSON)
                       ▼
┌─────────────────────────────────────────────────────┐
│              Spring Security Filter Chain           │
│         JWT Authentication Filter → Role Check     │
└──────────────────────┬──────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────┐
│                  Controller Layer                   │
│   @RestController — Request validation, routing    │
└──────────────────────┬──────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────┐
│                   Service Layer                     │
│   Business logic, KPI cascade, scoring, workflow   │
│   @Transactional cho các luồng multi-table          │
└──────────────────────┬──────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────┐
│                 Repository Layer                    │
│   Spring Data JPA + Native Query (MySQL 8)         │
└──────────────────────┬──────────────────────────────┘
                       │ JDBC
                       ▼
┌─────────────────────────────────────────────────────┐
│              MySQL 8.0 (Docker Container)           │
│         Persistent volume: mysql_prod_data          │
└─────────────────────────────────────────────────────┘
```

### 2.2 Luồng xác thực (Authentication Flow)

```
Client → POST /auth/login (username + password)
       ↓
Spring Security → UserDetailsService → DB lookup
       ↓
Xác thực thành công → JJWT tạo Access Token (JWT)
       ↓
Client lưu token → gửi kèm Authorization: Bearer <token>
       ↓
JwtAuthenticationFilter → parse token → set SecurityContext
       ↓
@PreAuthorize / hasRole("MANAGER") → kiểm tra quyền
```

### 2.3 Mô hình phân quyền

| Cơ chế | Cách dùng |
|--------|-----------|
| **Role-Based (RBAC)** | 4 role: ADMIN, DIRECTOR, MANAGER, EMPLOYEE |
| **Data-level security** | Service layer kiểm tra `departmentId`, `userId` trước khi trả dữ liệu |
| **Stateless JWT** | Không dùng session, phù hợp scale ngang |

---

## 3. Thiết kế DB nổi bật

### 3.1 Mô hình Polymorphic cho KPI Document

Bảng `kpi_documents` dùng cột `target_type` (COMPANY / DEPARTMENT / EMPLOYEE) và `target_id` để ánh xạ đến nhiều loại đối tượng — thay vì tạo 3 bảng riêng. Đây là **Single-Table Polymorphism**, giúp:
- Query thống nhất qua 1 bảng
- Dễ báo cáo cross-level

### 3.2 Cây phòng ban đệ quy

Bảng `departments` dùng `parent_id` tự tham chiếu — **Adjacency List Pattern**. Phù hợp cho cấu trúc phòng ban ≤ 4–5 cấp. Với cây sâu hơn trong tương lai có thể nâng lên **Closure Table** hoặc **Materialized Path**.

### 3.3 Immutable tracking log

`kpi_tracking_log` chỉ INSERT, không UPDATE/DELETE — đây là pattern **Append-Only Ledger**, đảm bảo audit trail đầy đủ.

---

## 4. Khả năng mở rộng (Scalability)

### 4.1 Scale theo chiều dọc (Vertical Scaling)

| Thành phần | Khả năng |
|------------|----------|
| JVM Heap | Tăng `-Xmx` trong Dockerfile |
| DB | Tăng RAM/CPU cho MySQL container |
| Connection Pool | HikariCP (mặc định Spring Boot), tunable qua `application.yml` |

### 4.2 Scale theo chiều ngang (Horizontal Scaling)

| Yêu cầu | Trạng thái hiện tại | Cần bổ sung |
|---------|---------------------|-------------|
| Stateless API | ✅ JWT stateless | — |
| Multi-instance app | ✅ Có thể chạy nhiều instance | Load balancer (Nginx/Traefik) |
| Shared session | ✅ Không cần (JWT) | — |
| DB read replica | ❌ Chưa có | MySQL Read Replica hoặc PlanarDB |
| Distributed cache | ❌ Chưa có | Redis (dashboard, token blacklist) |
| File storage | ❌ Chưa rõ (kpi_attachments) | MinIO / S3-compatible |

### 4.3 Điểm có thể trở thành bottleneck

| Điểm | Lý do | Hướng xử lý |
|------|-------|-------------|
| Dashboard tổng hợp (DIRECTOR/ADMIN) | Query JOIN nhiều bảng, tính % realtime | Cache Redis 5–10 phút, hoặc materialized view |
| KPI scoring cuối kỳ | Tính điểm cho hàng nghìn nhân viên cùng lúc | Async processing (@Async hoặc queue) |
| Upload minh chứng | File lớn, lưu trong DB hay filesystem | Tách ra object storage (MinIO/S3) |
| `kpi_tracking_log` | Insert liên tục, to theo thời gian | Partition by cycle_id hoặc archive sau khi cycle CLOSED |

---

## 5. Khả năng tích hợp với HRM

### 5.1 Các điểm tích hợp tự nhiên

Dữ liệu mà HRM thường quản lý và KPI Platform cần dùng:

| Thực thể | HRM quản lý | KPI cần dùng |
|----------|-------------|--------------|
| Nhân viên (Employee) | ✅ | Giao KPI, tracking, đánh giá |
| Phòng ban (Department) | ✅ | Phân cấp KPI |
| Chức vụ (Position) | ✅ | Xác định role, template KPI phù hợp |
| Chu kỳ nhân sự (review period) | ✅ | Đồng bộ với kpi_cycles |
| Kết quả đánh giá (final score, rating) | ❌ (KPI tạo ra) | Đẩy ngược sang HRM để tính lương/thưởng |

### 5.2 Phương án tích hợp (theo mức độ phức tạp)

#### Phương án A — REST API Integration (Đơn giản, ưu tiên trước)

```
HRM System ◄──── REST API ────► KPI Platform
```

- HRM expose API: `/api/employees`, `/api/departments`, `/api/positions`
- KPI Platform gọi định kỳ hoặc on-demand để sync
- KPI Platform expose API: `/api/kpi-results/{cycleId}` cho HRM pull về tính lương

**Ưu điểm:** Dễ implement, không cần thay đổi DB schema  
**Nhược điểm:** Có độ trễ, cần xử lý retry khi HRM không sẵn sàng

#### Phương án B — Shared Database / Schema (Nguy hiểm, không khuyến nghị)

Dùng chung DB với HRM → coupling cao, khó maintain.

#### Phương án C — Event-Driven / Message Queue (Scale tốt hơn)

```
HRM System ──► Kafka/RabbitMQ ──► KPI Platform
                                  ↑
                          Consume event:
                          - employee.created
                          - employee.updated
                          - department.changed
```

KPI Platform đăng ký nhận event từ HRM, cập nhật local copy.  
**Ưu điểm:** Loose coupling, resilient, scale tốt  
**Nhược điểm:** Phức tạp hơn, cần thêm Kafka/RabbitMQ

#### Phương án D — OAuth2 / SSO (Cho phần Authentication)

```
HRM có sẵn user directory (Active Directory / Keycloak)
        │
        ▼ OAuth2 / OIDC
KPI Platform nhận token từ HRM → không cần tạo user riêng
```

Thay vì tự quản lý user, KPI Platform trust token từ HRM/IdP.

### 5.3 Lộ trình tích hợp đề xuất

```
Giai đoạn 1 (MVP):
  → KPI tự quản lý user, phòng ban (standalone)
  → HRM kết nối qua REST API để pull kết quả đánh giá

Giai đoạn 2 (Tích hợp nhẹ):
  → Đồng bộ one-way: HRM → KPI (employee, department)
  → API key / service account authentication
  → Webhook hoặc scheduled sync

Giai đoạn 3 (Tích hợp sâu):
  → SSO qua OAuth2/OIDC (Keycloak hoặc HRM làm IdP)
  → Event-driven sync qua message queue
  → Kết quả KPI tự động push sang HRM để tính lương/thưởng
```

---

## 6. Điểm mạnh kỹ thuật hiện tại

| Điểm mạnh | Lý do |
|-----------|-------|
| **Stateless JWT** | Dễ scale ngang, không cần sticky session |
| **MapStruct** | Mapping Entity ↔ DTO compile-time, hiệu năng tốt, không dùng reflection runtime |
| **Layered Architecture rõ ràng** | Dễ maintain, test từng layer độc lập |
| **Native Query hỗ trợ** | Linh hoạt với query phức tạp (join, pagination) |
| **Docker Compose** | Dễ deploy, chuẩn bị sẵn cho CI/CD |
| **Immutable tracking log** | Audit trail đầy đủ, không mất dữ liệu lịch sử |
| **Polymorphic KPI Document** | Mô hình hóa gọn gàng 3 cấp KPI trong 1 bảng |

---

## 7. Điểm cần cân nhắc / rủi ro kỹ thuật

| Rủi ro | Mức độ | Ghi chú |
|--------|--------|---------|
| Spring Boot 4.0.6 còn khá mới | Trung bình | Một số thư viện third-party có thể chưa tương thích đầy đủ |
| Chưa có caching layer | Trung bình | Dashboard query sẽ nặng khi dữ liệu lớn |
| File attachment chưa rõ storage | Trung bình | Nếu lưu trong DB (BLOB) sẽ chậm; cần tách ra object storage |
| Không có API versioning | Thấp | Khi tích hợp HRM, breaking change sẽ rủi ro; nên dùng `/api/v1/` |
| Chưa có rate limiting | Thấp | Cần bổ sung khi public hoặc tích hợp với hệ thống ngoài |
| `parent_id` adjacency list | Thấp | OK với ≤ 5 cấp; cần đánh giá lại nếu cây phòng ban rất sâu |

---

## 8. Tóm tắt

```
Công nghệ:   Java 17 + Spring Boot 4 + MySQL 8 + JWT + MapStruct
Kiến trúc:   Layered (Controller → Service → Repository), RBAC, Stateless
Mở rộng:     Stateless JWT ✅ | Cần thêm Redis cache, Read replica, Object storage
Tích hợp HRM: Giai đoạn 1 qua REST API | Giai đoạn 3 qua OAuth2 SSO + Event Queue
```
