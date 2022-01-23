package com.freeb;

import com.freeb.thrift.SearchOrder;

public class SearchTypeConvert {
    public static SearchOrder SearchOrderOri2Thr(com.freeb.Enum.SearchOrder order){
        switch (order){
            case SIMILARITY:
                return SearchOrder.SIMILARITY;
            case PRICE_DESC:
                return SearchOrder.PRICE_DESC;
            case PRICE_ASC:
                return SearchOrder.PRICE_ASC;
            case SALES:
                return SearchOrder.SALES;
            case UPDATE_TIME:
                return SearchOrder.UPDATE_TIME;
            default:
                return SearchOrder.SIMILARITY;
        }
    }
}
