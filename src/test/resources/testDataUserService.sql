INSERT INTO roles (role) VALUES ('ADMIN');
INSERT INTO roles (role) VALUES ('USER');
INSERT INTO users (active, email, password, user_name) VALUES (true, 'artur.sarahman@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Artur Sarakhman');
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO users (active, email, password, user_name) VALUES (true, 'user@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Example User');
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO users (active, email, password, user_name) VALUES (false , 'blocked@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Blocked User');
INSERT INTO user_role (user_id, role_id) VALUES (3, 2);

-- INSERT INTO products VALUES (1, NOW(), 1000, 'Telephone');
-- INSERT INTO products VALUES (2, NOW(), 1500, 'TV');
-- INSERT INTO products VALUES (3, NOW(), 13000, 'Car');
-- INSERT INTO products VALUES (4, NOW(), 800, 'Washer');
-- INSERT INTO products VALUES (5, NOW(), 100, 'Blanked');
-- INSERT INTO products VALUES (6, NOW(), 1200, 'Computer');
-- INSERT INTO products VALUES (7, NOW(), 120, 'Schoolbag');
-- INSERT INTO products VALUES (8, NOW(), 500, 'Sofa');
-- INSERT INTO products VALUES (9, NOW(), 300, 'Vacuum');
-- INSERT INTO products VALUES (10, NOW(), 600, 'Fridge');
-- INSERT INTO products VALUES (11, NOW(), 150, 'Iron');
-- INSERT INTO products VALUES (12, NOW(), 50, 'Kettle');--
-- INSERT INTO properties VALUES (1, 'Model', 'pixel4', 1);
-- INSERT INTO properties VALUES (2, 'Color', 'grey', 1);
-- INSERT INTO properties VALUES (3, 'Model', 'LG', 2);
-- INSERT INTO properties VALUES (4, 'Size', 'big', 2);
-- INSERT INTO properties VALUES (5, 'Model', 'audi a6', 3);
-- INSERT INTO properties VALUES (6, 'Size', 'very big', 3);
-- INSERT INTO properties VALUES (7, 'Color', 'green', 3);
--
-- INSERT INTO orders VALUES(1, 'UNREGISTERED', 1, 1);
-- INSERT INTO orders VALUES(2, 'UNREGISTERED', 2, 1);
-- INSERT INTO orders VALUES(3, 'UNREGISTERED', 3, 1);
-- INSERT INTO orders VALUES(4, 'UNREGISTERED', 4, 1);