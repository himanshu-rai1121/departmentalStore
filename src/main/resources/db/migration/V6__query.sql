-- Insert data into Customer table
INSERT INTO Customer (full_name, address, contact_number) VALUES
('Himanshu Kumar', 'Gurgaon', '1234567890'),
('Rahul Singh', 'Delhi', '4567890123'),
('Priyanshu Gupta', 'Ghaziabad', '7890123456');

-- Insert data into Product table
INSERT INTO Product (name, description, price, expiry, count, availability) VALUES
('Shirt', 'It is a shirt', 100.99, '2024-06-30', 10, true),
('T-Shirt', 'It is a T shirt', 15.99, '2024-08-31', 50, true),
('Trouser', 'It is a trouser', 599.00, '2024-12-31', 200, true);

-- Insert data into Discount table
INSERT INTO Discount (name, min_price, start_date_time, end_date_time, description, discount_value, coupon_code) VALUES
('Discount 1', 0, '2024-03-01 00:00:00', '2024-05-31 23:59:59', 'Use this to avail flat 50% discount on all your orders.', 50.00, 'FLAT50'),
('Discount 2', 1000.00, '2024-04-01 00:00:00', '2024-06-30 23:59:59', 'Get 75% discount on minimum order value of 1000', 75.00, 'SPRING75'),
('Discount 3', 15.00, '2024-07-01 00:00:00', '2025-07-31 23:59:59', 'Get for free', 100.00, 'FREE');

-- Insert data into customer_order table
INSERT INTO customer_order (product_id, customer_id, timestamp, quantity, discount_id, amount) VALUES
(1, 1, '2024-04-29 20:30:00', 4, 1, 21.98),
(2, 2, '2024-04-29 21:30:00', 1, NULL, 15.99);

-- Insert data into Backorder table
INSERT INTO Backorder (product_id, customer_id, timestamp, quantity) VALUES
(1, 2, '2024-04-29 20:00:00', 50),
(2, 1, '2024-04-29 21:00:00', 60);
