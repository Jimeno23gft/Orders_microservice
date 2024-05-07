-- Create a table called 'products'
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(255),
    price DECIMAL(10, 2),
    quantity INT
);

-- Insert some sample data into the 'products' table
INSERT INTO products (product_id, product_name, price, quantity) VALUES
(1, 'Keyboard', 25.99, 100),
(2, 'Mouse', 15.50, 150),
(3, 'Monitor', 199.99, 50),
(4, 'Headphones', 49.99, 75);