package com.freeb.Entity;

import java.util.List;

public class ProductPage {
    Long prodId;
    String prodName;
    Integer prodSales;
    //TODO check图片用string可以吗 -solved
    // Yes: https://www.baeldung.com/java-base64-image-string
    List<String> prodImages;
    Long merchantId;
    Long merchantName;
    List<CommentInfo> prodComments;
}
