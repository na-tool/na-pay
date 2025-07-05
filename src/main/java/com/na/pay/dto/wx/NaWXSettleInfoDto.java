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
public class NaWXSettleInfoDto {
    /**
     * 【分账标识】订单的分账标识在下单时设置，传入true表示在订单支付成功后可进行分账操作。以下是详细说明：
     *
     * 需要分账（传入true）：
     * 订单收款成功后，资金将被冻结并转入基本账户的不可用余额。商户可通过请求分账API，将收款资金分配给其他商户或用户。完成分账操作后，可通过接口解冻剩余资金，或在支付成功30天后自动解冻。
     *
     * 不需要分账（传入false或不传，默认为false）：
     * 订单收款成功后，资金不会被冻结，而是直接转入基本账户的可用余额。
     *
     *【选填】
     */
    @JsonProperty("profit_sharing")
    @Builder.Default
    public Boolean profitSharing = false;
}
