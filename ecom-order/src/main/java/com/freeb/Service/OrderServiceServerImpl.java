package com.freeb.Service;

import com.freeb.Clients.OrderClients;
import com.freeb.Dao.OrderInfoStorage;
import com.freeb.Entity.*;
import com.freeb.Enum.IdType;
import com.freeb.Orders.Orders;
import com.freeb.Utils.PackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderServiceServerImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceServerImpl.class);
    // TODO 初始化
    private OrderClients client;

    private Orders orders = new Orders();
    private OrderInfoStorage storage;

    public OrderServiceServerImpl(OrderClients c){
        storage = new OrderInfoStorage();
        this.client = c;
    }

    @Override
    public OrderResp GetOrderListByUserId(OrderReq orderReq){
        OrderResp orderResp = new OrderResp();

        if (!client.AccountExists(orderReq.getUserId())){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }

        List<OrderInfo> infos = orders.getOrderListByAccountId(orderReq.getUserId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }

        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        //TODO has more
        return orderResp;
    }

    @Override
    public OrderResp GetOrderByPaymentId(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        if (!client.VerifyAccessByAccount(orderReq.getUserId(),orderReq.getPaymentId(), IdType.PAYMENT_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        List<OrderInfo> infos = orders.getOrderByPaymentId(orderReq.getUserId(),orderReq.getPaymentId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        return orderResp;
    }

//    @Override
//    public OrderResp CreatePaymentByOrderId(OrderReq orderReq) {
//        OrderResp orderResp = new OrderResp();
//        orderResp.setHasMore(false);
//        if (!client.VerifyAccessByAccount(orderReq.getUserId(),orderReq.getOrderId(), IdType.ORDER_ID)){
//            orderResp.setBaseResp(PackResponse.packNoAuthority());
//            return orderResp;
//        }
//        AccountInfo aInfo = client.GetAccountInfo(orderReq.getUserId());
//        if(aInfo ==null){
//            logger.warn("create order failed OrderReq="+orderReq.toString());
//            orderResp.setBaseResp(PackResponse.packUnknownFailure());
//            return orderResp;
//        }
//        Double prodVal= client.GetProdPrice(orderReq.getProdId());
//        Double discountsVal= client.GetProdDiscounts(orderReq.getProdId(),0).getDiscountVal();
//
//        PaymentInfo payInfo = new PaymentInfo(prodVal,discountsVal,aInfo.getUserCard(),orderReq.getUserId());
//        Long paymentId = client.CreatePayment(payInfo);
//        if(paymentId ==-1L){
//            logger.warn("create payment failed OrderReq="+orderReq.toString());
//            //TODO 升级resp类型
//            orderResp.setBaseResp(PackResponse.packUnknownFailure());
//            return orderResp;
//        }
//        Boolean re =storage.UpdatePaymentIdByOrderId(orderReq.getOrderId(),paymentId);
//        //update order's paymentID
//        List<OrderInfo> infos = storage.getOrderInfoByOrderId(orderReq.getOrderId());
//        orderResp.setBaseResp(PackResponse.packSuccess());
//        orderResp.setOrderInfos(infos);
//        return orderResp;
//    }

    @Override
    public OrderResp GetOrderByOrderId(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        if (!client.VerifyAccessByAccount(orderReq.getUserId(),orderReq.getOrderId(), IdType.ORDER_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        List<OrderInfo> infos = orders.getOrderByOrderId(orderReq.getUserId(),orderReq.getOrderId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }

        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        return orderResp;
    }

    @Override
    public OrderResp CreateOrderByCartInfo(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        AccountInfo aInfo = client.GetAccountInfo(orderReq.getUserId());
        if(aInfo ==null){
            logger.warn("create order failed OrderReq="+orderReq.toString());
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        // 1， user合法
        if (!client.AccountExists(orderReq.getUserId())){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        // 2. product 有余量 + Fetch Name + Fetch 商家名字
        CartInfo cInfo = orderReq.getCartInfo();
        if(cInfo==null){
            logger.warn("create order failed cInfo = NULL OrderReq="+orderReq.toString());
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        ProductInfo pInfo = client.IncProductSales(cInfo.getProdId(),cInfo.getIncartQuantity());
        if(pInfo==null){
            logger.warn("create order failed pInfo = NULL OrderReq="+orderReq.toString());
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        MerchantInfo mInfo = client.GetMerchantInfoById(pInfo.getMerchantId());
        if(mInfo==null){
            logger.warn("create order failed mInfo = NULL OrderReq="+orderReq.toString());
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        DiscountInfo dInfo = client.GetProdDiscounts(pInfo.getProdId(),0);
        if(dInfo==null){
            logger.warn("create order failed dInfo = NULL OrderReq="+orderReq.toString());
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        // 3. Create Order
        Long orderId = storage.CreateOrderInfoByCartInfo(cInfo.getUserId(),mInfo.getMerchantId(),mInfo.getMerchantName(),pInfo.getProdId(),pInfo.getProdName(),cInfo.getCartId());
        if(orderId==-1){
            logger.warn("create order failed");
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        // 4. Create Payment By order
        PaymentInfo payInfo = new PaymentInfo(cInfo.getIncartQuantity()*pInfo.getProdPrice(),cInfo.getIncartQuantity()*dInfo.getDiscountVal(),aInfo.getUserCard(),orderReq.getUserId());
        Long paymentId = client.CreatePayment(payInfo);
        // 5. 返回orderInfo
        if(!storage.UpdatePaymentIdByOrderId(orderId,paymentId)){
            logger.warn("create order failed Update Payment"+payInfo.getPaymentId());
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }

        orderResp.setOrderInfos(storage.getOrderInfoByOrderId(orderId));
        orderResp.setBaseResp(PackResponse.packSuccess());
        return orderResp;
    }


}
