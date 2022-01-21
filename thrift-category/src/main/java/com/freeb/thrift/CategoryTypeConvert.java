package com.freeb.thrift;


import java.util.ArrayList;
import java.util.List;

public class CategoryTypeConvert {

    public static CategoryPage ConvertCategoryPageOri2Thr(com.freeb.Entity.CategoryPage page){
        com.freeb.thrift.CategoryPage pg = new com.freeb.thrift.CategoryPage();
        pg.setProdId(page.getProdId());
        pg.setProdName(page.getProdName());
        pg.setProdSales(page.getProdSales());
        pg.setProdImage(page.getProdImage());
        pg.setMerchantId(page.getMerchantId());
        return pg;

    }
    public static ProductInfo ConvertProductInfoOri2Thr(com.freeb.Entity.ProductInfo info){
        ProductInfo pinfo = new ProductInfo();
        pinfo.setProdId(info.getProdId());
        pinfo.setProdName(info.getProdName());
        pinfo.setCategoryId(info.getCategoryId());
        pinfo.setProdSales(info.getProdSales());
        pinfo.setProdRemain(info.getProdRemain());
        pinfo.setProdImages(info.getProdImages());
        pinfo.setDiscountsId(info.getDiscountsId());
        pinfo.setMerchantId(info.getMerchantId());
        //Notice time 没写
        return pinfo;
    }

    public static CommentInfo ConvertCommentInfoOri2Thr(com.freeb.Entity.CommentInfo info){
        CommentInfo cInfo = new CommentInfo();
        cInfo.setCommentId(info.getCommentId());
        cInfo.setUserId(info.getUserId());
        cInfo.setUserName(info.getUserName());
        cInfo.setProdId(info.getProdId());
        cInfo.setCommentDetails(info.getCommentDetails());
        cInfo.setCommentImages(info.getCommentImages());
        return cInfo;
    }

    public static ProductPage ConvertProductPageOri2Thr(com.freeb.Entity.ProductPage page){
        ProductPage pg= new ProductPage();
        pg.setInfo(ConvertProductInfoOri2Thr(page.getInfo()));
        pg.setMerchantId(page.getMerchantId());
        pg.setMerchantName(page.getMerchantName());
        pg.setDiscountVal(page.getDiscountVal());
        pg.setProdComments(ConvertCommentInfoLstOri2Thr(page.getProdComments()));
        return pg;
    }

    public static List<CategoryPage> ConvertCategoryPageLstOri2Thr(List<com.freeb.Entity.CategoryPage> pages){
        List<CategoryPage> lst = new ArrayList<>();
        for(com.freeb.Entity.CategoryPage page:pages){
            lst.add(ConvertCategoryPageOri2Thr(page));
        }
        return lst;

    }

    public static List<ProductPage> ConvertProductPageLstOri2Thr(List<com.freeb.Entity.ProductPage> pages){
        List<ProductPage> lst = new ArrayList<>();
        for(com.freeb.Entity.ProductPage page:pages){
            lst.add(ConvertProductPageOri2Thr(page));
        }
        return lst;

    }

    public static List<ProductInfo> ConvertProducInfoLstOri2Thr(List<com.freeb.Entity.ProductInfo> infos){
        List<ProductInfo> lst = new ArrayList<>();
        for(com.freeb.Entity.ProductInfo info:infos){
            lst.add(ConvertProductInfoOri2Thr(info));
        }
        return lst;
    }

    public static List<CommentInfo> ConvertCommentInfoLstOri2Thr(List<com.freeb.Entity.CommentInfo> infos){
        List<CommentInfo> lst = new ArrayList<>();
        for(com.freeb.Entity.CommentInfo info:infos){
            lst.add(ConvertCommentInfoOri2Thr(info));
        }
        return lst;
    }

    public static SearchType ConvertSearchTypeOri2Thr(com.freeb.Enum.SearchType type){
        switch (type){
            case ACCOUNT_ID:
                return SearchType.ACCOUNT_ID;
            case SHIPPING_ID:
                return SearchType.SHIPPING_ID;
            case OBJ_ID:
                return SearchType.OBJ_ID;
            case MERCHANT_ID:
                return SearchType.MERCHANT_ID;
            case ORDER_ID:
                return SearchType.ORDER_ID;
            case PAYMENT_ID:
                return SearchType.PAYMENT_ID;
            case OBJ_NAME:
                return SearchType.OBJ_NAME;
            case MERCHANT_NAME:
                return SearchType.MERCHANT_NAME;
            default:
                return SearchType.ACCOUNT_ID;
        }
    }

    public static SearchOrder ConvertSearchOrderOri2Thr(com.freeb.Enum.SearchOrder order){
        switch (order){
            case SIMILARITY:
                return SearchOrder.SIMILARITY;
            case UPDATE_TIME:
                return SearchOrder.UPDATE_TIME;
            case SALES:
                return SearchOrder.SALES;
            case PRICE_ASC:
                return SearchOrder.PRICE_ASC;
            case PRICE_DESC:
                return SearchOrder.PRICE_DESC;
            default:
                return SearchOrder.SIMILARITY;
        }
    }


    /* === for client ===*/

    public static com.freeb.Enum.SearchType ConvertSearchTypeThr2Ori(SearchType type){
        switch (type){
            case ACCOUNT_ID:
                return com.freeb.Enum.SearchType.ACCOUNT_ID;
            case SHIPPING_ID:
                return com.freeb.Enum.SearchType.SHIPPING_ID;
            case OBJ_ID:
                return com.freeb.Enum.SearchType.OBJ_ID;
            case MERCHANT_ID:
                return com.freeb.Enum.SearchType.MERCHANT_ID;
            case ORDER_ID:
                return com.freeb.Enum.SearchType.ORDER_ID;
            case PAYMENT_ID:
                return com.freeb.Enum.SearchType.PAYMENT_ID;
            case OBJ_NAME:
                return com.freeb.Enum.SearchType.OBJ_NAME;
            case MERCHANT_NAME:
                return com.freeb.Enum.SearchType.MERCHANT_NAME;
            default:
                return com.freeb.Enum.SearchType.ACCOUNT_ID;
        }
    }
    public static com.freeb.Entity.ProductInfo ConvertProductInfoThr2Ori(ProductInfo info){
        com.freeb.Entity.ProductInfo pinfo = new com.freeb.Entity.ProductInfo();
        pinfo.setProdId(info.getProdId());
        pinfo.setProdName(info.getProdName());
        pinfo.setCategoryId(info.getCategoryId());
        pinfo.setProdSales(info.getProdSales());
        pinfo.setProdRemain(info.getProdRemain());
        pinfo.setProdImages(info.getProdImages());
        pinfo.setDiscountsId(info.getDiscountsId());
        pinfo.setMerchantId(info.getMerchantId());
        //Notice time 没写
        return pinfo;
    }

    public static com.freeb.Entity.MerchantInfo ConvertMerchantInfoThr2Ori(MerchantInfo info){
        com.freeb.Entity.MerchantInfo mInfo = new com.freeb.Entity.MerchantInfo();
        mInfo.setMerchantId(info.getMerchantId());
        mInfo.setMerchantName(info.getMerchantName());
        return mInfo;
    }

    public static com.freeb.Entity.DiscountInfo ConvertDiscountInfoThr2Ori(DiscountInfo info){
        com.freeb.Entity.DiscountInfo dInfo = new com.freeb.Entity.DiscountInfo();
        dInfo.setDiscountId(info.getDiscountId());
        dInfo.setDiscountType(info.getDiscountType());
        dInfo.setDiscountVal(info.getDiscountVal());
        dInfo.setProdId(info.getProdId());
        return dInfo;
    }

    public static com.freeb.Entity.CommentInfo ConvertCommentInfoThr2Ori(CommentInfo info){
        com.freeb.Entity.CommentInfo cInfo = new com.freeb.Entity.CommentInfo();
        cInfo.setCommentId(info.getCommentId());
        cInfo.setUserId(info.getUserId());
        cInfo.setUserName(info.getUserName());
        cInfo.setProdId(info.getProdId());
        cInfo.setCommentDetails(info.getCommentDetails());
        cInfo.setCommentImages(info.getCommentImages());
        return cInfo;
    }


    public static List<com.freeb.Entity.CommentInfo> ConvertCommentInfoLstThr2Ori(List<CommentInfo> infos){
        List<com.freeb.Entity.CommentInfo> lst = new ArrayList<>();
        for(CommentInfo info:infos){
            lst.add(ConvertCommentInfoThr2Ori(info));
        }
        return lst;
    }


}
