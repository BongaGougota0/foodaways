--INSERT INTO `roles` (`role`)
-- VALUES ('admin'), ('store_owner'), ('customer');
--
--INSERT INTO `store_user` ( `full_name`, `mobile_number`, `email`, `password`, `role_id`)
--VALUES ('Bonga Gougota', '0812779593','bonga@mail.com', 'bonga@mail.com', 2),
--('Admin User', '0123456789','admin@mail.com', 'admin@mail.com', 1),
--('Thato Someone', '0812379593','thato@mail.com', 'thato@mail.com', 3);
--
--INSERT INTO `status` (`status`)
-- VALUES ('order_placed'),('order_accepted'), ('preparing_order'), ('in_transit'), ('delivered');
--
--INSERT INTO `store` (`store_name`,`store_number`,`location`,`store_owner`)
-- VALUES('Tashas','011 267 0903', 'Midrand', 1);
--
-- INSERT INTO `product` (`menu_items`, `product_image`, `product_price`, `product_store`)
--  VALUES ('spinach, coleslow, tomato, prawn','../images/products/default.jpeg', 93.90, 1),
--  ('calamari and prawn','../images/products/default.jpeg', 120.99, 1);
--
--INSERT INTO `order_data` (`order_status`, `user_id`, `order_items`, `created_at`, `order_to`)
--VALUES (1,3,1,CURDATE(),1);
