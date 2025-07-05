package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXRefundAmountDto {
    /**
     * 【退款金额】 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额。
     * 【必填】
     */
    private Long refund;

    /**
     * 【退款出资账户及金额】退款需从指定账户出资时，可传递该参数以指定出资金额（币种最小单位，仅限整数）。
     * 多账户出资退款需满足：1、未开通退款支出分离功能；2、订单为待分账或分账中的分账订单。
     * 传递参数需确保：1、基本账户可用与不可用余额之和等于退款金额；2、账户类型不重复。不符条件将返回错误。
     * 【可选】
     */
    private List<NaWXRefundFromDto> from;

    /**
     * 【原订单金额】 原支付交易的订单总金额，币种的最小单位，只能为整数
     * 【必填】
     */
    private Long total;

    /**
     * 【退款币种】 符合ISO 4217标准的三位字母代码，固定传：CNY，代表人民币。
     * 【必填】
     */
    private String currency;

    /**
     * 【用户实际支付金额】用户现金支付金额，整型，单位为分，例如10元订单用户使用了2元全场代金券，则该金额为用户实际支付的8元。
     * 【返回必填】
     */
    @JsonProperty("payer_total")
    private Long payerTotal;

    /**
     * 【用户退款金额】 指用户实际收到的现金退款金额，数据类型为整型，单位为分。例如在一个10元的订单中，用户使用了2元的全场代金券，
     * 若商户申请退款5元，则用户将收到4元的现金退款(即该字段所示金额)和1元的代金券退款。
     * 注：部分退款用户无法继续使用代金券，只有在订单全额退款且代金券未过期的情况下，且全场券属于银行立减金用户才能继续使用代金券。
     * 详情参考含优惠退款说明。
     * 【返回必填】
     */
    @JsonProperty("payer_refund")
    private Long payerRefund;

    /**
     * 【应结退款金额】 去掉免充值代金券退款金额后的退款金额，整型，单位为分，
     * 例如10元订单用户使用了2元全场代金券(一张免充值1元 + 一张预充值1元)，商户申请退款5元，则该金额为 退款金额5元 - 0.5元免充值代金券退款金额 = 4.5元。
     * 【返回必填】
     */
    @JsonProperty("settlement_refund")
    private Long settlementRefund;

    /**
     * 【应结订单金额】去除免充值代金券金额后的订单金额，整型，单位为分，
     * 例如10元订单用户使用了2元全场代金券(一张免充值1元 + 一张预充值1元)，则该金额为 订单金额10元 - 免充值代金券金额1元 = 9元。
     * 【返回必填】
     */
    @JsonProperty("settlement_total")
    private Long settlementTotal;

    /**
     * 【优惠退款金额】 申请退款后用户收到的代金券退款金额，整型，单位为分，
     * 例如10元订单用户使用了2元全场代金券，商户申请退款5元，用户收到的是4元现金 + 1元代金券退款金额(该字段) 。
     *
     */
    @JsonProperty("discount_refund")
    private Long discountRefund;

    /**
     * 【手续费退款金额】 订单退款时退还的手续费金额，整型，单位为分，例如一笔100元的订单收了0.6元手续费，商户申请退款50元，该金额为等比退还的0.3元手续费。
     * 【返回选填】
     */
    @JsonProperty("refund_fee")
    private Long refundFee;
}
