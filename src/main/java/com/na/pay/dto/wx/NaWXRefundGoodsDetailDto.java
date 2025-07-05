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
public class NaWXRefundGoodsDetailDto {
    /**
     * 【商户侧商品编码】 订单下单时传入的商户侧商品编码。
     * 【必填】
     */
    @JsonProperty("merchant_goods_id")
    private String merchantGoodsId;

    /**
     * 【微信侧商品编码】 订单下单时传入的微信侧商品编码（没有可不传）
     * 【非必填】
     */
    @JsonProperty("wechatpay_goods_id")
    private String wechatpayGoodsId;

    /**
     * 【商品名称】 订单下单时传入的商品名称。
     * 【非必填】
     */
    @JsonProperty("goods_name")
    private String goodsName;

    /**
     * 【商品单价】 订单下单时传入的商品单价。
     * 【必填】
     */
    @JsonProperty("unit_price")
    private Long unitPrice;

    /**
     * 【商品退款金额】 商品退款金额，单位为分
     * 【必填】
     */
    @JsonProperty("refund_amount")
    private Long refundAmount;

    /**
     * 【商品退货数量】 对应商品的退货数量
     * 【必填】
     */
    @JsonProperty("refund_quantity")
    private Long refundQuantity;
}
