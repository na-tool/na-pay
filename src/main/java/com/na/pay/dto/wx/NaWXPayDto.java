package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.na.pay.enums.NaPayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Response;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXPayDto {
    /**
     * 【商户应用ID】APPID是商户移动应用唯一标识，在开放平台(移动应用)申请。此处需填写与mchid完成绑定的appid，详见：商户模式开发必要参数说明。
     * 【必填】
     */
    public String appid;

    /**
     * 【商户号】是由微信支付系统生成并分配给每个商户的唯一标识符，商户号获取方式请参考商户模式开发必要参数说明。
     * 【必填】
     */
    public String mchid;

    /**
     * 【商品描述】商品信息描述，用户微信账单的商品字段中可见(可参考APP支付示例说明-账单示意图)，商户需传递能真实代表商品信息的描述，不能超过127个字符。
     * 【必填】
     */
    public String description;

    /**
     * 【商户订单号】商户系统内部订单号，要求6-32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。
     */
    @JsonProperty("out_trade_no")
    public String outTradeNo;

    /**
     * 【支付结束时间】
     * 1、定义：支付结束时间是指用户能够完成该笔订单支付的最后时限，并非订单关闭的时间。超过此时间后，用户将无法对该笔订单进行支付。如需关闭订单，请调用关闭订单API接口。
     * 2、格式要求：支付结束时间需遵循rfc3339标准格式：yyyy-MM-DDTHH:mm:ss+TIMEZONE。yyyy-MM-DD 表示年月日；T 字符用于分隔日期和时间部分；HH:mm:ss 表示具体的时分秒；TIMEZONE 表示时区（例如，+08:00 对应东八区时间，即北京时间）。
     * 示例：2015-05-20T13:29:35+08:00 表示北京时间2015年5月20日13点29分35秒。
     * 3、注意事项：
     * time_expire 参数仅在用户首次下单时可设置，且不允许后续修改，尝试修改将导致错误。
     * 若用户实际进行支付的时间超过了订单设置的支付结束时间，商户需使用新的商户订单号下单，生成新的订单供用户进行支付。若未超过支付结束时间，则可使用原参数重新请求下单接口，以获取当前订单最新的prepay_id 进行支付。
     * 支付结束时间不能早于下单时间后1分钟，若设置的支付结束时间早于该时间，系统将自动调整为下单时间后1分钟作为支付结束时间。
     * 【选填】
     */
    @JsonProperty("time_expire")
    public String timeExpire;

    /**
     * 【商户数据包】商户在创建订单时可传入自定义数据包，该数据对用户不可见，
     * 用于存储订单相关的商户自定义信息，其总长度限制在128字符以内。
     * 支付成功后查询订单API和支付成功回调通知均会将此字段返回给商户，并且该字段还会体现在交易账单。
     * 【选填】
     */
    public String attach;

    /**
     * 【商户回调地址】商户接收支付成功回调通知的地址，需按照notify_url填写注意事项规范填写。
     * 【必填】
     */
    @JsonProperty("notify_url")
    public String notifyUrl;

    /**
     * 【订单优惠标记】代金券在创建时可以配置多个订单优惠标记，标记的内容由创券商户自定义设置。详细参考：创建代金券批次API。
     * 如果代金券有配置订单优惠标记，则必须在该参数传任意一个配置的订单优惠标记才能使用券。如果代金券没有配置订单优惠标记，则可以不传该参数。
     * 示例：
     * 如有两个活动，活动A设置了两个优惠标记：WXG1、WXG2；活动B设置了两个优惠标记：WXG1、WXG3；
     * 下单时优惠标记传WXG2，则订单参与活动A的优惠；
     * 下单时优惠标记传WXG3，则订单参与活动B的优惠；
     * 下单时优惠标记传共同的WXG1，则订单参与活动A、B两个活动的优惠；
     * 【选填】
     */
    @JsonProperty("goods_tag")
    public String goodsTag;

    /**
     * 【电子发票入口开放标识】 传入true时，支付成功消息和支付详情页将出现开票入口。
     * 需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效。 详细参考：电子发票介绍
     * true：是
     * false：否
     * 【选填】
     */
    @JsonProperty("support_fapiao")
    @Builder.Default
    public Boolean supportFapiao = false;

    /**
     * 【订单金额】订单金额信息
     * 【必填】
     */
    public NaWXAmountDto amount;

    /**
     * 【优惠功能】 优惠功能
     * 【选填】
     */
    @JsonProperty("detail")
    public NaWXDetailDto detail;

    /**
     * 【场景信息】 场景信息
     * 【选填】
     */
    @JsonProperty("scene_info")
    public NaWXSceneInfoDto sceneInfo;

    /**
     * 【结算信息】 结算信息
     * 【选填】
     */
    @JsonProperty("settle_info")
    public NaWXSettleInfoDto settleInfo;

    /**
     * 请求或者响应一些参数   【按需使用】
     */
    private Map<String,Object> params;

    /**
     * ------------------------返回信息---------------------
     */

    /**
     * App
     */
    @JsonProperty("prepay_id")
    private String prepayId;

    /**
     * Native
     */
    @JsonProperty("code_url")
    private String codeUrl;

    /**
     * H5
     */
    @JsonProperty("h5_url")
    private String h5Url;

    private Response httpResponse;

    /**
     * 单据状态
     */
    private NaPayStatus naPayStatus;
}
