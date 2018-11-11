DROP TABLE IF EXISTS users;
CREATE TABLE users
(id 		SERIAL,
user_id 	VARCHAR(50),
friend1_id 	INT		DEFAULT -1, 
friend2_id 	INT	 	DEFAULT -1,
friend3_id 	INT		DEFAULT -1,
status		INT		DEFAULT 1,
timestamp 	TIMESTAMP	DEFAULT 'yesterday',
tmp_prefecture_id INT		DEFAULT 1,
tmp_city_id	INT		DEFAULT 100,
tmp_friend_id	INT		DEFAULT -1,
tmp_name	VARCHAR(20),
tmp_kind	INT		DEFAULT 255,
PRIMARY KEY (id),
UNIQUE (user_id)
);

