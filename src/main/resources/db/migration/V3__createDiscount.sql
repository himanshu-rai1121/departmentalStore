CREATE TABLE Discount (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    discount_value DECIMAL(5, 2) NOT NULL,
    start_date_time DATETIME NOT NULL,
    end_date_time DATETIME NOT NULL,
    description TEXT,
    min_price DECIMAL(10, 2) NOT NULL,
    coupon_code VARCHAR(255) NOT NULL
);
