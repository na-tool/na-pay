package com.na.pay.dto;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.na.pay.conts.INaPayConst;
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
@ApiModel(value="退款对象", description="退款对象")
public class NaRefundDto {

    /**
     * 订单号   【必填】
     */
    private String orderId;

    /**
     * 订单编号    原订单号  【必填】
     */
    private String refundNo;

    /**
     * 退款金额  【必填】
     */
    private String refundAmount;

    /**
     * 退款原因
     */
    private String refundReason = "退款";

    /**
     * 【描述】退款请求号。
     * 标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。
     * 注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，防止该笔交易重复退款。
     * 支付宝会保证同样的退款请求号多次请求只会退一次。
     * 【必选条件】部分退款时必选
     */
    private String outRequestNo;

//    /**
//     * 真实姓名
//     */
//    private String accountName;
//
//    /**
//     * 账号
//     */
//    private String accountNumber;

    /**
     * 退款类型
     */
    private String refundType = INaPayConst.RefundType.ORIGINAL;

    /**
     * 单据状态
     */
    private NaPayStatus naPayStatus;

    /**
     * 支付宝退款响应
     */
    private AlipayTradeRefundResponse response;

    /**
     * 请求或者响应一些参数  【按需使用】
     */
    private Map<String, Object> params;

    public Boolean aliPayParamsChecked(){
        /**
         * 检查 orderId 是否符合条件
         */
//        boolean isOrderIdValid = orderId != null && orderId.length() <= 64;

        boolean isRefundIdValid = refundNo != null && refundNo.length() <= 64;

        /**
         * 检查 totalAmount 是否符合条件
         */
        boolean isTotalAmountValid = refundAmount != null && refundAmount.length() <= 9 && validateTotalAmount(refundAmount);

        /**
         * 检查 title 是否符合条件，并使用 GlobalPayUtils.isValidString(title) 进行额外验证
         */
        //boolean isRefundReasonValid = refundReason != null && refundReason.length() <= 246;

        /**
         * 返回所有条件的逻辑与
         */
        return isRefundIdValid && isTotalAmountValid;
    }

    public boolean validateTotalAmount(String totalAmount) {
        try {
            double amount = Double.parseDouble(totalAmount);
            return amount >= 0.01;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    /**
//     * 检查姓名
//     */
//    public boolean validateAccountName() {
//        return accountName != null && accountName.length() <= 100;
//    }
//
//    /**
//     * 检查账号
//     */
//    public boolean validateAccountNumber() {
//        return accountNumber != null && accountNumber.length() <= 100;
//    }

    /**
     * 验证退款请求标识（outRequestNo）的有效性。
     * 规则：非空且长度不超过64个字符。
     *
     * @return 如果退款标识有效，返回true；否则返回false。
     */
    public boolean validateOutRequestNo() {
        return outRequestNo != null && outRequestNo.length() <= 64;
    }
}
