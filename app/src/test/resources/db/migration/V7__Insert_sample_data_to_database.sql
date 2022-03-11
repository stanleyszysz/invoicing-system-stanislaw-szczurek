INSERT INTO company (id, tax_identifier, name) VALUES
    ('980ae02d-f1f4-4e75-b8d0-c2529af2acda', '5555555555', 'Abra 1'),
    ('4569edf4-566e-46b9-bb81-896185a08125', '6666666666', 'Abra 2'),
    ('c4d4b7cb-4b77-41ce-90ee-b7afd959f97d', '7777777777', 'Abra 3');

INSERT INTO address (company_id, city, postal_code, street_name, street_number) VALUES
    ((SELECT id FROM company WHERE tax_identifier='5555555555'), 'Wrocław', '99-999', 'Słonecznikowa', '15'),
    ((SELECT id FROM company WHERE tax_identifier='6666666666'), 'Warszawa', '00-000', 'Tulipanowa', '25'),
    ((SELECT id FROM company WHERE tax_identifier='7777777777'), 'Szczecin', '22-222', 'Różana', '35');

INSERT INTO invoice (id, date_at, invoice_number, seller, buyer) VALUES
	('e8dfcc75-5549-47fe-83bb-7bb0d6de76ce', '2022-02-24', '2022/02/24/001', (SELECT id FROM company WHERE tax_identifier='5555555555'), (SELECT id FROM company WHERE tax_identifier='6666666666')),
	('d94653d8-352d-4eed-89f4-75a3442e1175', '2022-02-24', '2022/02/24/002', (SELECT id FROM company WHERE tax_identifier='7777777777'), (SELECT id FROM company WHERE tax_identifier='6666666666'));

	INSERT INTO car (id, registration_number, personal_usage) VALUES
	('dda6200b-f835-429a-8409-f903c74e0275', 'DW8G888', false),
	('f5c2fed0-d5a8-43fe-9757-5c193685f854', 'DW9G999', true);

INSERT INTO invoice_entry (id, description, price, vat_value, vat_rate, car_related_expense, invoice) VALUES
    ('2ced996c-ab36-45a4-8d9b-c0450c529234', 'Item 1', 10.00, 2.30, (SELECT id FROM vat WHERE name='VAT_23'), (SELECT id FROM car WHERE registration_number='DW8G888'), (SELECT id FROM invoice WHERE invoice_number='2022/02/24/001')),
    ('9b48a5cb-85de-42af-8347-f580ce4cdafe', 'Item 2', 20.00, 1.60, (SELECT id FROM vat WHERE name='VAT_8'), (SELECT id FROM car WHERE registration_number='DW8G888'), (SELECT id FROM invoice WHERE invoice_number='2022/02/24/001')),
    ('ccc2b952-3cfe-4da4-a374-d565f96f20f9', 'Item 3', 30.00, 1.50, (SELECT id FROM vat WHERE name='VAT_5'), (SELECT id FROM car WHERE registration_number='DW8G888'), (SELECT id FROM invoice WHERE invoice_number='2022/02/24/001')),
    ('935c774a-3a25-4fb4-a90f-e588d6f6e5e9', 'Fuel', 300.00, 69.00, (SELECT id FROM vat WHERE name='VAT_23'), (SELECT id FROM car WHERE registration_number='DW9G999'), (SELECT id FROM invoice WHERE invoice_number='2022/02/24/002'));