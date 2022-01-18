package com.freeb.Service;

import com.freeb.Dao.DiscountStorage;
import com.freeb.Entity.DiscountInfo;

public class DiscountServiceImpl implements DiscountService {

    DiscountStorage storage = new DiscountStorage();


    @Override
    public DiscountInfo GetDiscounts(Long prodId, Integer type) {
        return storage.GetDiscountInfo(prodId,type);
    }
}
