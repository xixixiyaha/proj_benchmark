package com.freeb.Service;

import com.freeb.Dao.PaymentInfoStorage;
import com.freeb.Entity.PaymentInfo;
import com.freeb.Enum.PaymentStatus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    static String PAYMENT_DB_URL;
    static String PAYMENT_USER;
    static String PAYMENT_PWD;

    private PaymentInfoStorage storage;

    public PaymentServiceImpl() throws ClassNotFoundException {
        storage = new PaymentInfoStorage(PAYMENT_DB_URL,PAYMENT_USER,PAYMENT_PWD);
    }

    private Boolean VerifyAccess2Payment(Long uid,Long pid){
        Long access_id = storage.GetUserIdByPaymentId(pid);
        return uid.equals(access_id);
    }

    @Override
    public PaymentStatus CheckPaymentStatusById(Long uid, Long paymentId) {
        Boolean access = VerifyAccess2Payment(uid,paymentId);
        if(!access){
            logger.warn("unauthorized status check");
            return PaymentStatus.PAYMENT_NO_RECORD;
        }
        Integer status = storage.GetPaymentStatusById(paymentId);
        if(status<0){
            logger.warn("check status failed in DB connection uid="+uid+"payment_id = "+paymentId);
            return PaymentStatus.PAYMENT_NO_RECORD;
        }
        if(status>=PaymentStatus.values().length){
            logger.warn("invalid status enum uid="+uid+"payment_id = "+paymentId+ "invalid status = "+status);
            return PaymentStatus.PAYMENT_NO_RECORD;
        }
        return PaymentStatus.values()[status];
    }

    @Override
    public Long CreatePayment(PaymentInfo info) {
        if(info.getUserId()==null){
            logger.warn("invalid payment info");
            return -1L;
        }
        Long re = storage.CreatePaymentInfo(info);
        if(re<=0){
            logger.warn("get info failed in DB connection uid="+info.getUserId());
        }
        return re;
    }

    @Override
    public Boolean CancelPayment(Long uid, Long paymentId) {
        Boolean access = VerifyAccess2Payment(uid,paymentId);
        if(!access){
            logger.warn("unauthorized status check");
            return false;
        }
        PaymentInfo info = storage.GetPaymentInfoById(paymentId);
        if(info==null){
            logger.warn("get info failed in DB connection uid="+uid+"payment_id = "+paymentId);
            return false;
        }
        info.setPaymentStatus(PaymentStatus.PAYMENT_CANCELLED.ordinal());
        Boolean re = storage.UpdatePaymentStatusById(info);
        if(!re){
            logger.warn("update failed in DB connection uid="+uid+"payment_id = "+paymentId);
            return false;
        }
        return true;
    }


    @Override
    public PaymentInfo GetPaymentInfoById(Long uid, Long paymentId) {
        Boolean access = VerifyAccess2Payment(uid,paymentId);
        if(!access){
            logger.warn("unauthorized status check");
            return null;
        }
        PaymentInfo info = storage.GetPaymentInfoById(paymentId);
        if(info==null){
            logger.warn("get info failed in DB connection uid="+uid+"payment_id = "+paymentId);
            return null;
        }
        return info;
    }
}
