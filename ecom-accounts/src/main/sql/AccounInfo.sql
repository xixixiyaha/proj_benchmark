CREATE TABLE ACCOUNT_INFOS(
            user_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
            user_name VARCHAR(255) ,
            user_pwd CHAR(40),
            user_description TEXT,
            CREATE_TIME DATETIME,
            UPDATE_TIME DATETIME,
            INDEX (user_name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;