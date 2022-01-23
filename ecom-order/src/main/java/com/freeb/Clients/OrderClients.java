package com.freeb.Clients;

import com.freeb.Entity.*;
import com.freeb.Enum.IdType;
import com.freeb.Enum.PaymentStatus;

public abstract class OrderClients {

    /*==== Accounts ====*/

    public abstract Boolean AccountExists(long accountId);

    public abstract Boolean VerifyAccessByAccount(long accountId, long targetId, IdType idType);

    public abstract AccountInfo GetAccountInfo(Long id);
    /*==== Products ====*/

    public abstract PaymentStatus CheckPaymentStatusById(Long uid, Long paymentId);

    /*
     * Normal Callback. High Concurrency.
     * */
    public abstract Long CreatePayment(PaymentInfo info);

    /*
     * Nested. Long waiting time.
     * */
    public abstract Boolean CancelPayment(Long uid,Long paymentId);

    /*
     * Big data structure.
     * */
    public abstract PaymentInfo GetPaymentInfoById(Long uid, Long paymentId);

    public abstract ProductInfo IncProductSales(Long pid, Integer purchaseNum);

    public abstract MerchantInfo GetMerchantInfoById(Long mid);


    /*==== Discounts ====*/
//    public abstract Double GetProdPrice(long prodId);

    public abstract DiscountInfo GetProdDiscounts(Long prodId, Integer type);

    public abstract CartInfo GetCartInfoById(Long cartId,Long userId);
}
