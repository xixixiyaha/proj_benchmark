package com.freeb.thrift;

public class PaymentTypeConvert {

    public static PaymentStatus ConvertPaymentStatusOri2Thr(com.freeb.Enum.PaymentStatus status){
        switch (status){
            case PAYMENT_NO_RECORD:
                return PaymentStatus.PAYMENT_NO_RECORD;
            case PAYMENT_ERR:
                return PaymentStatus.PAYMENT_ERR;
            case PAYMENT_START:
                return PaymentStatus.PAYMENT_START;
            case PAYMENT_FINISH:
                return PaymentStatus.PAYMENT_FINISH;
            case PAYMENT_CANCELLED:
                return PaymentStatus.PAYMENT_CANCELLED;
            case PAYMENT_REFUND_ERR:
                return PaymentStatus.PAYMENT_REFUND_ERR;
            case PAYMENT_REFUND_START:
                return PaymentStatus.PAYMENT_REFUND_START;
            case PAYMENT_REFUND_CALLED:
                return PaymentStatus.PAYMENT_REFUND_CALLED;
            case PAYMENT_REFUND_FINISH:
                return PaymentStatus.PAYMENT_REFUND_FINISH;
            default:
                return PaymentStatus.PAYMENT_NO_RECORD;
        }
    }

    public static com.freeb.Entity.PaymentInfo ConvertPaymentInfoThr2Ori(PaymentInfo info){
        com.freeb.Entity.PaymentInfo pInfo = new com.freeb.Entity.PaymentInfo();

        pInfo.setPaymentId(info.getPaymentId());
        pInfo.setPaymentStatus(info.getPaymentStatus());
        pInfo.setPaymentVal(info.getPaymentVal());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setUserId(info.getUserId());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setPaymentCard(info.getPaymentCard());
        //Notice OmitTime
        return pInfo;

    }

    public static PaymentInfo ConvertPaymentInfoOri2Thr(com.freeb.Entity.PaymentInfo info){
        PaymentInfo pInfo = new PaymentInfo();

        pInfo.setPaymentId(info.getPaymentId());
        pInfo.setPaymentStatus(info.getPaymentStatus());
        pInfo.setPaymentVal(info.getPaymentVal());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setUserId(info.getUserId());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setPaymentCard(info.getPaymentCard());
        //Notice OmitTime
        return pInfo;

    }
}
