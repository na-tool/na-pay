package com.na.pay.service;

import com.alipay.api.AlipayApiException;
import com.na.pay.config.NaAutoPayConfig;
import com.na.pay.dto.NaOrderCallbackInAsyncDto;
import com.na.pay.enums.NaPayStatus;

public interface INaPayOrderGlobalService {
    /**
     * 检查订单的一些数据
     *
     * @param naOrderCallbackInAsyncDto 异步回调订单数据传输对象
     * @param naAutoPayConfig 支付配置对象
     * @return 返回订单支付状态 {@link NaPayStatus}
     * @throws Exception 调用异常
     */
    NaPayStatus checkOrder(NaOrderCallbackInAsyncDto naOrderCallbackInAsyncDto, NaAutoPayConfig naAutoPayConfig) throws Exception;

    /**
     * 更新订单状态
     *
     * @param naOrderCallbackInAsyncDto 异步回调订单数据传输对象
     * @param naAutoPayConfig 支付配置对象
     * @return 返回更新后的订单支付状态 {@link NaPayStatus}
     */
    NaPayStatus updateOrder(NaOrderCallbackInAsyncDto naOrderCallbackInAsyncDto,NaAutoPayConfig naAutoPayConfig);
}
