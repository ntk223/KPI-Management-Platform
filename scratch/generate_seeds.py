# Programmatic Generator for KPI Management Rich Seed Data
# Target File: seed_data_rich.sql

sql = []

sql.append("""-- Rich seed data for kpi_management
-- Generated on 2026-06-30
-- Password for all created accounts: 123456 (BCrypt hashed)

SET FOREIGN_KEY_CHECKS = 0;

-- Truncate all tables for a clean seed
TRUNCATE TABLE account_roles;
TRUNCATE TABLE accounts;
TRUNCATE TABLE employees;
TRUNCATE TABLE departments;
TRUNCATE TABLE positions;
TRUNCATE TABLE roles;
TRUNCATE TABLE kpi_attachments;
TRUNCATE TABLE kpi_categories;
TRUNCATE TABLE kpi_cycles;
TRUNCATE TABLE kpi_document_evaluations;
TRUNCATE TABLE kpi_documents;
TRUNCATE TABLE kpi_item_evaluations;
TRUNCATE TABLE kpi_items;
TRUNCATE TABLE kpi_templates;
TRUNCATE TABLE kpi_tracking_logs;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Insert Roles
INSERT INTO roles (id, code, name, description) VALUES
(1, 'ADMIN', 'Quản trị hệ thống', 'Toàn quyền cấu hình danh mục và hệ thống'),
(2, 'DIRECTOR', 'Giám đốc', 'Quản lý, phê duyệt KPI cấp công ty và phòng ban'),
(3, 'MANAGER', 'Trưởng phòng', 'Quản lý KPI phòng ban và nhân viên cấp dưới'),
(4, 'EMPLOYEE', 'Nhân viên', 'Thực hiện và báo cáo kết quả KPI cá nhân');

-- 2. Insert Positions
INSERT INTO positions (id, position_code, title, level, is_deleted, created_at, updated_at) VALUES
(1, 'DIR', 'Giám đốc', 5, false, NOW(), NOW()),
(2, 'MGR', 'Trưởng phòng', 3, false, NOW(), NOW()),
(3, 'EMP', 'Nhân viên', 1, false, NOW(), NOW());

-- 3. Insert Departments
INSERT INTO departments (id, department_code, name, parent_id, manager_id, is_deleted, created_at, updated_at) VALUES
(1, 'HQ', 'Ban Giám Đốc', NULL, NULL, false, NOW(), NOW()),
(2, 'IT', 'Phòng Công nghệ thông tin', 1, NULL, false, NOW(), NOW()),
(3, 'MKT', 'Phòng Marketing', 1, NULL, false, NOW(), NOW());

-- 4. Insert Employees (1 to 10)
INSERT INTO employees (id, employee_code, full_name, email, phone_number, department_id, position_id, is_deleted, created_at, updated_at) VALUES
(1, 'EMP001', 'Nguyễn Văn Giám Đốc', 'director@kpi.com', '0901234567', 1, 1, false, NOW(), NOW()),
(2, 'EMP002', 'Trần Thị Trưởng Phòng IT', 'manager_it@kpi.com', '0907654321', 2, 2, false, NOW(), NOW()),
(3, 'EMP003', 'Lê Văn Nhân Viên IT', 'emp_it@kpi.com', '0912345678', 2, 3, false, NOW(), NOW()),
(4, 'EMP004', 'Phạm Thị Trưởng Phòng MKT', 'manager_mkt@kpi.com', '0922334455', 3, 2, false, NOW(), NOW()),
(5, 'EMP005', 'Hoàng Văn Nhân Viên MKT', 'emp_mkt@kpi.com', '0933445566', 3, 3, false, NOW(), NOW()),
(6, 'EMP006', 'Trần Văn Dev Phụ', 'emp_it_2@kpi.com', '0912345679', 2, 3, false, NOW(), NOW()),
(7, 'EMP007', 'Phạm Văn Code Dạo', 'emp_it_3@kpi.com', '0912345680', 2, 3, false, NOW(), NOW()),
(8, 'EMP008', 'Ngô Văn Tester', 'emp_it_4@kpi.com', '0912345681', 2, 3, false, NOW(), NOW()),
(9, 'EMP009', 'Bùi Thị Content', 'emp_mkt_2@kpi.com', '0933445567', 3, 3, false, NOW(), NOW()),
(10, 'EMP010', 'Nguyễn Thị Designer', 'emp_mkt_3@kpi.com', '0933445568', 3, 3, false, NOW(), NOW());

-- Update department manager associations
UPDATE departments SET manager_id = 1 WHERE id = 1;
UPDATE departments SET manager_id = 2 WHERE id = 2;
UPDATE departments SET manager_id = 4 WHERE id = 3;

-- 5. Insert Accounts
INSERT INTO accounts (id, username, password_hash, status, provider, employee_id, is_deleted, created_at, updated_at) VALUES
(1, 'director', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 1, false, NOW(), NOW()),
(2, 'manager_it', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 2, false, NOW(), NOW()),
(3, 'emp_it', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 3, false, NOW(), NOW()),
(4, 'manager_mkt', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 4, false, NOW(), NOW()),
(5, 'emp_mkt', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 5, false, NOW(), NOW()),
(6, 'emp_it_2', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 6, false, NOW(), NOW()),
(7, 'emp_it_3', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 7, false, NOW(), NOW()),
(8, 'emp_it_4', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 8, false, NOW(), NOW()),
(9, 'emp_mkt_2', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 9, false, NOW(), NOW()),
(10, 'emp_mkt_3', '$2a$10$tZ9sD3rYI.s7fU1r06JvOe2K.eZkYm1n7VpC3d3lT422c5zT8BKe6', 'ACTIVE', 'LOCAL', 10, false, NOW(), NOW());

-- 6. Map Account Roles
INSERT INTO account_roles (account_id, role_id) VALUES
(1, 1), -- director has ADMIN role
(1, 2), -- director has DIRECTOR role
(2, 3), -- manager_it has MANAGER role
(3, 4), -- emp_it has EMPLOYEE role
(4, 3), -- manager_mkt has MANAGER role
(5, 4), -- emp_mkt has EMPLOYEE role
(6, 4),
(7, 4),
(8, 4),
(9, 4),
(10, 4);

-- 7. Insert KPI Cycles
INSERT INTO kpi_cycles (id, cycle_code, name, type, start_date, end_date, status, created_by, is_deleted, created_at, updated_at) VALUES
(1, 'CYCLE_2026_Q1', 'Quý 1 năm 2026', 'QUARTERLY', '2026-01-01', '2026-03-31', 'CLOSED', 'ADMIN', false, NOW(), NOW()),
(2, 'CYCLE_2026_Q2', 'Quý 2 năm 2026', 'QUARTERLY', '2026-04-01', '2026-06-30', 'ACTIVE', 'ADMIN', false, NOW(), NOW()),
(3, 'CYCLE_2026_Q3', 'Quý 3 năm 2026', 'QUARTERLY', '2026-07-01', '2026-09-30', 'PLANNING', 'ADMIN', false, NOW(), NOW());

-- 8. Insert KPI Categories
INSERT INTO kpi_categories (id, category_code, name, description, is_deleted, created_at, updated_at) VALUES
(1, 'FIN', 'Tài chính (Financial)', 'Chỉ tiêu đo lường hiệu quả tài chính, doanh thu, lợi nhuận', false, NOW(), NOW()),
(2, 'CUST', 'Khách hàng (Customer)', 'Đo lường mức độ hài lòng, phát triển thị phần và thương hiệu', false, NOW(), NOW()),
(3, 'PROC', 'Quy trình nội bộ (Internal Processes)', 'Năng suất, chất lượng và cải tiến quy trình nghiệp vụ', false, NOW(), NOW()),
(4, 'LND', 'Học hỏi & Phát triển (Learning & Growth)', 'Nâng cao năng lực nhân sự, đào tạo và phát triển kỹ năng', false, NOW(), NOW());

-- 9. Insert KPI Templates with item_type
INSERT INTO kpi_templates (id, template_code, category_id, name, description, unit, target_type, item_type, default_weight, is_active, is_deleted, created_at, updated_at) VALUES
(1, 'REV_COMP', 1, 'Doanh thu Công ty', 'Doanh số bán hàng toàn công ty', 'VNĐ', 'HIGHER_BETTER', 'NUMERIC', 40.00, true, false, NOW(), NOW()),
(2, 'PROFIT_COMP', 1, 'Lợi nhuận ròng Công ty', 'Tổng lợi nhuận sau thuế của công ty', 'VNĐ', 'HIGHER_BETTER', 'NUMERIC', 30.00, true, false, NOW(), NOW()),
(3, 'CSAT_CUST', 2, 'Chỉ số hài lòng khách hàng (CSAT)', 'Mức độ hài lòng của khách hàng khảo sát định kỳ', '%', 'HIGHER_BETTER', 'PERCENTAGE', 15.00, true, false, NOW(), NOW()),
(4, 'SYSTEM_UPTIME', 3, 'Uptime Hệ thống IT', 'Tỉ lệ thời gian hệ thống hoạt động ổn định', '%', 'HIGHER_BETTER', 'PERCENTAGE', 25.00, true, false, NOW(), NOW()),
(5, 'BUG_RATE', 3, 'Tỉ lệ lỗi trên Production', 'Số lượng bug nghiêm trọng phát hiện trên môi trường production', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 20.00, true, false, NOW(), NOW()),
(6, 'TRAINING_HOURS', 4, 'Số giờ đào tạo bình quân', 'Số giờ tham gia đào tạo nâng cao chuyên môn của nhân viên', 'Giờ', 'HIGHER_BETTER', 'NUMERIC', 10.00, true, false, NOW(), NOW()),
(7, 'MQL_LEADS', 2, 'Số lượng MQL Leads', 'Marketing Qualified Leads mang về', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 30.00, true, false, NOW(), NOW()),
(8, 'STORY_POINTS', 3, 'Story Points Hoàn Thành', 'Tổng Story Points dev hoàn thành trong sprint', 'Points', 'HIGHER_BETTER', 'NUMERIC', 20.00, true, false, NOW(), NOW()),
(9, 'ABSENT_RATE', 4, 'Tỷ lệ đi làm muộn/nghỉ', 'Tỷ lệ đi làm muộn của phòng ban', '%', 'LOWER_BETTER', 'PERCENTAGE', 10.00, true, false, NOW(), NOW());
""")

# Setup programmatic counters
doc_id = 100
item_id = 1000

def create_doc(cycle_id, target_type, target_id, status, source_type, created_by, approved_by=None, total_progress=0.0):
    global doc_id
    doc_id += 1
    doc_code = f"KPI-Q{cycle_id}-2026-{doc_id:04d}"
    
    app_by_val = f"'{approved_by}'" if approved_by else "NULL"
    app_at_val = "NOW()" if approved_by else "NULL"
    sub_at_val = "NOW()" if status in ['PENDING_APPROVAL', 'APPROVED', 'CLOSED'] else "NULL"
    closed_at_val = "NOW()" if status == 'CLOSED' else "NULL"
    
    sql.append(f"""
INSERT INTO kpi_documents (id, document_code, cycle_id, target_type, target_id, parent_doc_id, source_type, created_by, approved_by, status, submitted_at, approved_at, closed_at, total_progress, is_deleted, created_at, updated_at) VALUES
({doc_id}, '{doc_code}', {cycle_id}, '{target_type}', {target_id}, NULL, '{source_type}', '{created_by}', {app_by_val}, '{status}', {sub_at_val}, {app_at_val}, {closed_at_val}, {total_progress}, false, NOW(), NOW());""")
    return doc_id

def create_item(doc_id, template_id, parent_id, name, desc, unit, target_type, item_type, parent_weight, document_weight, target_val, current_val, progress):
    global item_id
    item_id += 1
    parent_id_val = str(parent_id) if parent_id else "NULL"
    template_id_val = str(template_id) if template_id else "NULL"
    
    sql.append(f"""
INSERT INTO kpi_items (id, document_id, template_id, parent_id, name, description, unit, target_type, item_type, parent_weight, document_weight, target_value, current_value, progress, status, is_deleted, created_at, updated_at) VALUES
({item_id}, {doc_id}, {template_id_val}, {parent_id_val}, '{name}', '{desc}', '{unit}', '{target_type}', '{item_type}', {parent_weight}, {document_weight}, {target_val}, {current_val}, {progress}, 'ACTIVE', false, NOW(), NOW());""")
    return item_id

def create_item_eval(item_id, self_score, self_comment, manager_score, manager_comment, final_score):
    sql.append(f"""
INSERT INTO kpi_item_evaluations (kpi_item_id, self_score, self_comment, self_eval_at, manager_score, manager_comment, manager_eval_at, final_score, is_deleted, created_at, updated_at) VALUES
({item_id}, {self_score}, '{self_comment}', NOW(), {manager_score}, '{manager_comment}', NOW(), {final_score}, false, NOW(), NOW());""")

def create_doc_eval(doc_id, total_self, total_mgr, total_final, rating, evaluated_by, note):
    sql.append(f"""
INSERT INTO kpi_document_evaluations (document_id, total_self_score, total_manager_score, total_final_score, rating, evaluated_by, evaluated_at, note, is_deleted, created_at, updated_at) VALUES
({doc_id}, {total_self}, {total_mgr}, {total_final}, '{rating}', '{evaluated_by}', NOW(), '{note}', false, NOW(), NOW());""")


# ==========================================
# CYCLE 1 (Q1/2026 - CLOSED)
# ==========================================

# 1. Company Doc (Target: COMPANY=1)
comp_q1 = create_doc(1, 'COMPANY', 1, 'CLOSED', 'ASSIGNED', 'director', 'director', 92.5)
c_rev_q1 = create_item(comp_q1, 1, None, 'Doanh thu Công ty Q1', 'Doanh số toàn công ty Q1', 'VNĐ', 'HIGHER_BETTER', 'NUMERIC', 0, 50.0, 10000000000.0, 11000000000.0, 100.0)
c_prof_q1 = create_item(comp_q1, 2, None, 'Lợi nhuận ròng Q1', 'Lợi nhuận sau thuế Q1', 'VNĐ', 'HIGHER_BETTER', 'NUMERIC', 0, 50.0, 2000000000.0, 1700000000.0, 85.0)

# 2. IT Dept Doc (Target: DEPARTMENT=2)
it_dept_q1 = create_doc(1, 'DEPARTMENT', 2, 'CLOSED', 'ASSIGNED', 'director', 'director', 88.0)
it_uptime_q1 = create_item(it_dept_q1, 4, None, 'Uptime Hệ thống IT Q1', 'Duy trì hoạt động dịch vụ', '%', 'HIGHER_BETTER', 'PERCENTAGE', 0, 60.0, 99.9, 99.5, 99.5)
it_bug_q1 = create_item(it_dept_q1, 5, None, 'Tỉ lệ lỗi Q1', 'Hạn chế bug nghiêm trọng', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 0, 40.0, 5, 2, 100.0)

# 3. IT Manager Doc (Target: EMPLOYEE=2)
mgr_it_q1 = create_doc(1, 'EMPLOYEE', 2, 'CLOSED', 'ASSIGNED', 'manager_it', 'director', 89.0)
mit_item1 = create_item(mgr_it_q1, 4, None, 'Uptime Hệ thống hạ tầng', 'Quản trị hệ thống IT hạ tầng', '%', 'HIGHER_BETTER', 'PERCENTAGE', 0, 50.0, 99.9, 99.9, 100.0)
mit_item2 = create_item(mgr_it_q1, 6, None, 'Số giờ đào tạo nội bộ IT', 'Đào tạo kỹ năng lập trình', 'Giờ', 'HIGHER_BETTER', 'NUMERIC', 0, 50.0, 20, 18, 90.0)
create_item_eval(mit_item1, 100.0, 'Đạt tuyệt đối', 100.0, 'Hoàn thành tốt', 100.0)
create_item_eval(mit_item2, 90.0, 'Thiếu 2 giờ', 90.0, 'Cần cố gắng', 90.0)
create_doc_eval(mgr_it_q1, 95.0, 95.0, 95.0, 'EXCELLENT', 'director', 'Hoàn thành xuất sắc nhiệm vụ')

# 4. IT Staff Doc (Target: EMPLOYEE=3)
emp_it_q1 = create_doc(1, 'EMPLOYEE', 3, 'CLOSED', 'PROPOSED', 'emp_it', 'manager_it', 85.0)
eit_item1 = create_item(emp_it_q1, 8, None, 'Story Points Hoàn Thành Q1', 'Hoàn thành task trong Sprint', 'Points', 'HIGHER_BETTER', 'NUMERIC', 0, 70.0, 100, 85, 85.0)
eit_item2 = create_item(emp_it_q1, 5, None, 'Khắc phục bug hotfix', 'Hạn chế lỗi nghiêm trọng', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 0, 30.0, 2, 2, 100.0)
create_item_eval(eit_item1, 85.0, 'Nỗ lực hoàn thành', 85.0, 'Đồng ý', 85.0)
create_item_eval(eit_item2, 100.0, 'Không phát sinh lỗi thêm', 100.0, 'Tốt', 100.0)
create_doc_eval(emp_it_q1, 89.5, 89.5, 89.5, 'GOOD', 'manager_it', 'Làm việc chăm chỉ')

# 5. MKT Dept Doc (Target: DEPARTMENT=3)
mkt_dept_q1 = create_doc(1, 'DEPARTMENT', 3, 'CLOSED', 'ASSIGNED', 'director', 'director', 95.0)
mkt_leads_q1 = create_item(mkt_dept_q1, 7, None, 'MQL Leads Q1', 'Leads tiềm năng mang về', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 0, 70.0, 1000, 1100, 100.0)
mkt_abs_q1 = create_item(mkt_dept_q1, 9, None, 'Tỷ lệ đi muộn Q1', 'Kỷ luật giờ giấc', '%', 'LOWER_BETTER', 'PERCENTAGE', 0, 30.0, 5, 2, 100.0)

# 6. MKT Staff Doc (Target: EMPLOYEE=5)
emp_mkt_q1 = create_doc(1, 'EMPLOYEE', 5, 'CLOSED', 'PROPOSED', 'emp_mkt', 'manager_mkt', 96.0)
emkt_item1 = create_item(emp_mkt_q1, 7, None, 'Chiến dịch quảng cáo Leads Q1', 'Leads từ Facebook & GG', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 0, 80.0, 500, 580, 100.0)
emkt_item2 = create_item(emp_mkt_q1, 9, None, 'Đi muộn Q1', 'Giờ giấc chuyên cần', '%', 'LOWER_BETTER', 'PERCENTAGE', 0, 20.0, 2, 0, 100.0)
create_item_eval(emkt_item1, 100.0, 'Vượt chỉ tiêu', 100.0, 'Rất tốt', 100.0)
create_item_eval(emkt_item2, 100.0, 'Đầy đủ', 100.0, 'Tốt', 100.0)
create_doc_eval(emp_mkt_q1, 100.0, 100.0, 100.0, 'EXCELLENT', 'manager_mkt', 'Xuất sắc')


# ==========================================
# CYCLE 2 (Q2/2026 - ACTIVE)
# ==========================================

# 1. Company Doc (Target: COMPANY=1)
comp_q2 = create_doc(2, 'COMPANY', 1, 'APPROVED', 'ASSIGNED', 'director', 'director', 45.0)
c_rev_q2 = create_item(comp_q2, 1, None, 'Doanh thu Công ty Q2', 'Doanh số toàn công ty Q2', 'VNĐ', 'HIGHER_BETTER', 'NUMERIC', 0, 50.0, 12000000000.0, 5000000000.0, 41.6)
c_prof_q2 = create_item(comp_q2, 2, None, 'Lợi nhuận ròng Q2', 'Lợi nhuận sau thuế Q2', 'VNĐ', 'HIGHER_BETTER', 'NUMERIC', 0, 50.0, 2500000000.0, 1200000000.0, 48.0)

# 2. IT Dept Doc (Target: DEPARTMENT=2)
it_dept_q2 = create_doc(2, 'DEPARTMENT', 2, 'APPROVED', 'ASSIGNED', 'director', 'director', 55.0)
it_uptime_q2 = create_item(it_dept_q2, 4, None, 'Uptime Hệ thống IT Q2', 'Hoạt động máy chủ', '%', 'HIGHER_BETTER', 'PERCENTAGE', 0, 50.0, 99.9, 99.8, 99.8)
it_bug_q2 = create_item(it_dept_q2, 5, None, 'Kiểm soát Bug Q2', 'Hạn chế bug hotfix', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 0, 50.0, 4, 3, 75.0)

# 3. IT Staff Docs
# - emp_it (id 3) gets APPROVED
emp_it_q2_approved = create_doc(2, 'EMPLOYEE', 3, 'APPROVED', 'PROPOSED', 'emp_it', 'manager_it', 60.0)
eit_app_item1 = create_item(emp_it_q2_approved, 8, None, 'Phát triển Feature X', 'Story points hoàn thành', 'Points', 'HIGHER_BETTER', 'NUMERIC', 0, 60.0, 80, 50, 62.5)
eit_app_item2 = create_item(emp_it_q2_approved, 5, None, 'Fix bugs release Q2', 'Fix bug kịp tiến độ', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 0, 40.0, 5, 2, 60.0)

# - emp_it_2 (id 6) gets PENDING_APPROVAL
emp_it_q2_pending = create_doc(2, 'EMPLOYEE', 6, 'PENDING_APPROVAL', 'PROPOSED', 'emp_it_2', None, 0.0)
eit_pen_item1 = create_item(emp_it_q2_pending, 8, None, 'Refactoring Codebase', 'Hoàn thành refactor core', 'Points', 'HIGHER_BETTER', 'NUMERIC', 0, 50.0, 40, 0, 0.0)
eit_pen_item2 = create_item(emp_it_q2_pending, 5, None, 'Bảo mật API', 'Không để lọt lỗi OWASP', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 0, 50.0, 0, 0, 100.0)

# - emp_it_3 (id 7) gets DRAFT
emp_it_q2_draft = create_doc(2, 'EMPLOYEE', 7, 'DRAFT', 'PROPOSED', 'emp_it_3', None, 0.0)
eit_dr_item1 = create_item(emp_it_q2_draft, 6, None, 'Tự đào tạo Cloud AWS', 'Đạt chứng chỉ AWS Cloud Practitioner', 'Giờ', 'HIGHER_BETTER', 'NUMERIC', 0, 100.0, 40, 10, 25.0)

# - emp_it_4 (id 8) gets REJECTED
emp_it_q2_rejected = create_doc(2, 'EMPLOYEE', 8, 'REJECTED', 'PROPOSED', 'emp_it_4', 'manager_it', 0.0)
sql.append(f"UPDATE kpi_documents SET reject_reason = 'Trọng số phân bố chưa hợp lý, cần tập trung hơn vào coding.' WHERE id = {doc_id};")
eit_rej_item1 = create_item(emp_it_q2_rejected, 8, None, 'Nghiên cứu AI Tech', 'Research AI APIs', 'Points', 'HIGHER_BETTER', 'NUMERIC', 0, 10.0, 20, 0, 0.0)
eit_rej_item2 = create_item(emp_it_q2_rejected, 5, None, 'Support users', 'Hỗ trợ kỹ thuật phòng ban', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 0, 90.0, 10, 0, 0.0)


# 4. IT Manager Doc (Target: EMPLOYEE=2)
mgr_it_q2 = create_doc(2, 'EMPLOYEE', 2, 'APPROVED', 'ASSIGNED', 'manager_it', 'director', 70.0)
mit_q2_item1 = create_item(mgr_it_q2, 4, None, 'Duy trì Uptime hạ tầng hệ thống Q2', 'Đảm bảo uptime hệ thống ổn định', '%', 'HIGHER_BETTER', 'PERCENTAGE', 0, 70.0, 99.9, 99.9, 100.0)
mit_q2_item2 = create_item(mgr_it_q2, 6, None, 'Tuyển dụng & Đào tạo team IT', 'Bổ sung 2 dev mới', 'Giờ', 'HIGHER_BETTER', 'NUMERIC', 0, 30.0, 30, 0, 0.0)


# 5. MKT Dept Doc (Target: DEPARTMENT=3)
mkt_dept_q2 = create_doc(2, 'DEPARTMENT', 3, 'APPROVED', 'ASSIGNED', 'director', 'director', 40.0)
mkt_leads_q2 = create_item(mkt_dept_q2, 7, None, 'MQL Leads Q2', 'Leads tiềm năng mang về', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 0, 80.0, 1200, 480, 40.0)
mkt_abs_q2 = create_item(mkt_dept_q2, 9, None, 'Tỷ lệ chuyên cần Q2', 'Muộn dưới 3 lần', '%', 'LOWER_BETTER', 'PERCENTAGE', 0, 20.0, 5, 1, 100.0)

# 6. MKT Staff Docs
# - emp_mkt (id 5) gets APPROVED
emp_mkt_q2 = create_doc(2, 'EMPLOYEE', 5, 'APPROVED', 'PROPOSED', 'emp_mkt', 'manager_mkt', 35.0)
emkt_q2_item1 = create_item(emp_mkt_q2, 7, None, 'Chạy chiến dịch MKT hè', 'Tạo nguồn lead khách hàng mới', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 0, 80.0, 600, 210, 35.0)
emkt_q2_item2 = create_item(emp_mkt_q2, 9, None, 'Kỷ luật đi làm chuyên cần', 'Hạn chế muộn giờ', '%', 'LOWER_BETTER', 'PERCENTAGE', 0, 20.0, 1, 0, 100.0)

# - emp_mkt_2 (id 9) gets PENDING_APPROVAL
emp_mkt_q2_pending = create_doc(2, 'EMPLOYEE', 9, 'PENDING_APPROVAL', 'PROPOSED', 'emp_mkt_2', None, 0.0)
emkt_pen_item1 = create_item(emp_mkt_q2_pending, 7, None, 'Đồng bộ data leads CRM', 'Đồng bộ data khách hàng tự động', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 0, 100.0, 100, 0, 0.0)

# - emp_mkt_3 (id 10) gets DRAFT
emp_mkt_q2_draft = create_doc(2, 'EMPLOYEE', 10, 'DRAFT', 'PROPOSED', 'emp_mkt_3', None, 0.0)
emkt_dr_item1 = create_item(emp_mkt_q2_draft, 7, None, 'Thiết kế ấn phẩm truyền thông', 'Banner, Catalogue cho Q2', 'Leads', 'HIGHER_BETTER', 'NUMERIC', 0, 100.0, 50, 5, 10.0)

# Update parent links for cascading (Dual-Weight structures)
# Under mitigating bugs eit_app_item2 (ID = 1018)
sql.append("""
-- 10. Create child items for dual-weight test (tree structure)
-- Mitigating bugs (doc_id = 109, item_id = 1018, parent_weight = 0, document_weight = 40.0)
-- We will change its item_type to 'GROUP' and aggregate progress from children
UPDATE kpi_items SET item_type = 'GROUP' WHERE id = 1018;

-- Insert children for 1018:
-- Child 1: Fix Bug level 1 (parent_weight = 60%, target = 10, current = 10, progress = 100%)
INSERT INTO kpi_items (id, document_id, template_id, parent_id, name, description, unit, target_type, item_type, parent_weight, document_weight, target_value, current_value, progress, status, is_deleted, created_at, updated_at) VALUES
(2001, 109, 5, 1018, 'Fix Bugs Level 1', 'Sửa lỗi giao diện và logic nhẹ', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 60.00, 0.00, 10.0, 10.0, 100.0, 'ACTIVE', false, NOW(), NOW());

-- Child 2: Fix Bug level 2 (parent_weight = 40%, target = 5, current = 2, progress = 60%)
INSERT INTO kpi_items (id, document_id, template_id, parent_id, name, description, unit, target_type, item_type, parent_weight, document_weight, target_value, current_value, progress, status, is_deleted, created_at, updated_at) VALUES
(2002, 109, 5, 1018, 'Fix Bugs Level 2', 'Sửa lỗi bảo mật và hiệu năng', 'Lỗi', 'LOWER_BETTER', 'NUMERIC', 40.00, 0.00, 5.0, 2.0, 60.0, 'ACTIVE', false, NOW(), NOW());

-- Update parent progress (60% * 100 + 40% * 60 = 84%)
UPDATE kpi_items SET progress = 84.00 WHERE id = 1018;
""")

# Save generated statements to a file
with open("/home/ntkien223/kpi-management-project/kpi-management/seed_data_rich.sql", "w", encoding="utf-8") as f:
    f.write("\n".join(sql))
print("Successfully generated seed_data_rich.sql!")
