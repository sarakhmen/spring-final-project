REPLACE INTO roles VALUES (1,'ADMIN');
REPLACE INTO roles VALUES (2,'USER');
REPLACE INTO roles VALUES (3,'BLOCKED');
REPLACE INTO users VALUES (1, true, 'artur.sarahman@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Artur Sarakhman');
REPLACE INTO user_role VALUES (1, 1);
REPLACE INTO users VALUES (2, true, 'user@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Example User');
REPLACE INTO user_role VALUES (2, 2);
REPLACE INTO users VALUES (3, true, 'blocked@gmail.com', '$2a$12$IcsJIPLyMdHog5ntehnhW.9eMn7J9U1iR3d8hpoJHjWeCLavwdrSm', 'Blocked User');
REPLACE INTO user_role VALUES (3, 3);