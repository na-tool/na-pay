package com.na.pay.conts;

public class NaWXPayConstants {
    public static final String DOMAIN_API = "https://api.mch.weixin.qq.com";

    //app下单
    public static final String PAY_TRANSACTIONS_APP = "/v3/pay/transactions/app";

    //Native下单
    public static final String PAY_TRANSACTIONS_NATIVE = "/v3/pay/transactions/native";

    //JSAPI下单
    public static final String PAY_TRANSACTIONS_JSAPI = "/v3/pay/transactions/jsapi";

    //H5下单
    public static final String PAY_TRANSACTIONS_H5 = "/v3/pay/transactions/h5";

    // 微信支付订单号查询订单
    public static final String PAY_TRANSACTIONS_ID = "/v3/pay/transactions/id/{transaction_id}";

    // 商户订单号查询订单
    public static final String PAY_TRANSACTIONS_OUT_TRADE_NO = "/v3/pay/transactions/out-trade-no/{out_trade_no}";

    //关闭订单
    public static final String PAY_CLOSE_ORDER = "/v3/pay/transactions/out-trade-no/{out_trade_no}/close";

    //申请退款
    public static final String REFUND_DOMESTIC_REFUNDS = "/v3/refund/domestic/refunds";

    // 查询单笔退款（按商户退款单号）
    public static final String REFUND_DOMESTIC_REFUNDS_ID = "/v3/refund/domestic/refunds/{out_refund_no}";

    // 发起转账
    public static final String TRANSFER = "/v3/fund-app/mch-transfer/transfer-bills";

    // 商户单号查询转账单
    public static final String TRANSFER_ID = "/v3/fund-app/mch-transfer/transfer-bills/out-bill-no/{out_bill_no}";

    // 微信单号查询转账单
    public static final String TRANSFER_TRANSACTION_ID = "/v3/fund-app/mch-transfer/transfer-bills/transfer-bill-no/{transfer_bill_no}";

}