package com.na.pay.enums;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public enum NaPayStatus implements INaPayStatusProvider{
    WAIT("1", "wait", "等待支付","pay.wait"),
    PAYING("2", "paying", "支付中","pay.paying"),
    PAY_FAIL("3", "pay_fail", "支付失败", "pay.fail"),
    PAY_SUCCESS("4", "pay_success", "支付成功", "pay.success"),
    CANCEL("5", "cancel", "取消支付", "pay.cancel"),

    REFUND("6", "refund", "退款", "refund.start"),
    REFUND_PROCESS("9", "refund_process", "退款处理中", "refund.processing"),
    REFUND_FAIL("7", "refund_fail", "退款失败", "refund.fail"),
    REFUND_SUCCESS("8", "refund_success", "退款成功", "refund.success"),

    DATA_ERROR("10", "data_error", "数据错误", "data.error"),
    DATA_INVALID("11", "data_invalid", "数据无效", "data.invalid"),
    DATA_VERIFY_FAIL("12", "data_verify_fail", "数据验证失败", "data.verify.fail"),
    DATA_VERIFY_SUCCESS("13", "data_verify_success", "数据验证成功", "data.verify.success"),
    DATA_CONFIG_PARAMS_ERROR("13", "data_config_params_error", "数据配置参数错误", "data.config.params_error"),

    ALIPAY_TRADE_SUCCESS("14", "TRADE_SUCCESS", "支付宝支付成功", "alipay.trade_success"),

    SIGN_VERIFY_FAIL("15", "sign_verify_fail", "签名验证失败", "sign.verify.fail"),
    SIGN_VERIFY_SUCCESS("16", "sign_verify_success", "签名验证成功", "sign.verify.success"),

    UPDATE_ORDER_STATUS("17", "update_order_status", "更新订单状态", "order.update"),
    UPDATE_ORDER_STATUS_FAIL("18", "update_order_status_fail", "更新订单状态失败", "order.update.fail"),
    UPDATE_ORDER_STATUS_SUCCESS("19", "update_order_status_success", "更新订单状态成功", "order.update.success"),

    QUERY_ORDER_STATUS("20", "query_order_status", "查询订单状态", "order.query"),
    QUERY_ORDER_STATUS_FAIL("21", "query_order_status_fail", "查询订单状态失败", "order.query.fail"),
    QUERY_ORDER_STATUS_SUCCESS("22", "query_order_status_success", "查询订单状态成功", "order.query.success"),

    TRANSFER_ORDER("23", "transfer_order", "转账订单", "transfer.start"),
    TRANSFER_ORDER_SUCCESS("24", "transfer_order_success", "转账订单成功", "transfer.success"),
    TRANSFER_ORDER_FAIL("25", "transfer_order_fail", "转账订单失败", "transfer.fail");


    ;
    private final String code;
    private final String enMsg;
    private final String zhMsg;
    private final String msgKey;

    NaPayStatus(String code, String enMsg, String zhMsg, String msgKey) {
        this.code = code;
        this.enMsg = enMsg;
        this.zhMsg = zhMsg;
        this.msgKey = msgKey;
    }


    @Override
    public String code() {
        return code;
    }

    @Override
    public String enMsg() {
        return enMsg;
    }

    @Override
    public String zhMsg() {
        return zhMsg;
    }

    @Override
    public String getMsg() {
        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage())) {
            return this.zhMsg;
        } else {
            return this.enMsg;
        }
    }

    @Override
    public String msgKey() {
        return msgKey;
    }

    @Override
    public String getLanguageMsg() {
        return "";
    }
}
