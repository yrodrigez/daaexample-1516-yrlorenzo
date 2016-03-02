CREATE DATABASE `daaexample`;

CREATE TABLE `daaexample`.`people` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(50) DEFAULT NULL,
	`surname` varchar(100) DEFAULT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `daaexample`.`users` (
	`login` varchar(100) NOT NULL,
	`password` varchar(64) DEFAULT NULL,
	PRIMARY KEY (`login`)
);

CREATE TABLE `daaexample`.`pets`(
	`id` int NOT NULL AUTO_INCREMENT,
	`people_id` int NOT NULL,
	`name` varchar(10) DEFAULT NOT NULL,
	`breed` varchar(20) DEFAULT NULL,
	`animal` varchar(30) DEFAULT NULL,
	PRIMARY KEY (`id`),
	KEY `people_id` (`people_id`),
	FOREIGN KEY (`people_id`) REFERENCES `daaexample`.`people`(`id`)
);

GRANT ALL ON `daaexample`.* TO 'daa'@'localhost' IDENTIFIED BY 'daa';