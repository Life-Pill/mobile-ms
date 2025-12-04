-- V2__Seed_Branch_Data.sql
-- Seed data for Branch Service
-- Note: Employers are now managed by Identity Service with branchId reference

-- Insert sample branches (matching Entity column names)
INSERT INTO branch (branch_name, branch_address, branch_contact, branch_fax, branch_email, branch_description, branch_status, branch_location, branch_created_on, branch_created_by, branch_latitude, branch_longitude, opening_hours, closing_hours) VALUES
('LifePill Main Branch', '123 Health Street, Colombo 01', '0112345678', '0112345679', 'main@lifepill.lk', 'Main headquarters and flagship pharmacy branch with full-service pharmacy, consultation rooms, and 24-hour emergency services', true, 'Colombo 01', '2024-01-01', 'System Admin', 6.9271, 79.8612, '08:00', '22:00'),
('LifePill Kandy Branch', '456 Temple Road, Kandy', '0812345678', '0812345679', 'kandy@lifepill.lk', 'Serving the hill capital region with specialized herbal medicine section', true, 'Kandy', '2024-02-15', 'System Admin', 7.2906, 80.6337, '08:30', '21:30'),
('LifePill Galle Branch', '789 Fort Road, Galle Fort', '0912345678', '0912345679', 'galle@lifepill.lk', 'Southern region main pharmacy with heritage building location', true, 'Galle', '2024-03-01', 'System Admin', 6.0535, 80.2210, '09:00', '21:00'),
('LifePill Negombo Branch', '321 Beach Road, Negombo', '0312345678', '0312345679', 'negombo@lifepill.lk', 'Coastal area pharmacy services with tourist-friendly multilingual staff', true, 'Negombo', '2024-04-10', 'System Admin', 7.2094, 79.8358, '08:00', '22:00'),
('LifePill Jaffna Branch', '654 Hospital Road, Jaffna', '0212345678', '0212345679', 'jaffna@lifepill.lk', 'Northern region healthcare hub serving the peninsula community', true, 'Jaffna', '2024-05-20', 'System Admin', 9.6615, 80.0255, '08:00', '20:00')
ON CONFLICT DO NOTHING;
