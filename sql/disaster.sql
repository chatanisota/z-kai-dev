DROP TABLE IF EXISTS disaster;
CREATE TABLE disaster
(
id 		SERIAL,
user_id 	VARCHAR(50),
mini_report	VARCHAR(59),
report		VARCHAR(200),
sended		INT,
timestamp	TIMESTAMP,
PRIMARY KEY (id)
);

