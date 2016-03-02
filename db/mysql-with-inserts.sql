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
	`owner_id` int NOT NULL,
	`name` varchar(10) NOT NULL,
	`breed` varchar(20) NULL,
	`animal` varchar(30) NOT NULL,
	PRIMARY KEY (`id`),
	KEY `owner_id` (`owner_id`),
	FOREIGN KEY (`owner_id`) REFERENCES `daaexample`.`people`(`id`)
);

GRANT ALL ON `daaexample`.* TO 'daa'@'localhost' IDENTIFIED BY 'daa';

INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Antón','Pérez');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Manuel','Martínez');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Laura','Reboredo');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Perico','Palotes');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Ana','María');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'María','Nuevo');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Alba','Fernández');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Asunción','Jiménez');

INSERT INTO `daaexample`.`pets` (`id`,`owner_id`,`name`,`breed`, `animal` ) VALUES (0,1,'Whysky','Beagle', 'Dog');

INSERT INTO `daaexample`.`users` (`login`,`password`) VALUES ('admin', '0b893644f3b2097d004c58d585e784ac92dd1356d25158a298573ad54ab2d15d');
