package com.freeb.Service;

import com.freeb.Entity.DiscountInfo;

public interface DiscountService {

    public DiscountInfo GetDiscounts(Long prodId, Integer type);
}
