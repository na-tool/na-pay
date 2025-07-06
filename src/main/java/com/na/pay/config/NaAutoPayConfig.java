package com.na.pay.config;

import com.na.pay.utils.NaGlobalPayUtils;
import com.na.pay.utils.NaWXPayUtility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Component
@ConfigurationProperties(prefix = "na.pay")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaAutoPayConfig {
    /**
     * 支付宝配置开始  ----------------------------------------------------------------------
     */
    /**
     * 支付宝 appId  【必填】
     */
    private String aliAppId;
    /**
     * 支付宝 应用私钥  【与证书模式二选一】
     */
    private String aliAppPrivateKey;
    /**
     * 支付宝 支付宝公钥  【与证书模式二选一】
     * 支付宝公钥用于验证支付宝返回的数据签名，以确保数据的真实性和完整性
     */
    private String aliPublicKey;
    /**
     * 支付宝 异步通知地址(异步通知校验更新订单等，注意使用POST请求)  【必填】
     */
    private String aliNotifyUrl;
    /**
     * 支付宝 同步通知地址(同步请求，GET请求，支付成功后跳转的页面)
     */
    private String aliReturnUrl;

    /**
     * 支付宝 证书模式下 应用公钥证书  【与秘钥模式二选一】
     * 例如  certs/ali/appCertPublicKey_1234567899.crt
     */
    private String aliAppCertPublicKey;

    /**
     * 支付宝 证书模式下 支付宝公钥证书 【与秘钥模式二选一】
     * 例如  certs/ali/alipayCertPublicKey_RSA2.crt
     */
    private String aliCertPublicKey;

    /**
     * 支付宝 证书模式下 支付宝根证书   【与秘钥模式二选一】
     * 例如  certs/ali/alipayRootCert.crt
     */
    private String aliRootCert;

    @Builder.Default
    private boolean aliCertProject = true;

    @Builder.Default
    private boolean aliCert = false;

    /**
     * 支付宝 默认
     */
    @Builder.Default
    private String aliSignType = "RSA2";

    /**
     * 支付宝 默认
     */
    @Builder.Default
    private String aliCharset = "utf-8";

    /**
     * 支付宝必填 网关，根据开发环境和生产环境自行填写
     */
    @Builder.Default
    private String aliGatewayUrl = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝 默认
     */
    @Builder.Default
    private String aliFormat = "json";

    /**
     * ------------------------------支付宝配置结束----------------------------------------
     */

    /**
     * ------------------------------微信配置开始----------------------------------------
     */

    /**
     * 商户号
     */
    private String wxMchId;
    /**
     * 开发者ID
     */
    private String wxAppId;
    /**
     * 商户API证书
     */
    private String wxApiCertPath;

    public PrivateKey getPrivateKey() throws IOException {
        return NaWXPayUtility.loadPrivateKeyFromString(NaGlobalPayUtils.readKeyStringFromPath(wxApiCertPath, wxCertProject));
    }

    /**
     * 商户API证书序列号
     */
    private String wxApiSerialNo;
    /**
     * 微信支付公钥
     */
    private String wxPayPublicKeyPath;

    public PublicKey getPayPublicKey() throws IOException {
        return NaWXPayUtility.loadPublicKeyFromString(NaGlobalPayUtils.readKeyStringFromPath(wxPayPublicKeyPath, wxCertProject));
    }

    /**
     * 微信支付公钥ID
     */
    private String wxPayPublicId;
    /**
     * APIv3秘钥
     */
    private String wxApiV3Key;


    private String wxNotifyUrl;

    @Builder.Default
    private boolean wxCertProject = true;

    /**
     * ------------------------------微信配置结束----------------------------------------
     */
}
