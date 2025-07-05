package com.na.pay.dto;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.na.pay.enums.NaPayStatus;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="查询单对象", description="查询单对象")
public class NaOrderQueryDto {

    /**
     * 订单编号 【必填】
     */
    private String orderId;

    /**
     * 支付宝查询订单 响应数据
     */
    private AlipayTradeQueryResponse alipayTradeQueryResponse;

    /**
     * 单据状态
     */
    private NaPayStatus naPayStatus;

    /**
     * 请求或者响应一些参数  【按需使用】
     */
    private Map<String,Object> params;

    public Boolean aliPayParamsChecked(){
        /**
         * 检查 orderId 是否符合条件
         */
        return orderId != null && orderId.length() <= 64;
    }
}
