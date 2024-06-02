--create database foodawaysDB;

--use foodawaysDB;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `store_user` (
  `id` int AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `mobile_number` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(350) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS `store` (
  `id` int AUTO_INCREMENT,
  `store_name` varchar(100) NOT NULL,
  `store_number` varchar(15) NOT NULL,
  `location` varchar(100) NOT NULL,
  `store_owner` INT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (store_owner) REFERENCES store_user(id)
);

CREATE TABLE IF NOT EXISTS `status` (
   `id` int AUTO_INCREMENT PRIMARY KEY,
   `status` varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `product` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `menu_items` VARCHAR(500),
    `product_image` VARCHAR(500) NULL,
    `product_price` double NOT NULL,
    `product_store` int NOT NULL,
    FOREIGN KEY (product_store) REFERENCES store(id)
);

CREATE TABLE IF NOT EXISTS `order` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `order_status` int NOT NULL,
    `user_id` int NOT NULL,
    `order_items` int NULL,
    `order_total` double NULL,
    `created_at` TIMESTAMP DEFAULT NULL,
    `closed_at` TIMESTAMP DEFAULT NULL,
    `order_to` int NOT NULL,
    FOREIGN KEY (order_status) REFERENCES status(id),
    FOREIGN KEY (user_id) REFERENCES store_user(id),
    FOREIGN KEY (order_to) REFERENCES store(id),
    FOREIGN KEY (order_items) REFERENCES product(id)
);