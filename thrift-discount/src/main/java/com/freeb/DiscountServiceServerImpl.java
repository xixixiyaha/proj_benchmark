package com.freeb;

import com.freeb.Service.DiscountServiceImpl;
import org.apache.thrift.TException;

public class DiscountServiceServerImpl implements DiscountService.Iface {

    private final com.freeb.Service.DiscountService discService = new DiscountServiceImpl();


    @Override
    public DiscountInfo GetDiscounts(long prodId, int discountType) throws TException {
        return DiscountTypeConvert.DiscountInfoOri2Thr(discService.GetProdDiscounts(prodId,discountType));
    }
}
