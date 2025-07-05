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
public class NaWXDetailDto {
    /**
     * 【订单原价】
     * 1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
     * 2、当订单原价与支付金额不相等，则不享受优惠。
     * 3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。
     * 【选填】
     */
    @JsonProperty("cost_price")
    public Long costPrice;

    /**
     * 【商品小票ID】 商家小票ID
     * 【选填】
     */
    @JsonProperty("invoice_id")
    public String invoiceId;

    /**
     * 【单品列表】 单品列表信息
     * 条目个数限制：【1，6000】
     * 【选填】
     */
    @JsonProperty("goods_detail")
    public List<NaWXGoodsDetailDto> goodsDetail;
}
