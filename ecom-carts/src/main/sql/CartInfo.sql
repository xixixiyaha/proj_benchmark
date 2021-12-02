CREATE TABLE CART_INFOS(
        cart_id LONG PRIMARY KEY NOT NULL,
        account_id LONG ,
        obj_id LONG,
        merchant_id LONG,
        incart_quantity MEDIUMINT,
        incart_select TINYINT,
        CREATE_TIME DATETIME,
        UPDATE_TIME DATETIME
)