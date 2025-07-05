package com.na.pay.service;

import com.alipay.api.AlipayApiException;
import com.na.pay.config.NaAutoPayConfig;
import com.na.pay.dto.NaOrderPayDto;
import com.na.pay.dto.NaOrderQueryDto;
import com.na.pay.dto.NaRefundDto;
import com.na.pay.dto.NaTransferDto;
import com.na.pay.dto.wx.NaWXOrderQueryDto;
import com.na.pay.dto.wx.NaWXPayDto;
import com.na.pay.dto.wx.NaWXRefundOrderDto;
import com.na.pay.dto.wx.NaWXTransferDto;

import java.io.IOException;

public interface INaPayExeService {
    /**
     * 支付宝 APP支付
     *
     * @param naOrderPayDto 支付订单数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 支付结果数据传输对象
     * @throws AlipayApiException 支付宝API调用异常
     * @throws IOException IO异常
     */
    NaOrderPayDto payApp(NaOrderPayDto naOrderPayDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException;

    /**
     * 支付宝 PC网页支付
     *
     * @param orderPayDto 支付订单数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 支付结果数据传输对象
     * @throws AlipayApiException 支付宝API调用异常
     * @throws IOException IO异常
     */
    NaOrderPayDto payWeb(NaOrderPayDto orderPayDto,NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException;

    /**
     * 支付宝 H5支付
     *
     * @param orderPayDto 支付订单数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 支付结果数据传输对象
     * @throws AlipayApiException 支付宝API调用异常
     * @throws IOException IO异常
     */
    NaOrderPayDto payH5(NaOrderPayDto orderPayDto,NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException;

    /**
     * 支付宝 查询订单
     *
     * @param orderQueryDto 查询订单数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws AlipayApiException 支付宝API调用异常
     * @throws IOException IO异常
     */
    NaOrderQueryDto queryOrder(NaOrderQueryDto orderQueryDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException;

    /**
     * 支付宝 转账
     *
     * @param naTransferDto 转账数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 转账结果数据传输对象
     * @throws AlipayApiException 支付宝API调用异常
     * @throws IOException IO异常
     */
    NaTransferDto transfer(NaTransferDto naTransferDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException;

    /**
     * 支付宝 退款
     *
     * @param naRefundDto 退款数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 退款结果数据传输对象
     * @throws AlipayApiException 支付宝API调用异常
     * @throws IOException IO异常
     */
    NaRefundDto refund(NaRefundDto naRefundDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException, IOException;


    /**
     * 微信 APP支付
     *
     * @param dto 微信支付数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 微信支付结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXPayDto payApp(NaWXPayDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 Native支付
     *
     * @param dto 微信支付数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 微信支付结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXPayDto payNative(NaWXPayDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 JSAPI支付
     *
     * @param dto 微信支付数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 微信支付结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXPayDto payJSAPI(NaWXPayDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 H5支付
     *
     * @param dto 微信支付数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 微信支付结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXPayDto payH5(NaWXPayDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 微信支付订单号查询订单
     *
     * @param dto 微信订单查询数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXOrderQueryDto queryByWxTradeNo(NaWXOrderQueryDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 商户订单号查询订单
     *
     * @param dto 微信订单查询数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXOrderQueryDto queryByOutTradeNo(NaWXOrderQueryDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 关闭订单
     *
     * @param dto 微信订单查询数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXOrderQueryDto wxCloseOrder(NaWXOrderQueryDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信 退款申请
     *
     * @param dto 微信退款数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 退款结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXRefundOrderDto wxRefundOrder(NaWXRefundOrderDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 查询单笔退款（按商户退款单号）
     *
     * @param dto 微信退款查询数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXRefundOrderDto queryByOutRefundNo(NaWXRefundOrderDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 发起转账
     *
     * @param dto 微信转账数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 转账结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXTransferDto wxTransfer(NaWXTransferDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 商户单号查询转账单
     *
     * @param dto 微信转账查询数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXTransferDto queryTransferOutBillNo(NaWXTransferDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;

    /**
     * 微信单号查询转账单
     *
     * @param dto 微信转账查询数据传输对象
     * @param naAutoPayConfig 支付配置
     * @return 查询结果数据传输对象
     * @throws IOException IO异常
     */
    NaWXTransferDto queryTransferTransferBillNo(NaWXTransferDto dto, NaAutoPayConfig naAutoPayConfig) throws IOException;
}
