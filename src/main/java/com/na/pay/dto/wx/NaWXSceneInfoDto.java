package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXSceneInfoDto {
    /**
     * 【用户终端IP】 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     * 【必填】
     */
    @JsonProperty("payer_client_ip")
    public String payerClientIp;

    /**
     * 【商户端设备号】 商户端设备号（门店号或收银设备ID）
     * 【可选】
     */
    @JsonProperty("device_id")
    public String deviceId;

    /**
     * 【商户门店信息】 商户门店信息
     * 【可选】
     */
    @JsonProperty("store_info")
    public NaWXStoreInfoDto storeInfo;
}
