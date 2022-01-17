package com.freeb.Service;

import com.freeb.Entity.DiscountInfo;

public interface DiscountsService {

    public DiscountInfo GetDiscounts(Long prodId, Integer type);
}
