CREATE TABLE ACCOUNT_INFOS(
            account_id LONG PRIMARY KEY NOT NULL ,
            user_name VARCHAR(255) ,
            user_pwd INT,
            user_description TEXT,
            CREATE_TIME DATETIME,
            UPDATE_TIME DATETIME
)