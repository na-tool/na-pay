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
public class NaWXRefundPromotionDetailDto {
    /**
     * 【券ID】代金券id，单张代金券的编号
     * 【返回必填】
     */
    @JsonProperty("promotion_id")
    private String promotionId;

    /**
     * 【优惠范围】优惠活动中代金券的适用范围，分为两种类型：
     * GLOBAL：全场代金券-以订单整体可优惠的金额为优惠门槛的代金券；
     * SINGLE：单品优惠-以订单中具体某个单品的总金额为优惠门槛的代金券
     * 【返回必填】
     */
    private String scope;

    /**
     * 【优惠类型】代金券资金类型，优惠活动中代金券的结算资金类型，分为两种类型：
     * CASH：预充值-带有结算资金的代金券，会随订单结算给订单收款商户；
     * NOCASH：免充值-不带有结算资金的代金券，无资金结算给订单收款商户。
     * 【返回必填】
     */
    private String type;

    /**
     * 【代金券面额】 代金券优惠的金额
     * 【返回必填】
     */
    private Long amount;

    /**
     * 【优惠退款金额】 代金券退款的金额
     * 【返回必填】
     */
    @JsonProperty("refund_amount")
    private Long refundAmount;

    /**
     * 【退款商品】 指定商品退款时传的退款商品信息。
     * 【返回选填】
     */
    @JsonProperty("goods_detail")
    private List<NaWXGoodsDetailDto> goodsDetail;
}
