CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    user_id BIGINT,
    from_address VARCHAR(150),
    to_address LONG,
    status ENUM('UNPAID','PAID','SENT','IN_DELIVERY','CANCELLED','DELIVERED','UNKNOWN'),
    date_ordered DATETIME,
    date_delivered DATETIME
);
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


INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1001, '123 Main St', 111, 'PAID', '2024-05-07 08:00:00', null);

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1002, '456 Elm St', 222, 'UNPAID', '2024-05-08 09:00:00', null);

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1003, '789 Oak St', 333, 'IN_DELIVERY', '2024-05-09 10:00:00', null);

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1004, '101 Maple Ave', 444, 'DELIVERED', '2024-05-10 11:00:00', '2024-05-12 18:00:00');

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1005, '222 Pine St', 555, 'UNKNOWN', '2024-05-11 12:00:00', null);

INSERT INTO orders (user_id, from_address, to_address, status, date_ordered, date_delivered)
VALUES (1006, '333 Cedar Rd', 666, 'PAID', '2024-05-12 13:00:00', null);


INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (1, 1001, 'Product1', 'Category1', 'Description1', 50, 2);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (1, 1002, 'Product2', 'Category2', 'Description2', 30, 1);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (1, 1003, 'Product3', 'Category3', 'Description3', 40, 3);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (2, 1004, 'Product4', 'Category4', 'Description4', 60, 2);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (3, 1005, 'Product5', 'Category5', 'Description5', 70, 1);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (4, 1006, 'Product6', 'Category6', 'Description6', 80, 4);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (4, 1007, 'Product7', 'Category7', 'Description7', 90, 2);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (5, 1008, 'Product8', 'Category8', 'Description8', 100, 3);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (5, 1009, 'Product9', 'Category9', 'Description9', 110, 1);

INSERT INTO ordered_products (order_id, product_id, name, category, description, price, quantity)
VALUES (6, 1010, 'Product10', 'Category10', 'Description10', 120, 5);