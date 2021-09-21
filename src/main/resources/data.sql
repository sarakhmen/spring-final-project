REPLACE INTO roles VALUES (1,'ADMIN');
REPLACE INTO roles VALUES (2,'USER');
REPLACE INTO roles VALUES (3,'BLOCKED');
REPLACE INTO users VALUES (1, true, 'artur.sarahman@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Artur Sarakhman');
REPLACE INTO user_role VALUES (1, 1);
REPLACE INTO users VALUES (2, true, 'user@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Example User');
REPLACE INTO user_role VALUES (2, 2);
REPLACE INTO users VALUES (3, true, 'blocked@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Blocked User');
REPLACE INTO user_role VALUES (3, 3);

REPLACE INTO products VALUES (1, NOW(), 1000, 'Telephone');
REPLACE INTO products VALUES (2, NOW(), 1500, 'TV');
REPLACE INTO products VALUES (3, NOW(), 13000, 'Car');
REPLACE INTO products VALUES (4, NOW(), 800, 'Washer');
REPLACE INTO products VALUES (5, NOW(), 100, 'Blanked');
REPLACE INTO products VALUES (6, NOW(), 1200, 'Computer');
REPLACE INTO products VALUES (7, NOW(), 120, 'Schoolbag');
REPLACE INTO products VALUES (8, NOW(), 500, 'Sofa');
REPLACE INTO products VALUES (9, NOW(), 300, 'Vacuum');
REPLACE INTO products VALUES (10, NOW(), 600, 'Fridge');
REPLACE INTO products VALUES (11, NOW(), 150, 'Iron');
REPLACE INTO products VALUES (12, NOW(), 50, 'Kettle');

REPLACE INTO properties VALUES (1, 'Model', 'pixel4', 1);
REPLACE INTO properties VALUES (2, 'Color', 'grey', 1);
REPLACE INTO properties VALUES (3, 'Model', 'LG', 2);
REPLACE INTO properties VALUES (4, 'Size', 'big', 2);
REPLACE INTO properties VALUES (5, 'Model', 'audi a6', 3);
REPLACE INTO properties VALUES (6, 'Size', 'very big', 3);
REPLACE INTO properties VALUES (7, 'Color', 'green', 3);

REPLACE INTO orders VALUES(1, 'UNREGISTERED', 1, 1);
REPLACE INTO orders VALUES(2, 'UNREGISTERED', 2, 1);
REPLACE INTO orders VALUES(3, 'UNREGISTERED', 3, 1);
REPLACE INTO orders VALUES(4, 'UNREGISTERED', 4, 1);