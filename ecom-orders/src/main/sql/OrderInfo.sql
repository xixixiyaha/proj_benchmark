CREATE TABLE ORDER_INFOS(
    order_id INT PRIMARY KEY NOT NULL ,
    account_id INT,
    payment_status TINYINT,
    merchant_id INT NOT NULL,
    merchant_name VARCHAR(255),
    obj_id INT NOT NULL,
    obj_name VARCHAR (255),
    payment_id LONG,
--     shipping_id LONG,
    CREATE_TIME DATETIME,
    UPDATE_TIME DATETIME ,
)