package com.na.pay.service;

import com.alipay.api.AlipayApiException;
import com.na.pay.config.NaAutoPayConfig;
import com.na.pay.dto.NaOrderCallbackInAsyncDto;
import com.na.pay.enums.NaPayStatus;

public interface INaCallbackInAsyncService {

    /**
     * 处理全局支付异步回调
     *
     * @param naPayOrderGlobalService 支付订单全局处理服务（由业务实现）
     * @param naOrderCallbackInAsyncDto 回调参数 DTO
     * @param naAutoPayConfig 当前商户的支付配置
     * @return 返回支付状态 {@link NaPayStatus}
     * @throws AlipayApiException 当调用支付宝接口出现异常时抛出
     */
    NaPayStatus orderCallbackInAsync(INaPayOrderGlobalService naPayOrderGlobalService,
                                     NaOrderCallbackInAsyncDto naOrderCallbackInAsyncDto,
                                     NaAutoPayConfig naAutoPayConfig) throws Exception;
}
