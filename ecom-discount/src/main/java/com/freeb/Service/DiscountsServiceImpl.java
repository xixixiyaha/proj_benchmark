package com.freeb.Service;

import com.freeb.Dao.DiscountsStorage;
import com.freeb.Entity.DiscountInfo;

public class DiscountsServiceImpl implements DiscountsService {

    DiscountsStorage storage = new DiscountsStorage();


    @Override
    public DiscountInfo GetDiscounts(Long prodId, Integer type) {
        return storage.GetDiscountInfo(prodId,type);
    }
}
