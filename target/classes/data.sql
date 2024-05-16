
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    cart_id BIGINT,
    from_address VARCHAR(150),
    status ENUM('UNPAID','PAID','SENT','IN_DELIVERY','CANCELLED','DELIVERED','UNKNOWN'),
    date_ordered DATETIME,
    date_delivered DATETIME,
    total_price DOUBLE
);

INSERT INTO orders (cart_id, from_address, status, date_ordered, date_delivered,total_price)
VALUES (1001, '123 Main St', 'PAID', '2024-05-07 08:00:00', '2024-05-10 15:00:00',18);

INSERT INTO orders (cart_id, from_address, status, date_ordered, date_delivered,total_price)
VALUES (1002, '456 Elm St', 'UNPAID', '2024-05-08 09:00:00', '2024-05-10 16:00:00',18);

INSERT INTO orders (cart_id, from_address, status, date_ordered, date_delivered,total_price)
VALUES (1003, '789 Oak St', 'IN_DELIVERY', '2024-05-09 10:00:00', '2024-05-11 17:00:00',17);

INSERT INTO orders (cart_id, from_address, status, date_ordered, date_delivered,total_price)
VALUES (1004, '101 Maple Ave', 'DELIVERED', '2024-05-10 11:00:00', '2024-05-12 18:00:00',17);

INSERT INTO orders (cart_id, from_address, status, date_ordered, date_delivered,total_price)
VALUES (1005, '222 Pine St', 'UNKNOWN', '2024-05-11 12:00:00', '2024-05-10 14:00:00',16);

INSERT INTO orders (cart_id, from_address, status, date_ordered, date_delivered,total_price)
VALUES (1006, '333 Cedar Rd', 'PAID', '2024-05-12 13:00:00', '2024-05-14 19:00:00',18);


CREATE TABLE IF NOT EXISTS ordered_products (
    order_id LONG,
    product_id LONG,
    name VARCHAR(150),
    category VARCHAR(150),
    description VARCHAR(150),
    price LONG,
    quantity LONG,
    CONSTRAINT PK_orderedProducts PRIMARY KEY (order_id,product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS addresses (
    order_id LONG AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    number INT,
    door VARCHAR(255),
    city_name VARCHAR(255),
    zip_code VARCHAR(10),
    country_id LONG,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

INSERT INTO addresses (order_id, street, number, door, city_name, zip_code, country_id) VALUES
(1, 'Main Street', 123, 'A', 'Springfield', '12345', 1),
(2, 'Elm Street', 456, 'B', 'Shelbyville', '67890', 2),
(3, 'Oak Street', 789, 'C', 'Capital City', '10112', 3);



