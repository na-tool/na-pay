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
public class NaWXGoodsDetailDto {
    /**
     * 【商户侧商品编码】 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。
     * 【必填】
     */
    @JsonProperty("merchant_goods_id")
    public String merchantGoodsId;

    /**
     * 【微信支付商品编码】 微信支付定义的统一商品编码（没有可不传）
     * 【非必填】
     */
    @JsonProperty("wechatpay_goods_id")
    public String wechatpayGoodsId;

    /**
     * 【商品名称】 商品的实际名称
     * 【非必填】
     */
    @JsonProperty("goods_name")
    public String goodsName;

    /**
     * 【商品数量】 用户购买的数量
     * 【必填】
     */
    public Long quantity;

    /**
     * 【商品单价】整型，单位为：分。如果商户有优惠，需传输商户优惠后的单价
     * (例如：用户对一笔100元的订单使用了商场发的纸质优惠券100-50，则活动商品的单价应为原单价-50)
     * 【必填】
     */
    @JsonProperty("unit_price")
    public Long unitPrice;

    /**
     * 【商品编码】 商品编码。
     * 【必填】
     * 【查询订单】
     */
    @JsonProperty("goods_id")
    private String goodsId;

    /**
     * 【商品优惠金额】 商品优惠金额。
     * 【必填】
     * 【查询订单】
     */
    @JsonProperty("discount_amount")
    private Long discountAmount;

    /**
     * 【商品备注】 商品备注。创券商户在商户平台创建单品券时，若设置了商品备注则会返回。
     * 【非必填】
     * 【查询订单】
     */
    @JsonProperty("goods_remark")
    private String goodsRemark;

    /**
     * 【商品退款金额】 申请退款的商品退款金额
     * 【退款返回必填】
     */
    @JsonProperty("refund_amount")
    private Long refundAmount;

    /**
     * 【商品退货数量】 申请退款的商品退货数量。
     * 【退款返回必填】
     */
    @JsonProperty("refund_quantity")
    private Long refundQuantity;
}
