package com.na.pay.dto.wx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXPayerDto {
    /**
     * 【用户标识】用户在商户下单的appid下唯一标识。
     * 【选填】
     */
    private String openid;
}
