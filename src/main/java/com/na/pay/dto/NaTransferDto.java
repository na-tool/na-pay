package com.na.pay.dto;

import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.na.pay.conts.INaPayConst;
import com.na.pay.enums.NaPayStatus;
import com.na.pay.utils.NaGlobalPayUtils;
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
@ApiModel(value="转账对象", description="转账对象")
public class NaTransferDto {
    /**
     * 订单编号 【必填】
     */
    private String orderId;

    /**
     * 转账金额  不能少于0.1元  【必填】
     */
    private String totalAmount;

    /**
     * 转账标题  【必填】
     */
    private String title;

    /**
     * 账户名称  【必填】
     */
    private String accountName;

    /**
     * 账号  支付宝支持邮箱、电话号码  【必填】
     */
    private String accountNumber;

    /**
     * 产品编码  【必填】
     */
    @Builder.Default
    private String productCode = INaPayConst.ProductCode.TRANS_ACCOUNT_NO_PWD;

    /**
     * 身份类型  【必填】
     */
    @Builder.Default
    private String identityType = INaPayConst.IdentityType.ALIPAY_LOGON_ID;

    /**
     * 业务场景 【必填】
     */
    @Builder.Default
    private String bizScene = INaPayConst.BizScene.DIRECT_TRANSFER;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 支付宝转账 响应数据
     */
    private AlipayFundTransUniTransferResponse alipayFundTransUniTransferResponse;

    /**
     * 单据状态
     */
    private NaPayStatus payStatus;

    /**
     * 请求或者响应一些参数  【按需使用】
     */
    private Map<String,Object> params;

    /**
     * 公用回传参数。
     * 如果请求时传递了该参数，支付宝会在异步通知时将该参数原样返回。
     * 本参数必须进行UrlEncode之后才可以发送给支付宝。
     */
    private String encode;

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

        return isOrderIdValid
                && isTotalAmountValid
                && isTitleValid
                && validateProductCode()
                && validateIdentityType()
                && validateAccountName()
                && validateAccountNumber()
                && validateBizScene();
    }

    /**
     * 检查转账金额是否符合条件
     * @param totalAmount
     * @return
     */
    private boolean validateTotalAmount(String totalAmount) {
        try {
            double amount = Double.parseDouble(totalAmount);
            return amount >= 0.1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 检查姓名
     */
    private boolean validateAccountName() {
        return accountName != null && accountName.length() <= 100;
    }

    /**
     * 检查账号
     */
    private boolean validateAccountNumber() {
        return accountNumber != null && accountNumber.length() <= 100;
    }

    /**
     * 检查产品编码不能为空
     */
    private boolean validateProductCode() {
        return productCode != null && productCode.length() > 0;
    }

    /**
     * 检查身份类型不能为空
     */
    private boolean validateIdentityType() {
        return identityType != null && identityType.length() > 0;
    }

    /**
     * 检查场景
     */
    private boolean validateBizScene() {
        return bizScene != null && bizScene.length() > 0;
    }

    /**
     * 验证银行转账参数的有效性。
     * 检查条件包括：
     *  - 银行机构名称（INST_NAME）不为空且长度大于0
     *  - 账户类型（ACCOUNT_TYPE）不为空
     *
     * @return 如果银行转账参数有效，返回true；否则返回false。
     */
    public boolean validateBankTransfer() {
        return params.get(INaPayConst.BankTransferParams.INST_NAME) != null
                && String.valueOf(params.get(INaPayConst.BankTransferParams.INST_NAME)).length() > 0
                && params.get(INaPayConst.BankTransferParams.ACCOUNT_TYPE) != null;
    }
}
