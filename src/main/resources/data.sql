INSERT INTO app_user (username, password,role) VALUES ('customer1','pass1','CUSTOMER');
INSERT INTO app_user (username, password,role) VALUES ('staff1','pass2','KITCHEN_STAFF');
INSERT INTO app_user (username, password,role) VALUES ('rider1','pass3','DELIVERY_PARTNER');

INSERT INTO customer (app_user_id,first_name, last_name,address) VALUES (1,'ALICE','Smith','123 Main St');

INSERT INTO menu_items (name, description, price, is_Available) VALUES ('Butter Chicken', 'A creamy chicken dish', 12.99, true);
INSERT INTO menu_items (name, description, price, is_Available) VALUES ('Naan', 'Tandoor baked bread', 2.50, true);
INSERT INTO menu_items (name, description, price, is_Available) VALUES ('Veg Biryani', 'Aromatic mixed veg rice', 10.50, true);

INSERT INTO delivery_partner (user_id,name,vehicle_type,current_location,is_available) VALUES (3, 'Bob Johnson', 'Bike', '456 Elm St', true);



