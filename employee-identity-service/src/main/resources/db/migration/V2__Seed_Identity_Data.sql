-- V2__Seed_Identity_Data.sql
-- Seed data for Employee Identity Service
-- Contains employer data with references to branches (branch_id references Branch Service)
-- Password is BCrypt hash of 'password123' for all users

-- Insert employer bank details first
INSERT INTO employer_bank_details (bank_name, bank_branch_name, bank_account_number, employer_description, monthly_payment, payment_status, employer_id) VALUES
-- Main Branch (branch_id = 1) Bank Details
('Bank of Ceylon', 'Colombo Main', '0012345678901234', 'Branch Manager - Colombo Main Branch with 10+ years experience', 150000.00, true, 1),
('Commercial Bank', 'Colombo Fort', '0023456789012345', 'Senior Cashier - Main Branch, handling high-volume transactions', 75000.00, true, 2),
('Peoples Bank', 'Colombo 08', '0034567890123456', 'Senior Pharmacist - Main Branch, registered with SLMC', 85000.00, true, 3),

-- Kandy Branch (branch_id = 2) Bank Details
('Bank of Ceylon', 'Kandy Main', '0045678901234567', 'Branch Manager - Kandy Region, overseeing hill country operations', 140000.00, true, 4),
('Sampath Bank', 'Kandy Peradeniya', '0056789012345678', 'Cashier - Kandy Branch, bilingual Tamil/Sinhala speaker', 70000.00, true, 5),
('HNB', 'Kandy Lake View', '0156789012345679', 'Pharmacist - Kandy Branch, herbal medicine specialist', 80000.00, true, 6),

-- Galle Branch (branch_id = 3) Bank Details
('HNB', 'Galle Fort', '0067890123456789', 'Branch Manager - Galle Region, heritage branch specialist', 135000.00, true, 7),
('Commercial Bank', 'Galle Town', '0078901234567890', 'Senior Cashier - Galle Branch, tourist services trained', 72000.00, true, 8),
('Peoples Bank', 'Galle Matara Road', '0178901234567891', 'Pharmacist - Galle Branch, general pharmacy', 78000.00, true, 9),

-- Negombo Branch (branch_id = 4) Bank Details
('NSB', 'Negombo Main', '0089012345678901', 'Branch Manager - Negombo, coastal region operations', 130000.00, true, 10),
('Seylan Bank', 'Negombo Beach Road', '0189012345678902', 'Cashier - Negombo Branch, multilingual English/Sinhala', 68000.00, true, 11),
('Commercial Bank', 'Negombo City', '0289012345678903', 'Pharmacist - Negombo Branch, general pharmacy', 76000.00, true, 12),

-- Jaffna Branch (branch_id = 5) Bank Details
('Bank of Ceylon', 'Jaffna Main', '0090123456789012', 'Branch Manager - Jaffna Region, northern operations head', 125000.00, true, 13),
('Commercial Bank', 'Jaffna Hospital Road', '0190123456789013', 'Cashier - Jaffna Branch, Tamil language specialist', 65000.00, true, 14),
('Peoples Bank', 'Jaffna Town', '0290123456789014', 'Pharmacist - Jaffna Branch, general pharmacy', 75000.00, true, 15);

-- Insert employers/staff with branch_id references
-- Password: password123 (BCrypt encoded)
INSERT INTO employer (employer_nic_name, employer_first_name, employer_last_name, employer_email, employer_password, employer_phone, employer_address, employer_salary, employer_nic, is_active, gender, employer_date_of_birth, role, pin, branch_id, employer_bank_details_id) VALUES
-- Main Branch Staff (branch_id = 1)
('saman', 'Saman', 'Perera', 'saman.perera@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0771234567', '45 Park Lane, Colombo 03, Sri Lanka', 150000.00, '891234567V', true, 'MALE', '1989-05-15', 'MANAGER', 1234, 1, 1),
('kumari', 'Kumari', 'Silva', 'kumari.silva@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0781234568', '67 Garden Road, Colombo 05, Sri Lanka', 75000.00, '901234568V', true, 'FEMALE', '1990-08-22', 'CASHIER', 1235, 1, 2),
('nimal', 'Nimal', 'Fernando', 'nimal.fernando@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0761234569', '89 Lake Drive, Colombo 08, Sri Lanka', 85000.00, '881234569V', true, 'MALE', '1988-12-10', 'OTHER', 1236, 1, 3),

-- Kandy Branch Staff (branch_id = 2)
('nimali', 'Nimali', 'Fernando', 'nimali.fernando@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0772345678', '12 Hill Street, Kandy, Sri Lanka', 140000.00, '871234570V', true, 'FEMALE', '1987-03-25', 'MANAGER', 2234, 2, 4),
('chaminda', 'Chaminda', 'Ratnayake', 'chaminda.r@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0782345679', '34 Temple Road, Kandy, Sri Lanka', 70000.00, '921234571V', true, 'MALE', '1992-07-18', 'CASHIER', 2235, 2, 5),
('priyanka', 'Priyanka', 'Wijesekara', 'priyanka.w@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0792345680', '56 Dalada Veediya, Kandy, Sri Lanka', 80000.00, '891234572V', true, 'FEMALE', '1989-11-05', 'OTHER', 2236, 2, 6),

-- Galle Branch Staff (branch_id = 3)
('kasun', 'Kasun', 'Silva', 'kasun.silva@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0773456789', '56 Fort Road, Galle Fort, Sri Lanka', 135000.00, '861234572V', true, 'MALE', '1986-11-30', 'MANAGER', 3234, 3, 7),
('dilani', 'Dilani', 'Wijesinghe', 'dilani.w@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0783456780', '78 Beach Lane, Unawatuna, Galle, Sri Lanka', 72000.00, '931234573V', true, 'FEMALE', '1993-02-14', 'CASHIER', 3235, 3, 8),
('ruwan', 'Ruwan', 'Jayasinghe', 'ruwan.j@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0793456781', '90 Lighthouse Street, Galle, Sri Lanka', 78000.00, '901234574V', true, 'MALE', '1990-06-20', 'OTHER', 3236, 3, 9),

-- Negombo Branch Staff (branch_id = 4)
('thilini', 'Thilini', 'Jayawardena', 'thilini.j@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0774567890', '90 Coastal Road, Negombo, Sri Lanka', 130000.00, '851234574V', true, 'FEMALE', '1985-09-08', 'MANAGER', 4234, 4, 10),
('asanka', 'Asanka', 'Perera', 'asanka.p@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0784567891', '45 Lagoon Road, Negombo, Sri Lanka', 68000.00, '941234575V', true, 'MALE', '1994-04-12', 'CASHIER', 4235, 4, 11),
('malika', 'Malika', 'Bandara', 'malika.b@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0794567892', '67 Sea Street, Negombo, Sri Lanka', 76000.00, '911234576V', true, 'FEMALE', '1991-08-25', 'OTHER', 4236, 4, 12),

-- Jaffna Branch Staff (branch_id = 5)
('raj', 'Raj', 'Kumar', 'raj.kumar@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0775678901', '12 Hospital Road, Jaffna, Sri Lanka', 125000.00, '841234575V', true, 'MALE', '1984-06-20', 'MANAGER', 5234, 5, 13),
('priya', 'Priya', 'Shankar', 'priya.s@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0785678902', '34 Temple Road, Jaffna, Sri Lanka', 65000.00, '951234576V', true, 'FEMALE', '1995-01-30', 'CASHIER', 5235, 5, 14),
('kumar', 'Kumar', 'Veerasingam', 'kumar.v@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0795678903', '56 Main Street, Jaffna, Sri Lanka', 75000.00, '881234577V', true, 'MALE', '1988-10-15', 'OTHER', 5236, 5, 15);

-- Insert Owner account (Super Admin)
INSERT INTO employer (employer_nic_name, employer_first_name, employer_last_name, employer_email, employer_password, employer_phone, employer_address, employer_salary, employer_nic, is_active, gender, employer_date_of_birth, role, pin, branch_id) VALUES
('admin', 'System', 'Administrator', 'admin@lifepill.lk', '$2a$10$N9qo8uLOickgx2ZMRZoMyu.z..jkPa.8LVi3G.pJqR25KuBp2B2Ki', '0770000000', 'LifePill Headquarters, Colombo 01, Sri Lanka', 250000.00, '800000000V', true, 'MALE', '1980-01-01', 'OWNER', 9999, 1);
