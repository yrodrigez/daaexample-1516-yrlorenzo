CREATE DATABASE `daaexample`;

CREATE TABLE `daaexample`.`people` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL,
	`surname` varchar(100) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `daaexample`.`users` (
	`login` varchar(100) NOT NULL,
	`password` varchar(64) NOT NULL,
	PRIMARY KEY (`login`)
);

CREATE TABLE `daaexample`.`pets`(
	`id` int NOT NULL AUTO_INCREMENT,
	`owner_id` int NOT NULL,
	`name` varchar(10) NOT NULL,
	`breed` varchar(20) NOT NULL,
	`animal` varchar(30) NOT NULL ,
	PRIMARY KEY (`id`),
	KEY `people_id` (`owner_id`),
	FOREIGN KEY (`owner_id`) REFERENCES `daaexample`.`people`(`id`)
);

GRANT ALL ON `daaexample`.* TO 'daa'@'localhost' IDENTIFIED BY 'daa';