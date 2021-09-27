INSERT INTO roles (role) VALUES ('ADMIN');
INSERT INTO users (active, email, password, user_name) VALUES (true, 'artur.sarahman@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Artur Sarakhman');
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);

INSERT INTO products (creation_date, price, product_name) VALUES (NOW(), 1000, 'Telephone');
INSERT INTO products (creation_date, price, product_name) VALUES (NOW(), 1500, 'TV');
INSERT INTO properties (property_name, property_value, product_id) VALUES ('Model', 'pixel4', 1);
INSERT INTO properties (property_name, property_value, product_id) VALUES ('Size', 'small', 1);
INSERT INTO properties (property_name, property_value, product_id) VALUES ('Model', 'LG', 2);
INSERT INTO properties (property_name, property_value, product_id) VALUES ('Size', 'big', 2);

INSERT INTO orders (status, product_id, user_id) VALUES('UNREGISTERED', 1, 1);
INSERT INTO orders (status, product_id, user_id) VALUES('UNREGISTERED', 2, 1);
