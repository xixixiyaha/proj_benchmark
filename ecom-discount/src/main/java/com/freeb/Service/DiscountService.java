package com.freeb.Service;

import com.freeb.Entity.DiscountInfo;

public interface DiscountService {

    public DiscountInfo GetProdDiscounts(Long prodId, Integer type);
}
