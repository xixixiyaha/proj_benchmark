CREATE TABLE PAYMENT_INFOS(
          payment_id BIGINT PRIMARY KEY NOT NULL,
          payment_status TINYINT,
#           payment_access TINYINT,
          payment_val DOUBLE,
          discounts_val DOUBLE,
          payment_card VARCHAR(32),
          user_id BIGINT NOT NULL ,
          CREATE_TIME DATETIME,
          UPDATE_TIME DATETIME
)