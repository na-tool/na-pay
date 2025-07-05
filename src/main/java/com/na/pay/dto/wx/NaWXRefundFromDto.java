package com.na.pay.dto.wx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXRefundFromDto {
    /**
     * 【出资账户类型】 退款出资的账户类型。
     * 可选取值：
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     * 【必填】
     */
    private String account;

    /**
     * 【出资金额】对应账户出资金额
     * 【必填】
     */
    private String amount;
}
