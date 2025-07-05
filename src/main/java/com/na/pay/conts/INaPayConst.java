package com.na.pay.conts;

public interface INaPayConst {
    interface ProductCode{
        /**
         * 收发现金红包固定为：STD_RED_PACKET
         */
        String STD_RED_PACKET = "STD_RED_PACKET";

        /**
         * 单笔无密转账到支付宝账户固定为：TRANS_ACCOUNT_NO_PWD
         */
        String TRANS_ACCOUNT_NO_PWD = "TRANS_ACCOUNT_NO_PWD";

        /**
         * 单笔无密转账到银行卡固定为：TRANS_BANKCARD_NO_PWD
         */
        String TRANS_BANKCARD_NO_PWD = "TRANS_BANKCARD_NO_PWD";

        String QUICK_WAP_WAY = "QUICK_WAP_WAY";

        String QUICK_MSECURITY_PAY = "QUICK_MSECURITY_PAY";

        String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";
    }

    interface BizScene{
        /**
         * 单笔无密转账到支付宝,B2C现金红包
         */
        String DIRECT_TRANSFER = "DIRECT_TRANSFER";

        /**
         * C2C现金红包-领红包
         */
        String PERSONAL_COLLECTION = "PERSONAL_COLLECTION";
    }

    interface IdentityType{
        /**
         * 支付宝的会员ID
         */
        String ALIPAY_USER_ID = "ALIPAY_USER_ID";

        /**
         * 支付宝登录号，支持邮箱和手机号格式
         */
        String ALIPAY_LOGON_ID = "ALIPAY_LOGON_ID";

        /**
         * 支付宝 银行卡
         */
        String BANKCARD_ACCOUNT = "BANKCARD_ACCOUNT";
    }

    interface BankTransferParams{
        String INST_NAME = "inst_name";
        String ACCOUNT_TYPE = "account_type";
    }

    interface BankAccountType{
        /**
         * 企业对公账户
         */
        String BUSINESS_PAYMENT = "1";
        /**
         * 个人对公账户
         */
        String PERSONAL_PAYMENT = "2";
    }

    interface RefundType{
        /**
         * 原路退回
         */
        String ORIGINAL = "ORIGINAL";

        /**
         * 部分退款
         */
        String PARTIAL_REFUND = "PARTIAL_REFUND";
    }

    interface Channel{
        String ALIPAY = "alipay";
        String WECHAT = "wechat";
        String UNIONPAY = "unionpay";
        String BANK = "bank";

    }

    interface Key{
        String PASSBACK_PARAMS = "passback_params";
    }
}
