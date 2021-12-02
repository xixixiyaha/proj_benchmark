package com.freeb.Service;

import com.freeb.Entity.PaymentInfo;
import com.freeb.Enum.PaymentStatus;

public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentStatus CheckPaymentStatusById(Long paymentId) {
        return null;
    }

    @Override
    public Long CreatePayment(PaymentInfo info) {
        return null;
    }

    @Override
    public Boolean CancelPayment(Long paymentId) {
        return null;
    }

    @Override
    public PaymentInfo GetPaymentInfoById(Long id) {
        return null;
    }
}
