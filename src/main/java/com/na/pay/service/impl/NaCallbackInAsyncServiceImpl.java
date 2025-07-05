package com.na.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.na.pay.config.NaAutoPayConfig;
import com.na.pay.dto.NaOrderCallbackInAsyncDto;
import com.na.pay.enums.NaPayStatus;
import com.na.pay.service.INaCallbackInAsyncService;
import com.na.pay.service.INaPayOrderGlobalService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        prefix = "na.pay",
        name = "callback",
        havingValue = "true",       // 只有当为 "true" 时才加载
        matchIfMissing = false      // 配置项缺失时不加载
)
public class NaCallbackInAsyncServiceImpl implements INaCallbackInAsyncService {
    @Override
    public NaPayStatus orderCallbackInAsync(INaPayOrderGlobalService naPayOrderGlobalService,
                                            NaOrderCallbackInAsyncDto naOrderCallbackInAsyncDto,
                                            NaAutoPayConfig naAutoPayConfig) throws Exception {
        NaPayStatus naPayStatus = naPayOrderGlobalService.checkOrder(naOrderCallbackInAsyncDto,naAutoPayConfig);
        // 验证成功
        if(NaPayStatus.DATA_VERIFY_SUCCESS.equals(naPayStatus)){
            // 更新订单
            naPayStatus = naPayOrderGlobalService.updateOrder(naOrderCallbackInAsyncDto,naAutoPayConfig);
        }
        return naPayStatus;
    }
}
