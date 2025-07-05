package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.na.pay.enums.NaPayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Response;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXOrderQueryDto {
    /**
     * 【公众账号ID】商户下单时传入的公众账号ID。
     * 【必填】
     */
    private String appid;

    /**
     * 【商户号】商户下单时传入的商户号。
     * 【必填】
     */
    private String mchid;

    /**
     * 【商户订单号】商户下单时传入的商户系统内部订单号。
     * 【必填】
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /**
     * 【微信支付订单号】 微信支付侧订单的唯一标识。
     * 【必填】
     */
    @JsonProperty("transaction_id")
    private String transactionId;

    /**
     * 【交易类型】 返回当前订单的交易类型，枚举值：
     * JSAPI：公众号支付、小程序支付
     * NATIVE：Native支付
     * APP：APP支付
     * MICROPAY：付款码支付
     * MWEB：H5支付
     * FACEPAY：刷脸支付
     * 【必填】
     */
    @JsonProperty("trade_type")
    private String tradeType;

    /**
     * 【交易状态】 返回订单当前交易状态。详细业务流转状态处理请参考开发指引-订单状态流转图。枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（仅付款码支付会返回）
     * USERPAYING：用户支付中（仅付款码支付会返回）
     * PAYERROR：支付失败（仅付款码支付会返回）
     * 【必填】
     */
    @JsonProperty("trade_state")
    private String tradeState;

    /**
     * 【交易状态描述】 对交易状态的详细说明。
     * 【必填】
     */
    @JsonProperty("trade_state_desc")
    private String tradeStateDesc;

    /**
     * 【银行类型】 用户支付方式说明，订单支付成功后返回，
     * 格式为银行简码_具体类型(DEBIT借记卡/CREDIT信用卡/ECNY数字人民币)，
     * 例如ICBC_DEBIT代表工商银行借记卡，非银行卡支付类型(例如余额/零钱通等)统一为OTHERS，具体请参考《银行类型对照表》。
     * 【可选】
     */
    @JsonProperty("bank_type")
    private String bankType;

    /**
     * 【商户数据包】商户下单时传入的自定义数据包，用户不可见，长度不超过128字符，
     * 若下单传入该参数，则订单支付成功后此接口和支付成功回调通知以及交易账单中会原样返回；若下单未传该参数，则不会返回。
     * 【可选】
     */
    private String attach;

    /**
     * 【支付完成时间】
     * 1、定义：用户完成订单支付的时间。该参数在订单支付成功后返回。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。
     * yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     * 【可选】
     */
    @JsonProperty("success_time")
    private String successTime;

    /**
     * 【支付者信息】 订单的支付者信息。
     * 【可选】
     */
    private NaWXPayerDto payer;

    /**
     * 【订单金额】 订单金额信息。
     * 【可选】
     */
    private NaWXAmountDto amount;

    /**
     * 【场景信息】 下单时传入的支付场景描述，若下单传入该参数，则原样返回；若下单未传该参数，则不会返回。
     * 【可选】
     */
    @JsonProperty("scene_info")
    private NaWXSceneInfoDto sceneInfo;

    /**
     * 【优惠功能】 代金券信息，当订单有使用代金券时，该字段将返回所使用的代金券信息。
     * 【可选】
     */
    @JsonProperty("promotion_detail")
    private List<NaWXPromotionDetailDto> promotionDetail;


    private Response httpResponse;

    /**
     * 单据状态
     */
    private NaPayStatus naPayStatus;

    /**
     * 请求或者响应一些参数   【按需使用】
     */
    private Map<String,Object> params;
}
