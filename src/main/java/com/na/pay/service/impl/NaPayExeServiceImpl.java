package com.na.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.na.common.utils.NaObjConversionUtil;
import com.na.pay.config.NaAutoPayConfig;
import com.na.pay.conts.INaPayConst;
import com.na.pay.conts.NaWXPayConstants;
import com.na.pay.dto.*;
import com.na.pay.dto.wx.NaWXOrderQueryDto;
import com.na.pay.dto.wx.NaWXPayDto;
import com.na.pay.dto.wx.NaWXRefundOrderDto;
import com.na.pay.dto.wx.NaWXTransferDto;
import com.na.pay.enums.NaPayStatus;
import com.na.pay.service.INaPayExeService;
import com.na.pay.utils.NaGlobalPayUtils;
import com.na.pay.utils.NaWXPayUtility;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付执行服务实现类
 */
@Slf4j
@Service
public class NaPayExeServiceImpl implements INaPayExeService {
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    @Autowired
    private NaAutoPayConfig autoPayConfig;

    /**
     * APP 支付请求
     */
    @Override
    public NaOrderPayDto payApp(NaOrderPayDto naOrderPayDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException {
        naAutoPayConfig = (naAutoPayConfig != null) ? naAutoPayConfig : autoPayConfig;
        if (naOrderPayDto == null || !naOrderPayDto.aliPayParamsChecked()) {
            naOrderPayDto.setNaPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
            return naOrderPayDto;
        }

        AlipayClient alipayClient = createClient(naAutoPayConfig);

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(naOrderPayDto.getTitle());
        model.setBody(StringUtils.defaultIfEmpty(naOrderPayDto.getBody(), naOrderPayDto.getTitle()));
        model.setOutTradeNo(naOrderPayDto.getOrderId());
        model.setTimeoutExpress(naOrderPayDto.getTimeoutExpress());
        model.setTotalAmount(naOrderPayDto.getTotalAmount());
        model.setProductCode(INaPayConst.ProductCode.QUICK_MSECURITY_PAY);

        if (naOrderPayDto.getParams() != null && naOrderPayDto.getParams().containsKey(INaPayConst.Key.PASSBACK_PARAMS)) {
            model.setPassbackParams(String.valueOf(naOrderPayDto.getParams().get(INaPayConst.Key.PASSBACK_PARAMS)));
        }

        // 处理回传参数并编码
        model.setPassbackParams(encodePassback(naOrderPayDto.getEncode()));

        request.setBizModel(model);
        request.setNotifyUrl(naAutoPayConfig.getAliNotifyUrl());
        request.setReturnUrl(naAutoPayConfig.getAliReturnUrl());

        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        naOrderPayDto.setAlipayTradeAppPayResponse(response);
        naOrderPayDto.setTimeStamp(NaGlobalPayUtils.getCurrentTimestampInSecondsAsString());
        naOrderPayDto.setNaPayStatus(NaPayStatus.WAIT);
        return naOrderPayDto;
    }

    /**
     * 网页支付
     */
    @Override
    public NaOrderPayDto payWeb(NaOrderPayDto orderPayDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException {
        naAutoPayConfig = (naAutoPayConfig != null) ? naAutoPayConfig : autoPayConfig;
        if (orderPayDto == null || !orderPayDto.aliPayParamsChecked() || !orderPayDto.alipayResponseChecked()) {
            orderPayDto.setNaPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
            throw new RuntimeException(NaPayStatus.DATA_VERIFY_FAIL.getMsg());
        }

        AlipayClient alipayClient = createClient(naAutoPayConfig);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setSubject(orderPayDto.getTitle());
        model.setBody(StringUtils.defaultIfEmpty(orderPayDto.getBody(), orderPayDto.getTitle()));
        model.setOutTradeNo(orderPayDto.getOrderId());
        model.setTimeoutExpress(orderPayDto.getTimeoutExpress());
        model.setTotalAmount(orderPayDto.getTotalAmount());
        model.setProductCode(INaPayConst.ProductCode.FAST_INSTANT_TRADE_PAY);
        model.setPassbackParams(encodePassback(orderPayDto.getEncode()));

        request.setBizModel(model);
        request.setNotifyUrl(naAutoPayConfig.getAliNotifyUrl());
        request.setReturnUrl(naAutoPayConfig.getAliReturnUrl());

        AlipayTradePagePayResponse alipayTradePagePayResponse = alipayClient.pageExecute(request);
        orderPayDto.setAlipayTradePagePayResponse(alipayTradePagePayResponse);
        return orderPayDto;

//        String form = alipayClient.pageExecute(request).getBody();
//        HttpServletResponse response = orderPayDto.getResponse();
//        response.setContentType("text/html;charset=UTF-8");
//        response.getWriter().write(form);
//        response.getWriter().flush();
//        response.getWriter().close();
    }

    /**
     * H5支付
     */
    @Override
    public NaOrderPayDto payH5(NaOrderPayDto orderPayDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException {
        naAutoPayConfig = (naAutoPayConfig != null) ? naAutoPayConfig : autoPayConfig;
        if (orderPayDto == null || !orderPayDto.aliPayParamsChecked() || !orderPayDto.alipayResponseChecked()) {
            orderPayDto.setNaPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
            throw new RuntimeException(NaPayStatus.DATA_VERIFY_FAIL.getMsg());
        }

        AlipayClient alipayClient = createClient(naAutoPayConfig);

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setSubject(orderPayDto.getTitle());
        model.setBody(StringUtils.defaultIfEmpty(orderPayDto.getBody(), orderPayDto.getTitle()));
        model.setOutTradeNo(orderPayDto.getOrderId());
        model.setTimeoutExpress(orderPayDto.getTimeoutExpress());
        model.setTotalAmount(orderPayDto.getTotalAmount());
        model.setProductCode(INaPayConst.ProductCode.QUICK_WAP_WAY);
        model.setPassbackParams(encodePassback(orderPayDto.getEncode()));

        request.setBizModel(model);
        request.setNotifyUrl(naAutoPayConfig.getAliNotifyUrl());
        request.setReturnUrl(naAutoPayConfig.getAliReturnUrl());

        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request, "POST");
        orderPayDto.setAlipayTradeWapPayResponse(response);
        return orderPayDto;
    }

    /**
     * 订单查询
     */
    @Override
    public NaOrderQueryDto queryOrder(NaOrderQueryDto orderQueryDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException {
        naAutoPayConfig = (naAutoPayConfig != null) ? naAutoPayConfig : autoPayConfig;
        if (orderQueryDto == null || !orderQueryDto.aliPayParamsChecked()) {
            orderQueryDto.setNaPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
            return orderQueryDto;
        }

        AlipayClient alipayClient = createClient(naAutoPayConfig);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderQueryDto.getOrderId());
        request.setBizModel(model);

        AlipayTradeQueryResponse response = naAutoPayConfig.isAliCert() ? alipayClient.certificateExecute(request) : alipayClient.execute(request);
        orderQueryDto.setAlipayTradeQueryResponse(response);
        orderQueryDto.setNaPayStatus(NaPayStatus.QUERY_ORDER_STATUS_SUCCESS);
        return orderQueryDto;
    }

    /**
     * 单笔转账
     */
    @Override
    public NaTransferDto transfer(NaTransferDto dto, NaAutoPayConfig config) throws AlipayApiException, IOException {
        config = (config != null) ? config : autoPayConfig;
        if (dto == null || !dto.aliPayParamsChecked()) {
            dto.setPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
            return dto;
        }

        AlipayClient client = createClient(config);
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

        model.setOrderTitle(dto.getTitle());
        model.setBizScene(dto.getBizScene());
        model.setRemark(dto.getRemark());
        model.setOutBizNo(dto.getOrderId());
        model.setTransAmount(dto.getTotalAmount());
        model.setProductCode(dto.getProductCode());

        Participant payeeInfo = new Participant();
        if (INaPayConst.ProductCode.TRANS_BANKCARD_NO_PWD.equals(dto.getProductCode())) {
            if (!dto.validateBankTransfer()) {
                dto.setPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
                return dto;
            }
            BankcardExtInfo extInfo = new BankcardExtInfo();
            extInfo.setInstName(String.valueOf(dto.getParams().get(INaPayConst.BankTransferParams.INST_NAME)));
            extInfo.setAccountType(String.valueOf(dto.getParams().get(INaPayConst.BankTransferParams.ACCOUNT_TYPE)));
            payeeInfo.setBankcardExtInfo(extInfo);
            payeeInfo.setIdentityType(INaPayConst.IdentityType.BANKCARD_ACCOUNT);
        } else {
            payeeInfo.setIdentityType(dto.getIdentityType());
        }
        payeeInfo.setIdentity(dto.getAccountNumber());
        payeeInfo.setName(dto.getAccountName());
        model.setPayeeInfo(payeeInfo);

        model.setPassbackParams(encodePassback(dto.getEncode()));
        request.setBizModel(model);

        AlipayFundTransUniTransferResponse response = client.execute(request);
        dto.setAlipayFundTransUniTransferResponse(response);
        dto.setPayStatus(NaPayStatus.TRANSFER_ORDER_SUCCESS);
        return dto;
    }

    /**
     * 退款处理
     */
    @Override
    public NaRefundDto refund(NaRefundDto dto, NaAutoPayConfig config) throws AlipayApiException, IOException {
        config = (config != null) ? config : autoPayConfig;
        if (dto == null || !dto.aliPayParamsChecked()) {
            dto.setNaPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
            return dto;
        }

        AlipayClient client = createClient(config);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        model.setOutTradeNo(dto.getRefundNo());
        if (INaPayConst.RefundType.PARTIAL_REFUND.equals(dto.getRefundType())) {
            if (!dto.validateOutRequestNo()) {
                dto.setNaPayStatus(NaPayStatus.DATA_VERIFY_FAIL);
                return dto;
            }
            model.setOutRequestNo(dto.getRefundNo());
        }

        model.setRefundAmount(dto.getRefundAmount());
        model.setRefundCurrency("CNY");
        model.setRefundReason(dto.getRefundReason());
        request.setBizModel(model);

        AlipayTradeRefundResponse response = client.execute(request);
        dto.setResponse(response);
        return dto;
    }

    @Override
    public NaWXPayDto payApp(NaWXPayDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return payWX(dto, config, NaWXPayConstants.PAY_TRANSACTIONS_APP);
    }

    @Override
    public NaWXPayDto payNative(NaWXPayDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return payWX(dto, config, NaWXPayConstants.PAY_TRANSACTIONS_NATIVE);
    }

    @Override
    public NaWXPayDto payJSAPI(NaWXPayDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return payWX(dto, config, NaWXPayConstants.PAY_TRANSACTIONS_JSAPI);
    }

    @Override
    public NaWXPayDto payH5(NaWXPayDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return payWX(dto, config, NaWXPayConstants.PAY_TRANSACTIONS_H5);
    }

    @Override
    public NaWXOrderQueryDto queryByWxTradeNo(NaWXOrderQueryDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return queryWXOrder(dto, config, NaWXPayConstants.PAY_TRANSACTIONS_ID);
    }

    @Override
    public NaWXOrderQueryDto queryByOutTradeNo(NaWXOrderQueryDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return queryWXOrder(dto, config, NaWXPayConstants.PAY_TRANSACTIONS_OUT_TRADE_NO);
    }

    @Override
    public NaWXOrderQueryDto wxCloseOrder(NaWXOrderQueryDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return closeWXOrder(dto, config, NaWXPayConstants.PAY_CLOSE_ORDER);
    }

    @Override
    public NaWXRefundOrderDto wxRefundOrder(NaWXRefundOrderDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return refundWXOrder(dto, config, NaWXPayConstants.REFUND_DOMESTIC_REFUNDS);
    }

    @Override
    public NaWXRefundOrderDto queryByOutRefundNo(NaWXRefundOrderDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return queryRefundWXOrder(dto, config, NaWXPayConstants.REFUND_DOMESTIC_REFUNDS_ID);
    }

    @Override
    public NaWXTransferDto wxTransfer(NaWXTransferDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return transferWX(dto, config, NaWXPayConstants.TRANSFER);
    }

    @Override
    public NaWXTransferDto queryTransferOutBillNo(NaWXTransferDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return queryTransferWX(dto, config, NaWXPayConstants.TRANSFER_ID);
    }

    @Override
    public NaWXTransferDto queryTransferTransferBillNo(NaWXTransferDto dto, NaAutoPayConfig config) throws IOException {
        config = (config != null) ? config : autoPayConfig;
        return queryTransferWX(dto, config, NaWXPayConstants.TRANSFER_TRANSACTION_ID);
    }

    /**
     * 创建支付宝客户端（证书或普通方式）
     */
    private DefaultAlipayClient createClient(NaAutoPayConfig config) throws AlipayApiException, IOException {
        return config.isAliCert() ? certClient(config) : client(config);
    }

    private DefaultAlipayClient client(NaAutoPayConfig config) throws AlipayApiException {
        return new DefaultAlipayClient(
                config.getAliGatewayUrl(),
                config.getAliAppId(),
                config.getAliAppPrivateKey(),
                config.getAliFormat(),
                config.getAliCharset(),
                config.getAliPublicKey(),
                config.getAliSignType());
    }

    private DefaultAlipayClient certClient(NaAutoPayConfig config) throws AlipayApiException, IOException {
        CertAlipayRequest request = new CertAlipayRequest();
        request.setServerUrl(config.getAliGatewayUrl());
        request.setAppId(config.getAliAppId());
        request.setPrivateKey(config.getAliAppPrivateKey());
        request.setFormat(config.getAliFormat());
        request.setCharset(config.getAliCharset());
        request.setSignType(config.getAliSignType());

        request.setCertPath(NaGlobalPayUtils.getPath(config.getAliAppCertPublicKey(), config.isAliCertProject()));
        request.setAlipayPublicCertPath(NaGlobalPayUtils.getPath(config.getAliCertPublicKey(), config.isAliCertProject()));
        request.setRootCertPath(NaGlobalPayUtils.getPath(config.getAliRootCert(), config.isAliCertProject()));
        return new DefaultAlipayClient(request);
    }

    /**
     * 对传入参数进行URL编码
     */
    private String encodePassback(String encode) {
        try {
            if (StringUtils.isEmpty(encode)) {
                encode = "type=test";
            }
            return URLEncoder.encode(encode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private <T> T doRequest(String api, String method, Object bodyObj, Class<T> responseType, NaAutoPayConfig config) throws IOException {
        String reqBody = "";
        if(bodyObj != null){
            bodyObj = NaWXPayUtility.removeEmptyStrings(bodyObj);
            reqBody = NaWXPayUtility.toJson(bodyObj);
        }
        Request.Builder reqBuilder = new Request.Builder().url(NaWXPayConstants.DOMAIN_API + api);

        reqBuilder.addHeader("Accept", "application/json");
        reqBuilder.addHeader("Wechatpay-Serial", config.getWxPayPublicId());
        reqBuilder.addHeader("Content-Type", "application/json");
        reqBuilder.addHeader("Authorization", NaWXPayUtility.buildAuthorization(
                config.getWxMchId(), config.getWxApiSerialNo(), config.getPrivateKey(), method, api, reqBody));

        if ("POST".equalsIgnoreCase(method)) {
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"), reqBody);
            reqBuilder.method("POST", requestBody);
        } else {
            reqBuilder.method("GET", null);
        }

        Request httpRequest = reqBuilder.build();
        Response httpResponse = httpClient.newCall(httpRequest).execute();

        String respBody = NaWXPayUtility.extractBody(httpResponse);
        log.info("微信请求[{}]状态:{}, 响应体:{}", api, httpResponse.code(), respBody);

        if (httpResponse.code() >= 200 && httpResponse.code() < 300) {
            NaWXPayUtility.validateResponse(config.getWxPayPublicId(), config.getPayPublicKey(),
                    httpResponse.headers(), respBody);
            return NaWXPayUtility.fromJson(respBody, responseType);
        }
        return null;
    }

    public NaWXPayDto payWX(NaWXPayDto dto, NaAutoPayConfig config, String api) throws IOException {
        NaWXPayDto result = doRequest(api, "POST", dto, NaWXPayDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setNaPayStatus(result != null ? NaPayStatus.PAY_SUCCESS : NaPayStatus.PAY_FAIL);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }

    public NaWXOrderQueryDto queryWXOrder(NaWXOrderQueryDto dto, NaAutoPayConfig config, String api) throws IOException {
        api = api.replace("{transaction_id}", NaWXPayUtility.urlEncode(dto.getTransactionId()));
        api = api.replace("{out_trade_no}", NaWXPayUtility.urlEncode(dto.getOutTradeNo()));
        Map<String, Object> args = new HashMap<>();
        args.put("mchid", dto.getMchid());
        api += "?" + NaWXPayUtility.urlEncode(args);

        NaWXOrderQueryDto result = doRequest(api, "GET", null, NaWXOrderQueryDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setNaPayStatus(result != null ? NaPayStatus.QUERY_ORDER_STATUS_SUCCESS : NaPayStatus.QUERY_ORDER_STATUS_FAIL);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }

    public NaWXOrderQueryDto closeWXOrder(NaWXOrderQueryDto dto, NaAutoPayConfig config, String api) throws IOException {
        api = api.replace("{out_trade_no}", NaWXPayUtility.urlEncode(dto.getOutTradeNo()));
        NaWXOrderQueryDto result = doRequest(api, "POST", dto, NaWXOrderQueryDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }

    public NaWXRefundOrderDto refundWXOrder(NaWXRefundOrderDto dto, NaAutoPayConfig config, String api) throws IOException {
        NaWXRefundOrderDto result = doRequest(api, "POST", dto, NaWXRefundOrderDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }

    public NaWXRefundOrderDto queryRefundWXOrder(NaWXRefundOrderDto dto, NaAutoPayConfig config, String api) throws IOException {
        api = api.replace("{out_refund_no}", NaWXPayUtility.urlEncode(dto.getOutRefundNo()));
        NaWXRefundOrderDto result = doRequest(api, "GET", null, NaWXRefundOrderDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }

    private NaWXTransferDto transferWX(NaWXTransferDto dto, NaAutoPayConfig config, String api) throws IOException {
        NaWXTransferDto result = doRequest(api, "POST", dto, NaWXTransferDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }

    private NaWXTransferDto queryTransferWX(NaWXTransferDto dto, NaAutoPayConfig config, String api) throws IOException {
        api = api.replace("{out_bill_no}", NaWXPayUtility.urlEncode(dto.getOutBillNo()));
        api = api.replace("{transfer_bill_no}", NaWXPayUtility.urlEncode(dto.getTransferBillNo()));
        NaWXTransferDto result = doRequest(api, "GET", null, NaWXTransferDto.class, config);
        NaObjConversionUtil.overwriteNonNullAndNonEmptyFields(dto, result);
        dto.setHttpResponse(result != null ? result.getHttpResponse() : null);
        return dto;
    }
}