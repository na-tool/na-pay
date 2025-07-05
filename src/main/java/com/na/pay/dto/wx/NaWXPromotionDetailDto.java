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
public class NaWXPromotionDetailDto {
    /**
     * 【券ID】 代金券id，微信为代金券分配的唯一标识，创券商户调用发放指定批次的代金券时返回的代金券ID coupon_id。
     * 【必填】
     */
    @JsonProperty("coupon_id")
    private String couponId;

    /**
     * 【优惠名称】 优惠名称，创券商户创建代金券批次时传入的批次名称stock_name。
     * 【必填】
     */
    private String name;

    /**
     * 【优惠范围】优惠活动中代金券的适用范围，分为两种类型：
     * 1、GLOBAL：全场代金券-以订单整体可优惠的金额为优惠门槛的代金券；
     * 2、SINGLE：单品优惠-以订单中具体某个单品的总金额为优惠门槛的代金券
     * 【非必填】
     */
    private String scope;

    /**
     * 【优惠类型】代金券资金类型，优惠活动中代金券的结算资金类型，分为两种类型：
     * 1、CASH：预充值-带有结算资金的代金券，会随订单结算给订单收款商户；
     * 2、NOCASH：免充值-不带有结算资金的代金券，无资金结算给订单收款商户。
     * 【非必填】
     */
    private String type;

    /**
     * 【优惠券面额】代金券优惠的金额。
     * 【必填】
     */
    private Long amount;

    /**
     * 【活动ID】单张代金券所对应的批次号
     * 【非必填】
     */
    @JsonProperty("stock_id")
    private String stockId;

    /**
     * 【微信出资】 代金券有三种出资类型：微信出资、商户出资和其他出资。本参数将返回选择“微信出资类型”时的优惠券面额。
     * 1、创建代金券后默认为商户出资类型。如需使用其他两种类型，请与相关行业运营进行沟通。
     * 2、在 wechatpay_contribute、merchant_contribute 和 other_contribute 这三个字段中，仅有一个字段会返回出资金额。具体返回哪个字段取决于代金券批次的配置。
     * 【非必填】
     */
    @JsonProperty("wechatpay_contribute")
    private Long wechatpayContribute;

    /**
     * 【商户出资】代金券有三种出资类型：微信出资、商户出资和其他出资。本参数将返回选择“商户出资类型”时的优惠券面额。
     * 1、创建代金券后默认为商户出资类型。如需使用其他两种类型，请与相关行业运营进行沟通。
     * 2、在 wechatpay_contribute、merchant_contribute 和 other_contribute 这三个字段中，仅有一个字段会返回出资金额。具体返回哪个字段取决于代金券批次的配置。
     * 【非必填】
     */
    @JsonProperty("merchant_contribute")
    private Long merchantContribute;

    /**
     * 【其他出资】代金券有三种出资类型：微信出资、商户出资和其他出资。本参数将返回选择“其他出资类型”时的优惠券面额。
     * 1、创建代金券后默认为商户出资类型。如需使用其他两种类型，请与相关行业运营进行沟通。
     * 2、在 wechatpay_contribute、merchant_contribute 和 other_contribute 这三个字段中，仅有一个字段会返回出资金额。具体返回哪个字段取决于代金券批次的配置。
     * 【非必填】
     */
    @JsonProperty("other_contribute")
    private Long otherContribute;

    /**
     * 【优惠币种】 代金券金额所对应的货币种类：固定为：CNY，人民币。
     * 【非必填】
     */
    private String currency;

    /**
     * 【单品列表】 单品列表。scope为SINGLE（单品优惠）时返回该参数
     * 【非必填】
     */
    @JsonProperty("goods_detail")
    private List<NaWXGoodsDetailDto> goodsDetail;
}
