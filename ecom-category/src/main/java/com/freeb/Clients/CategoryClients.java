package com.freeb.Clients;

import com.freeb.Entity.CommentInfo;
import com.freeb.Entity.DiscountInfo;
import com.freeb.Entity.MerchantInfo;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.List;

public abstract class CategoryClients {

    public abstract List<Long> GetRecommendByProdName(Long userId, String words, SearchType type, SearchOrder order);

    public abstract ProductInfo GetProductInfo(Long id);

    public abstract List<CommentInfo> GetComments(Long prodId, Integer comtNum);

    public abstract DiscountInfo GetDiscounts(Long prodId, Integer type);

    public abstract MerchantInfo GetMerchantInfoByProd(Long pid);
}
