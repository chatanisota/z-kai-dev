DROP TABLE IF EXISTS friend;
CREATE TABLE friend
(
id 		SERIAL,
user_id 	VARCHAR(50),
locate_id 	INT,
name		VARCHAR(20)	DEFAULT 'no name',
kind		INT	DEFAULT 255,
PRIMARY KEY (id)
);

