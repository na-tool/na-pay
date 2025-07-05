package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXAmountDto {
    /**
     * 【总金额】 订单总金额，单位为分，整型。
     * 示例：1元应填写 100
     * 【必填】
     */
    public Long total;


    /**
     * 【货币类型】符合ISO 4217标准的三位字母代码，固定传：CNY，代表人民币。
     * 【选填】
     */
    @Builder.Default
    public String currency = "CNY";

    /**
     * 【用户支付金额】用户实际支付金额，整型，单位为分，用户支付金额=总金额-代金券金额。
     * 【选填】
     * 【查单的时候响应】
     */
    @JsonProperty("payer_total")
    private Long payerTotal;

    /**
     * 【用户支付币种】 订单支付成功后固定返回：CNY，代表人民币。
     * 【选填】
     * 【查单的时候响应】
     */
    @JsonProperty("payer_currency")
    private String payerCurrency;
}
