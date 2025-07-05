package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class NaWXRefundOrderDto {
    /**
     * 【微信支付订单号】 微信支付侧订单的唯一标识，订单支付成功后，查询订单和支付成功回调通知会返回该参数。
     * transaction_id和out_trade_no必须二选一进行传参。
     * 【选填】
     */
    @JsonProperty("transaction_id")
    private String transactionId;

    /**
     * 【商户订单号】 商户下单时传入的商户系统内部订单号。
     * transaction_id和out_trade_no必须二选一进行传参。
     * 【选填】
     */
    @JsonProperty("outTradeNo")
    private String out_trade_no;

    /**
     * 【商户退款单号】 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一商户退款单号多次请求只退一笔。不可超过64个字节数。
     * 【必填】
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    /**
     * 【退款原因】 若商户传了退款原因，该原因将在下发给用户的退款消息中显示，具体展示可参见退款通知UI示意图。
     * 请注意：1、该退款原因参数的长度不得超过80个字节；2、当订单退款金额小于等于1元且为部分退款时，退款原因将不会在消息中体现。
     * 【选填】
     */
    private String reason;

    /**
     * 【退款结果回调url】 异步接收微信支付退款结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     * 如果传了该参数，则商户平台上配置的回调地址（商户平台-交易中心-退款管理-退款配置）将不会生效，优先回调当前传的这个地址。
     * 【选填】
     */
    @JsonProperty("notify_url")
    private String notifyNrl;

    /**
     * 【退款资金来源】 若传递此参数则使用对应的资金账户退款。
     * 【选填】
     */
    @JsonProperty("funds_account")
    private String fundsAccount;

    /**
     * 【金额信息】订单退款金额信息
     * 【必填】
     */
    private NaWXRefundAmountDto amount;

    /**
     * 【退款商品】 请填写需要指定退款的商品信息，
     * 所指定的商品信息需要与下单时传入的单品列表goods_detail中的对应商品信息一致 ，如无需按照指定商品退款，本字段不填。
     * 【选填】
     */
    @JsonProperty("goods_detail")
    private List<NaWXRefundGoodsDetailDto> goodsDetail;


    /**
     * 【微信支付退款单号】申请退款受理成功时，该笔退款单在微信支付侧生成的唯一标识
     * 【返回必填】
     */
    @JsonProperty("refundId")
    private String refund_id;

    /**
     * 【退款渠道】 订单退款渠道
     * 以下枚举：
     * ORIGINAL: 原路退款
     * BALANCE: 退回到余额
     * OTHER_BALANCE: 原账户异常退到其他余额账户
     * OTHER_BANKCARD: 原银行卡异常退到其他银行卡(发起异常退款成功后返回)
     * 【返回必填】
     */
    private String channel;

    /**
     * 【退款入账账户】 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     * 5）退回支付用户银行电子账户:支付用户银行电子账户
     * 6）退回支付用户零花钱:支付用户零花钱
     * 7）退回用户经营账户:用户经营账户
     * 8）退回支付用户来华零钱包:支付用户来华零钱包
     * 9）退回企业支付商户:企业支付商户
     * 【返回必填】
     */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;

    /**
     * 【退款成功时间】
     * 1、定义：退款成功的时间，该字段在退款状态status为SUCCESS（退款成功）时返回。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     * 【返回选填】
     */
    @JsonProperty("success_time")
    private String successTime;

    /**
     * 【退款创建时间】
     * 1、定义：提交退款申请成功，微信受理退款申请单的时间。
     * 2、格式：遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     * 【返回必填】
     */
    @JsonProperty("create_time")
    private String createTime;

    /**
     * 【退款状态】退款单的退款处理状态。
     * SUCCESS: 退款成功
     * CLOSED: 退款关闭
     * PROCESSING: 退款处理中
     * ABNORMAL: 退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台-交易中心，手动处理此笔退款，可参考： 退款异常的处理，或者通过发起异常退款接口进行处理。
     * 注：状态流转说明请参考状态流转图
     * 【返回必填】
     */
    private String status;

    /**
     * 请求或者响应一些参数   【按需使用】
     */
    private Map<String,Object> params;

    @JsonProperty("promotion_detail")
    private List<NaWXRefundPromotionDetailDto> promotionDetail;

    private Response httpResponse;

}
