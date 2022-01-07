CREATE TABLE CART_INFOS(
                           cart_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                           user_id BIGINT NOT NULL ,
                           prod_id BIGINT NOT NULL ,
                           merchant_id BIGINT NOT NULL ,
                           incart_quantity MEDIUMINT COMMENT '购物车内的数量',
                           incart_select TINYINT COMMENT '是否被计入购买优惠',
                           CREATE_TIME DATETIME,
                           UPDATE_TIME DATETIME,
                           INDEX (user_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;