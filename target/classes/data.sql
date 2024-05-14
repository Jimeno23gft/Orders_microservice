DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    user_id BIGINT,
    from_address VARCHAR(150),
    to_address LONG,
    status ENUM('UNPAID','PAID','SENT','IN_DELIVERY','CANCELLED','DELIVERED','UNKNOWN'),
    date_ordered VARCHAR(150),
    date_delivered VARCHAR(150)
);

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1001, '123 Main St', 111, 'PAID', '2024/05/07', '2024/05/10');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1002, '456 Elm St', 222, 'UNPAID', '2024/05/08', '2024/05/10');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1003, '789 Oak St', 333, 'IN_DELIVERY', '2024/05/09', '2024/05/11');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1004, '101 Maple Ave', 444, 'DELIVERED', '2024/05/10', '2024/05/12');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1005, '222 Pine St', 555, 'UNKNOWN', '2024/05/11', '2024/05/10');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1006, '333 Cedar Rd', 666, 'PAID', '2024/05/12', '2024/05/14');

DROP TABLE IF EXISTS orderedProducts;
CREATE TABLE orderedProducts (
    order_id LONG,
    product_id LONG,
    quantity LONG,
    CONSTRAINT PK_orderedProducts PRIMARY KEY (order_id,product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

INSERT INTO orderedProducts (order_id, product_id, quantity)
VALUES
    (1, 1001, 5),
    (1, 1002, 3),
    (2, 1003, 2),
    (3, 1004, 1),
    (4, 1005, 4),
    (5, 1006, 2),
    (6, 1007, 1),
    (6, 1008, 3);
