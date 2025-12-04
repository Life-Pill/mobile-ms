-- V2__Seed_Inventory_Data.sql
-- Seed data for Inventory Service

-- Insert supplier companies
INSERT INTO supplier_company (company_name, company_address, company_contact, company_email, company_description, company_website, company_registration_number, is_active) VALUES
('PharmaCo International', '100 Pharma Street, Colombo 10', '0112001001', 'info@pharmaco.lk', 'Leading pharmaceutical distributor in Sri Lanka', 'www.pharmaco.lk', 'REG-001-2020', true),
('MedSupply Lanka', '200 Medical Road, Colombo 05', '0112002002', 'sales@medsupply.lk', 'Premium medical supplies and equipment', 'www.medsupply.lk', 'REG-002-2021', true),
('HealthCare Distributors', '300 Health Avenue, Colombo 03', '0112003003', 'contact@hcdist.lk', 'Comprehensive healthcare product distribution', 'www.hcdist.lk', 'REG-003-2019', true),
('Global Medicines Ltd', '400 Global Lane, Colombo 08', '0112004004', 'order@globalmeds.lk', 'International medicine importers', 'www.globalmeds.lk', 'REG-004-2018', true),
('WellnessPlus Solutions', '500 Wellness Road, Colombo 06', '0112005005', 'support@wellnessplus.lk', 'Wellness and supplement specialists', 'www.wellnessplus.lk', 'REG-005-2022', true);

-- Insert suppliers
INSERT INTO supplier (company_id, supplier_name, supplier_address, supplier_phone, supplier_email, supplier_description, supplier_rating, is_active) VALUES
(1, 'PharmaCo Main Supplier', '100 Pharma Street, Colombo 10', '0771001001', 'supplier1@pharmaco.lk', 'Primary supplier for generic medicines', '4.5', true),
(1, 'PharmaCo Specialty', '100 Pharma Street, Colombo 10', '0771001002', 'specialty@pharmaco.lk', 'Specialty and rare medicines supplier', '4.8', true),
(2, 'MedSupply Equipment', '200 Medical Road, Colombo 05', '0772002001', 'equipment@medsupply.lk', 'Medical equipment and devices', '4.3', true),
(2, 'MedSupply Consumables', '200 Medical Road, Colombo 05', '0772002002', 'consumables@medsupply.lk', 'Medical consumables and disposables', '4.4', true),
(3, 'HealthCare Primary', '300 Health Avenue, Colombo 03', '0773003001', 'primary@hcdist.lk', 'Primary healthcare products', '4.6', true),
(4, 'Global Pharma Import', '400 Global Lane, Colombo 08', '0774004001', 'import@globalmeds.lk', 'Imported pharmaceutical products', '4.7', true),
(5, 'WellnessPlus Supplements', '500 Wellness Road, Colombo 06', '0775005001', 'supplements@wellnessplus.lk', 'Vitamins and dietary supplements', '4.2', true);

-- Insert item categories
INSERT INTO item_category (category_name, category_description, is_active) VALUES
('Painkillers', 'Pain relief medications including analgesics and anti-inflammatory drugs', true),
('Antibiotics', 'Antimicrobial drugs for treating bacterial infections', true),
('Vitamins & Supplements', 'Nutritional supplements and vitamin products', true),
('Cardiovascular', 'Heart and blood pressure medications', true),
('Diabetes', 'Diabetes management medications and supplies', true),
('Respiratory', 'Respiratory and asthma medications', true),
('Dermatology', 'Skin care and dermatological products', true),
('Gastrointestinal', 'Digestive system medications', true),
('First Aid', 'First aid supplies and bandages', true),
('Baby Care', 'Infant and baby healthcare products', true);

-- Insert sample items (medicines and healthcare products)
INSERT INTO item (category_id, supplier_id, branch_id, item_name, selling_price, item_barcode, supply_date, supplier_price, is_free_issued, is_discounted, item_manufacture, item_quantity, is_stock, measuring_unit_type, manufacture_date, expire_date, purchase_date, rack_number, discounted_percentage, warehouse_name, item_description) VALUES
-- Painkillers
(1, 1, 1, 'Paracetamol 500mg', 25.00, 'MED001', '2024-01-15', 18.00, false, false, 'PharmaCo Labs', 500, true, 'TABLETS', '2024-01-01', '2026-01-01', '2024-01-15', 'A1-01', 0, 'Main Warehouse', 'Pain relief and fever reducer'),
(1, 1, 1, 'Ibuprofen 400mg', 35.00, 'MED002', '2024-01-15', 25.00, false, false, 'PharmaCo Labs', 300, true, 'TABLETS', '2024-01-01', '2026-01-01', '2024-01-15', 'A1-02', 0, 'Main Warehouse', 'Anti-inflammatory pain relief'),
(1, 2, 1, 'Aspirin 100mg', 20.00, 'MED003', '2024-02-01', 14.00, false, true, 'Specialty Pharma', 400, true, 'TABLETS', '2024-01-15', '2026-01-15', '2024-02-01', 'A1-03', 10, 'Main Warehouse', 'Blood thinner and pain relief'),
-- Antibiotics
(2, 1, 1, 'Amoxicillin 500mg', 85.00, 'MED004', '2024-01-20', 60.00, false, false, 'PharmaCo Labs', 200, true, 'CAPSULES', '2024-01-01', '2025-06-01', '2024-01-20', 'A2-01', 0, 'Main Warehouse', 'Broad-spectrum antibiotic'),
(2, 6, 1, 'Azithromycin 250mg', 120.00, 'MED005', '2024-02-10', 85.00, false, false, 'Global Pharma', 150, true, 'TABLETS', '2024-01-15', '2025-07-15', '2024-02-10', 'A2-02', 0, 'Main Warehouse', 'Macrolide antibiotic'),
(2, 1, 2, 'Ciprofloxacin 500mg', 95.00, 'MED006', '2024-01-25', 68.00, false, false, 'PharmaCo Labs', 180, true, 'TABLETS', '2024-01-01', '2025-08-01', '2024-01-25', 'A2-03', 0, 'Main Warehouse', 'Fluoroquinolone antibiotic'),
-- Vitamins
(3, 7, 1, 'Vitamin C 1000mg', 45.00, 'VIT001', '2024-02-01', 32.00, false, true, 'WellnessPlus', 600, true, 'TABLETS', '2024-01-01', '2026-06-01', '2024-02-01', 'B1-01', 15, 'Main Warehouse', 'Immune support vitamin'),
(3, 7, 1, 'Vitamin D3 5000IU', 65.00, 'VIT002', '2024-02-01', 45.00, false, false, 'WellnessPlus', 400, true, 'CAPSULES', '2024-01-01', '2026-06-01', '2024-02-01', 'B1-02', 0, 'Main Warehouse', 'Bone health vitamin'),
(3, 7, 2, 'Multivitamin Complex', 150.00, 'VIT003', '2024-02-15', 105.00, false, false, 'WellnessPlus', 300, true, 'TABLETS', '2024-01-15', '2026-07-15', '2024-02-15', 'B1-03', 0, 'Main Warehouse', 'Complete daily multivitamin'),
-- Cardiovascular
(4, 6, 1, 'Amlodipine 5mg', 55.00, 'CV001', '2024-01-10', 38.00, false, false, 'Global Pharma', 250, true, 'TABLETS', '2024-01-01', '2025-12-01', '2024-01-10', 'C1-01', 0, 'Main Warehouse', 'Blood pressure medication'),
(4, 1, 1, 'Atorvastatin 20mg', 75.00, 'CV002', '2024-01-15', 52.00, false, false, 'PharmaCo Labs', 200, true, 'TABLETS', '2024-01-01', '2025-11-01', '2024-01-15', 'C1-02', 0, 'Main Warehouse', 'Cholesterol lowering medication'),
-- Diabetes
(5, 6, 1, 'Metformin 500mg', 40.00, 'DM001', '2024-01-20', 28.00, false, false, 'Global Pharma', 350, true, 'TABLETS', '2024-01-01', '2025-10-01', '2024-01-20', 'D1-01', 0, 'Main Warehouse', 'Type 2 diabetes medication'),
(5, 1, 2, 'Glimepiride 2mg', 65.00, 'DM002', '2024-02-01', 45.00, false, false, 'PharmaCo Labs', 180, true, 'TABLETS', '2024-01-15', '2025-09-15', '2024-02-01', 'D1-02', 0, 'Main Warehouse', 'Diabetes medication'),
-- Respiratory
(6, 5, 1, 'Salbutamol Inhaler', 450.00, 'RESP001', '2024-02-05', 320.00, false, false, 'HealthCare Dist', 80, true, 'UNIT', '2024-01-01', '2025-06-01', '2024-02-05', 'E1-01', 0, 'Main Warehouse', 'Asthma rescue inhaler'),
(6, 5, 1, 'Montelukast 10mg', 85.00, 'RESP002', '2024-02-10', 60.00, false, false, 'HealthCare Dist', 150, true, 'TABLETS', '2024-01-15', '2025-07-15', '2024-02-10', 'E1-02', 0, 'Main Warehouse', 'Asthma prevention medication'),
-- Gastrointestinal
(8, 1, 1, 'Omeprazole 20mg', 50.00, 'GI001', '2024-01-25', 35.00, false, false, 'PharmaCo Labs', 280, true, 'CAPSULES', '2024-01-01', '2025-12-01', '2024-01-25', 'F1-01', 0, 'Main Warehouse', 'Acid reflux medication'),
(8, 5, 2, 'Domperidone 10mg', 35.00, 'GI002', '2024-02-01', 24.00, false, false, 'HealthCare Dist', 220, true, 'TABLETS', '2024-01-15', '2025-11-15', '2024-02-01', 'F1-02', 0, 'Main Warehouse', 'Anti-nausea medication'),
-- First Aid
(9, 3, 1, 'Sterile Bandage Roll', 25.00, 'FA001', '2024-02-15', 18.00, false, false, 'MedSupply', 500, true, 'UNIT', '2024-01-01', '2027-01-01', '2024-02-15', 'G1-01', 0, 'Main Warehouse', 'Sterile cotton bandage'),
(9, 4, 1, 'Antiseptic Solution 100ml', 85.00, 'FA002', '2024-02-15', 60.00, false, false, 'MedSupply', 200, true, 'ML', '2024-01-01', '2026-06-01', '2024-02-15', 'G1-02', 0, 'Main Warehouse', 'Wound cleaning antiseptic'),
-- Baby Care
(10, 7, 1, 'Baby Vitamin Drops', 180.00, 'BC001', '2024-02-20', 125.00, false, false, 'WellnessPlus', 100, true, 'ML', '2024-01-15', '2025-07-15', '2024-02-20', 'H1-01', 0, 'Main Warehouse', 'Infant vitamin supplement');
