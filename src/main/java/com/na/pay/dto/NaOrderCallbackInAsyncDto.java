package com.na.pay.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Data
@ApiModel(value="支付渠道对象", description="回调时根据类型处理不同数据")
public class NaOrderCallbackInAsyncDto {

    private HttpServletRequest request;
    private HttpServletResponse response;
    /**
     * 支付渠道
     */
    private String channel;

    /**
     * 订单号
     */
    private String orderId;

    private Map<String, String> paramsMap;
}
