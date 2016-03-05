CREATE TABLE people (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
	name VARCHAR(50) NOT NULL,
	surname VARCHAR(100) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE users (
	login VARCHAR(100) NOT NULL,
	password VARCHAR(64) NOT NULL,
	PRIMARY KEY (login)
);

CREATE TABLE pets(
id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
people_id int NOT NULL,
name varchar(10) DEFAULT NOT NULL,
breed varchar(20) DEFAULT NULL,
animal varchar(30) DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT pets_fk FOREIGN KEY (people_id) REFERENCES people(id) ON DELETE CASCADE ON UPDATE CASCADE
);