package com.na.pay.enums;

public interface INaPayStatusProvider {
    String code();
    String enMsg();
    String zhMsg();
    String getMsg();

    String msgKey();
    String getLanguageMsg();
}
