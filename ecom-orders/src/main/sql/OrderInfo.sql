CREATE TABLE ORDER_INFOS(
    order_id INT PRIMARY KEY NOT NULL ,
    user_id BIGINT NOT NULL ,
    payment_status TINYINT,
    merchant_id BIGINT NOT NULL,
    merchant_name VARCHAR(255),
    prod_id INT NOT NULL,
    prod_name VARCHAR (255),
    payment_id BIGINT,
     shipping_id BIGINT,
    CREATE_TIME DATETIME,
    UPDATE_TIME DATETIME ,
    INDEX(user_id,prod_id)
)