INSERT INTO ROLES(name) VALUES ('CUSTOMER');
INSERT INTO ROLES(name) VALUES ('KITCHEN_STAFF');
INSERT INTO ROLES(name) VALUES ('DELIVERY_PARTNER');


INSERT INTO app_user (username, password) VALUES ('customer1','pass1');
INSERT INTO app_user (username, password) VALUES ('staff1','pass2');
INSERT INTO app_user (username, password) VALUES ('rider1','pass3');

INSERT INTO USER_ROLES (app_user_id,roles_id) VALUES (1,1);
INSERT INTO USER_ROLES (app_user_id,roles_id) VALUES (2,2);
INSERT INTO USER_ROLES (app_user_id,roles_id) VALUES (3,3);

INSERT INTO customer (app_user_id,first_name, last_name,address) VALUES (1,'ALICE','Smith','123 Main St');

INSERT INTO menu_items (name, description, price, is_Available) VALUES ('Butter Chicken', 'A creamy chicken dish', 12.99, true);
INSERT INTO menu_items (name, description, price, is_Available) VALUES ('Naan', 'Tandoor baked bread', 2.50, true);
INSERT INTO menu_items (name, description, price, is_Available) VALUES ('Veg Biryani', 'Aromatic mixed veg rice', 10.50, true);

INSERT INTO delivery_partner (user_id,name,vehicle_type,current_location,is_available) VALUES (3, 'Bob Johnson', 'Bike', '456 Elm St', true);



