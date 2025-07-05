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
public class NaWXTransferDto {
    /**
     * 【商户单号】 商户系统内部的商家单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一
     * 【必填】
     */
    @JsonProperty("out_bill_no")
    private String outBillNo;

    /**
     * 【转账场景ID】 该笔转账使用的转账场景，可前往“商户平台-产品中心-商家转账”中申请。如：1000（现金营销），1006（企业报销）等
     * 【必填】
     */
    @JsonProperty("transfer_scene_id")
    private String transferSceneId;

    /**
     * 【收款用户OpenID】 用户在商户appid下的唯一标识。发起转账前需获取到用户的OpenID，获取方式详见参数说明。
     * 【必填】
     */
    private String openid;

    /**
     * 【收款用户姓名】 收款方真实姓名。需要加密传入，支持标准RSA算法和国密算法，公钥由微信侧提供。
     * 转账金额 >= 2,000元时，该笔明细必须填写
     * 若商户传入收款用户姓名，微信支付会校验收款用户与输入姓名是否一致，并提供电子回单
     * 【选填】
     */
    @JsonProperty("info_content")
    private String user_name;

    /**
     * 【转账金额】 转账金额单位为“分”。
     * 【必填】
     */
    @JsonProperty("transfer_amount")
    private Long transferAmount;

    /**
     * 【转账备注】 转账备注，用户收款时可见该备注信息，UTF8编码，最多允许32个字符
     * 【必填】
     */
    @JsonProperty("transfer_remark")
    private String transferRemark;

    /**
     * 【通知地址】 异步接收微信支付结果通知的回调地址，通知url必须为公网可访问的URL，必须为HTTPS，不能携带参数。
     * 【选填】
     */
    @JsonProperty("notify_url")
    private String notifyUrl ;

    /**
     * 【用户收款感知】 用户收款时感知到的收款原因将根据转账场景自动展示默认内容。如有其他展示需求，可在本字段传入。
     * 各场景展示的默认内容和支持传入的内容，可查看产品文档了解。
     * 【选填】
     */
    @JsonProperty("user_recv_perception")
    private String userRecvPerception;

    /**
     * 【转账场景报备信息】 各转账场景下需报备的内容，商户需要按照所属转账场景规则传参，详见转账场景报备信息字段说明。
     * 【必填】
     */
    @JsonProperty("transfer_scene_report_infos")
    private List<NaWXTransferSceneReportInfoDto> transferSceneReportInfos;


    /**
     * 【微信转账单号】 微信转账单号，微信商家转账系统返回的唯一标识
     * 【返回必填】
     */
    @JsonProperty("transfer_bill_no")
    private String transferBillNo;

    /**
     * 【单据创建时间】 单据受理成功时返回，按照使用rfc3339所定义的格式，格式为yyyy-MM-DDThh:mm:ss+TIMEZONE
     * 【返回必填】
     */
    @JsonProperty("create_time")
    private String createTime;

    /**
     * 【单据状态】 商家转账订单状态
     * 可选取值
     * ACCEPTED: 转账已受理
     * PROCESSING: 转账锁定资金中。如果一直停留在该状态，建议检查账户余额是否足够，如余额不足，可充值后再原单重试。
     * WAIT_USER_CONFIRM: 待收款用户确认，可拉起微信收款确认页面进行收款确认
     * TRANSFERING: 转账中，可拉起微信收款确认页面再次重试确认收款
     * SUCCESS: 转账成功
     * FAIL: 转账失败
     * CANCELING: 商户撤销请求受理成功，该笔转账正在撤销中
     * CANCELLED: 转账撤销完成
     * 【返回必填】
     */
    private String state;

    /**
     * 【跳转领取页面的package信息】 跳转微信支付收款页的package信息，APP调起用户确认收款或者JSAPI调起用户确认收款 时需要使用的参数。
     * 单据创建后，用户24小时内不领取将过期关闭，建议拉起用户确认收款页面前，先查单据状态：如单据状态为待收款用户确认，可用之前的package信息拉起；单据到终态时需更换单号重新发起转账。
     * 【返回选填】
     */
    @JsonProperty("package_info")
    private String packageInfo;

    private Response httpResponse;

    /**
     * 【商户号】 微信支付分配的商户号
     * 【返回必填】
     */
    @JsonProperty("mch_id")
    private String mchId;

    /**
     * 【商户AppID】 是微信开放平台和微信公众平台为开发者的应用程序(APP、小程序、公众号、企业号corpid即为此AppID)提供的一个唯一标识。
     * 此处，可以填写这四种类型中的任意一种APPID，但请确保该appid与商户号有绑定关系。详见：普通商户模式开发必要参数说明。
     * 【返回必填】
     */
    private String appid;

    /**
     * 【失败原因】 订单已失败或者已退资金时，会返回订单失败原因
     * 【返回选填】
     */
    @JsonProperty("fail_reason")
    private String failReason;

    /**
     * 【最后一次状态变更时间】 单据最后更新时间，按照使用rfc3339所定义的格式，格式为yyyy-MM-DDThh:mm:ss+TIMEZONE
     * 【返回必填】
     */
    @JsonProperty("update_time")
    private String updateTime;


    /**
     * 请求或者响应一些参数   【按需使用】
     */
    private Map<String,Object> params;
}
