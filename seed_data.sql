-- SQL Seed Data for KPI Management Database
-- Database name: kpi_management
-- Password for all created accounts: 123456

-- 1. Insert Roles
INSERT INTO roles (id, code, name, description) VALUES
(1, 'ADMIN', 'Quản trị hệ thống', 'Toàn quyền cấu hình danh mục và hệ thống'),
(2, 'DIRECTOR', 'Giám đốc', 'Quản lý, phê duyệt KPI cấp công ty và phòng ban'),
(3, 'MANAGER', 'Trưởng phòng', 'Quản lý KPI phòng ban và nhân viên cấp dưới'),
(4, 'EMPLOYEE', 'Nhân viên', 'Thực hiện và báo cáo kết quả KPI cá nhân')
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description);

-- 2. Insert Positions
INSERT INTO positions (id, position_code, title, level, is_deleted, created_at, updated_at) VALUES
(1, 'DIR', 'Giám đốc', 5, false, NOW(), NOW()),
(2, 'MGR', 'Trưởng phòng', 3, false, NOW(), NOW()),
(3, 'EMP', 'Nhân viên', 1, false, NOW(), NOW())
ON DUPLICATE KEY UPDATE title=VALUES(title), level=VALUES(level);

-- 3. Insert Departments
INSERT INTO departments (id, department_code, name, parent_id, manager_id, is_deleted, created_at, updated_at) VALUES
(1, 'HQ', 'Ban Giám Đốc', NULL, NULL, false, NOW(), NOW()),
(2, 'IT', 'Phòng Công nghệ thông tin', 1, NULL, false, NOW(), NOW()),
(3, 'MKT', 'Phòng Marketing', 1, NULL, false, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 4. Insert Employees
INSERT INTO employees (id, employee_code, full_name, email, phone_number, department_id, position_id, is_deleted, created_at, updated_at) VALUES
(1, 'EMP001', 'Nguyễn Văn Giám Đốc', 'director@kpi.com', '0901234567', 1, 1, false, NOW(), NOW()),
(2, 'EMP002', 'Trần Thị Trưởng Phòng IT', 'manager_it@kpi.com', '0907654321', 2, 2, false, NOW(), NOW()),
(3, 'EMP003', 'Lê Văn Nhân Viên IT', 'emp_it@kpi.com', '0912345678', 2, 3, false, NOW(), NOW()),
(4, 'EMP004', 'Phạm Thị Trưởng Phòng MKT', 'manager_mkt@kpi.com', '0922334455', 3, 2, false, NOW(), NOW()),
(5, 'EMP005', 'Hoàng Văn Nhân Viên MKT', 'emp_mkt@kpi.com', '0933445566', 3, 3, false, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), email=VALUES(email);

-- Update department manager associations
UPDATE departments SET manager_id = 1 WHERE id = 1;
UPDATE departments SET manager_id = 2 WHERE id = 2;
UPDATE departments SET manager_id = 4 WHERE id = 3;

-- 5. Insert Accounts
-- Default password for all: 123456 (hashed with BCrypt)
INSERT INTO accounts (id, username, password_hash, status, provider, employee_id, is_deleted, created_at, updated_at) VALUES
(1, 'director', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 1, false, NOW(), NOW()),
(2, 'manager_it', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 2, false, NOW(), NOW()),
(3, 'emp_it', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 3, false, NOW(), NOW()),
(4, 'manager_mkt', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 4, false, NOW(), NOW()),
(5, 'emp_mkt', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 5, false, NOW(), NOW())
ON DUPLICATE KEY UPDATE password_hash=VALUES(password_hash), status=VALUES(status);

-- 6. Map Account Roles
INSERT INTO account_roles (account_id, role_id) VALUES
(1, 1), -- director has ADMIN role
(1, 2), -- director has DIRECTOR role
(2, 3), -- manager_it has MANAGER role
(3, 4), -- emp_it has EMPLOYEE role
(4, 3), -- manager_mkt has MANAGER role
(5, 4)  -- emp_mkt has EMPLOYEE role
ON DUPLICATE KEY UPDATE account_id=account_id;

-- 7. Insert KPI Cycles
INSERT INTO kpi_cycles (id, cycle_code, name, type, start_date, end_date, status, created_by, is_deleted, created_at, updated_at) VALUES
(1, 'CYCLE_2026_Q1', 'Quý 1 năm 2026', 'QUARTERLY', '2026-01-01', '2026-03-31', 'ACTIVE', 'ADMIN', false, NOW(), NOW()),
(2, 'CYCLE_2026_Q2', 'Quý 2 năm 2026', 'QUARTERLY', '2026-04-01', '2026-06-30', 'PLANNING', 'ADMIN', false, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), status=VALUES(status);

-- 8. Insert KPI Categories
INSERT INTO kpi_categories (id, category_code, name, description, is_deleted, created_at, updated_at) VALUES
(1, 'FIN', 'Tài chính (Financial)', 'Chỉ tiêu đo lường hiệu quả tài chính, doanh thu, lợi nhuận', false, NOW(), NOW()),
(2, 'CUST', 'Khách hàng (Customer)', 'Đo lường mức độ hài lòng, phát triển thị phần và thương hiệu', false, NOW(), NOW()),
(3, 'PROC', 'Quy trình nội bộ (Internal Processes)', 'Năng suất, chất lượng và cải tiến quy trình nghiệp vụ', false, NOW(), NOW()),
(4, 'LND', 'Học hỏi & Phát triển (Learning & Growth)', 'Nâng cao năng lực nhân sự, đào tạo và phát triển kỹ năng', false, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 9. Insert KPI Templates
INSERT INTO kpi_templates (id, template_code, category_id, name, description, unit, target_type, default_weight, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'REV_COMP', 1, 'Doanh thu Công ty', 'Doanh số bán hàng toàn công ty', 'VNĐ', 'HIGHER_BETTER', 40.00, true, false, NOW(), NOW()),
(2, 'PROFIT_COMP', 1, 'Lợi nhuận ròng Công ty', 'Tổng lợi nhuận sau thuế của công ty', 'VNĐ', 'HIGHER_BETTER', 30.00, true, false, NOW(), NOW()),
(3, 'CSAT_CUST', 2, 'Chỉ số hài lòng khách hàng (CSAT)', 'Mức độ hài lòng của khách hàng khảo sát định kỳ', '%', 'HIGHER_BETTER', 15.00, true, false, NOW(), NOW()),
(4, 'SYSTEM_UPTIME', 3, 'Uptime Hệ thống IT', 'Tỉ lệ thời gian hệ thống hoạt động ổn định', '%', 'HIGHER_BETTER', 25.00, true, false, NOW(), NOW()),
(5, 'BUG_RATE', 3, 'Tỉ lệ lỗi trên Production', 'Số lượng bug nghiêm trọng phát hiện trên môi trường production', 'Lỗi', 'LOWER_BETTER', 20.00, true, false, NOW(), NOW()),
(6, 'TRAINING_HOURS', 4, 'Số giờ đào tạo bình quân', 'Số giờ tham gia đào tạo nâng cao chuyên môn của nhân viên', 'Giờ', 'HIGHER_BETTER', 10.00, true, false, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), default_weight=VALUES(default_weight);
