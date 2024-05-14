DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id LONG AUTO_INCREMENT PRIMARY KEY ,
    user_id BIGINT,
    from_address VARCHAR(150),
    to_address LONG,
    status ENUM('UNPAID','PAID','SENT','IN_DELIVERY','CANCELLED','DELIVERED','UNKNOWN'),
    date_ordered DATETIME,
    date_delivered DATETIME
);

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1001, '123 Main St', 1, 'PAID', '2024-05-07 08:00:00', '2024-05-10 15:00:00');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1002, '456 Elm St', 2, 'UNPAID', '2024-05-08 09:00:00', '2024-05-10 16:00:00');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1003, '789 Oak St', 3, 'IN_DELIVERY', '2024-05-09 10:00:00', '2024-05-11 17:00:00');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1004, '101 Maple Ave', 4, 'DELIVERED', '2024-05-10 11:00:00', '2024-05-12 18:00:00');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1005, '222 Pine St', 5, 'UNKNOWN', '2024-05-11 12:00:00', '2024-05-10 14:00:00');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1006, '333 Cedar Rd', 6, 'PAID', '2024-05-12 13:00:00', '2024-05-14 19:00:00');

DROP TABLE IF EXISTS ordered_products;
CREATE TABLE ordered_products (
    order_id LONG,
    product_id LONG,
    quantity LONG,
    CONSTRAINT PK_orderedProducts PRIMARY KEY (order_id,product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
    address_id LONG AUTO_INCREMENT PRIMARY KEY,
    order_id LONG,
    street VARCHAR(255),
    number INT,
    door VARCHAR(255),
    city_name VARCHAR(255),
    zip_code VARCHAR(10),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

INSERT INTO addresses (order_id, street, number, door, city_name, zip_code) VALUES
(1, 'Main Street', 123, 'A', 'Springfield', '12345'),
(2, 'Elm Street', 456, 'B', 'Shelbyville', '67890'),
(3, 'Oak Street', 789, 'C', 'Capital City', '10112');

INSERT INTO ordered_products (order_id, product_id, quantity)
VALUES
    (1, 1001, 5),
    (1, 1002, 3),
    (2, 1003, 2),
    (3, 1004, 1),
    (4, 1005, 4),
    (5, 1006, 2),
    (6, 1007, 1),
    (6, 1008, 3);