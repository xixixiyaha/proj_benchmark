package com.freeb.thrift;

import com.freeb.Service.PaymentServiceImpl;
import org.apache.thrift.TException;

public class PaymentServiceServerImpl implements PaymentService.Iface {
    private final com.freeb.Service.PaymentService payService = new PaymentServiceImpl();

    public PaymentServiceServerImpl() throws ClassNotFoundException {
    }


    @Override
    public PaymentStatus CheckPaymentStatusById(long uid, long paymentId) throws TException {
        return PaymentTypeConvert.ConvertPaymentStatusOri2Thr(payService.CheckPaymentStatusById(uid,paymentId));
    }

    @Override
    public long CreatePayment(PaymentInfo info) throws TException {
        return payService.CreatePayment(PaymentTypeConvert.ConvertPaymentInfoThr2Ori(info));
    }

    @Override
    public boolean CancelPayment(long uid, long paymentId) throws TException {
        return payService.CancelPayment(uid,paymentId);
    }

    @Override
    public PaymentInfo GetPaymentInfoById(long uid, long paymentId) throws TException {
        return PaymentTypeConvert.ConvertPaymentInfoOri2Thr(payService.GetPaymentInfoById(uid,paymentId));
    }
}
