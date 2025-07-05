package com.na.pay.dto;

import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.na.pay.enums.NaPayStatus;
import com.na.pay.utils.NaGlobalPayUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaOrderPayDto {
    /**
     * 订单编号 【必填】
     */
    private String orderId;

    /**
     * 订单金额  【必填】
     */
    private String totalAmount;



    /**
     * 商品名称 【必填】
     */
    private String title;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 支付宝订单过期时间
     */
    private String timeoutExpress = "30m";

    /**
     * 支付时间戳(单位:秒)
     */
    private String timeStamp;

    /**
     * 单据状态
     */
    private NaPayStatus naPayStatus;

    /**
     * 请求或者响应一些参数   【按需使用】
     */
    private Map<String,Object> params;

    /**
     * 公用回传参数。
     * 如果请求时传递了该参数，支付宝会在异步通知时将该参数原样返回。
     * 本参数必须进行UrlEncode之后才可以发送给支付宝。
     */
    private String encode;

    /**
     * 支付宝下单 响应数据
     */
    /**
     * App
     */
    private AlipayTradeAppPayResponse alipayTradeAppPayResponse;

    /**
     * H5
     */
    private AlipayTradeWapPayResponse alipayTradeWapPayResponse;

    /**
     * PC
     */
    private AlipayTradePagePayResponse alipayTradePagePayResponse;

    @JsonIgnore
    private HttpServletRequest request;
    @JsonIgnore
    private HttpServletResponse response;

    public Boolean aliPayParamsChecked(){
        /**
         * 检查 orderId 是否符合条件
         */
        boolean isOrderIdValid = orderId != null && orderId.length() <= 64;

        /**
         * 检查 totalAmount 是否符合条件
         */
        boolean isTotalAmountValid = totalAmount != null && totalAmount.length() <= 9 && validateTotalAmount(totalAmount);

        /**
         * 检查 title 是否符合条件，并使用 GlobalPayUtils.isValidString(title) 进行额外验证
         */
        boolean isTitleValid = title != null && title.length() <= 246 && NaGlobalPayUtils.isValidString(title);

        /**
         * 返回所有条件的逻辑与
         */
        return isOrderIdValid && isTotalAmountValid && isTitleValid;
    }

    public Boolean alipayResponseChecked(){
        return response != null;
    }

    public boolean validateTotalAmount(String totalAmount) {
        try {
            double amount = Double.parseDouble(totalAmount);
            return amount >= 0.01;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
