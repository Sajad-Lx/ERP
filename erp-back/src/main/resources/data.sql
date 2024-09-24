--SET FOREIGN_KEY_CHECK = 0;

--TRUNCATE TABLE app_role;
--INSERT INTO app_role (id, role_name, description)
--VALUES (1, 'ADMIN', 'Admin User - Has permission to perform admin tasks');
--INSERT INTO app_role (id, role_name, description)
--VALUES (2, 'STANDARD_USER', 'Standard User - Has no admin rights');

TRUNCATE TABLE app_user;
-- password test1234
INSERT INTO app_user (id, username, email, first_name, last_name, password, is_Using2FA, secret, role)
VALUES (1, 'admin.admin', 'admin@mail.com', 'Admin', 'Admin', '$2a$10$JSgv0UFyA6Ly7e.1lQGzmepbUSBx0vpQriE/YV2bBa6gbV/qX.0zW', false, '5TPFV3VGX3EKYAET', 'ADMIN');
-- password test1234
INSERT INTO app_user (id, username, email, first_name, last_name, password, is_Using2FA, secret, role)
VALUES (2, 'john.doe', 'john@mail.com', 'John', 'Doe', '$2a$10$JSgv0UFyA6Ly7e.1lQGzmepbUSBx0vpQriE/YV2bBa6gbV/qX.0zW', true, '5TPFV3VGX3EKYAET', 'STANDARD_USER');

--TRUNCATE TABLE user_role;
--INSERT INTO user_role(user_id, role_id)
--VALUES (1, 1);
--INSERT INTO user_role(user_id, role_id)
--VALUES (1, 2);
--INSERT INTO user_role(user_id, role_id)
--VALUES (2, 2);

--SET FOREIGN_KEY_CHECKS = 1;