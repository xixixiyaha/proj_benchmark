package freeb;


import freeb.thrift.CommentInfo;
import freeb.thrift.MerchantInfo;
import freeb.thrift.ProductInfo;
import freeb.thrift.SearchOrder;

import java.util.ArrayList;
import java.util.List;

import static com.freeb.Enum.SearchOrder.SIMILARITY;

public class ProductTypeConvert {

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

    public static MerchantInfo ConvertMerchantInfoOri2Thr(com.freeb.Entity.MerchantInfo info){
        MerchantInfo mInfo = new MerchantInfo();
        mInfo.setMerchantId(info.getMerchantId());
        mInfo.setMerchantName(info.getMerchantName());
        return mInfo;
    }

    public static com.freeb.Enum.SearchOrder ConvertSearchOrderThr2Ori(SearchOrder order){
        switch (order){
            case SIMILARITY:
                return com.freeb.Enum.SearchOrder.SIMILARITY;
            case UPDATE_TIME:
                return com.freeb.Enum.SearchOrder.UPDATE_TIME;
            case SALES:
                return com.freeb.Enum.SearchOrder.SALES;
            case PRICE_ASC:
                return com.freeb.Enum.SearchOrder.PRICE_ASC;
            case PRICE_DESC:
                return com.freeb.Enum.SearchOrder.PRICE_DESC;
            default:
                return com.freeb.Enum.SearchOrder.SIMILARITY;
        }
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
    public static List<CommentInfo> ConvertCommentInfoLstOri2Thr(List<com.freeb.Entity.CommentInfo> infos){
        List<CommentInfo> lst = new ArrayList<>();
        for(com.freeb.Entity.CommentInfo info:infos){
            lst.add(ConvertCommentInfoOri2Thr(info));
        }
        return lst;
    }

}
