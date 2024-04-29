CREATE TABLE customer_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    timestamp DATETIME,
    quantity INT NOT NULL,
    discount_id BIGINT,
    amount DECIMAL(10, 2),
    FOREIGN KEY (product_id) REFERENCES Product(id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (discount_id) REFERENCES Discount(id)
);
