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
public class NaWXStoreInfoDto {
    /**
     * 【门店编号】商户侧门店编号，总长度不超过32字符，由商户自定义。
     * 【必填】
     */
    public String id;

    /**
     * 【门店名称】 商户侧门店名称，由商户自定义。
     * 【非必填】
     */
    public String name;

    /**
     * 【地区编码】 地区编码，详细请见省市区编号对照表。
     * 【非必填】
     */
    @JsonProperty("area_code")
    public String areaCode;

    /**
     * 【详细地址】 详细的商户门店地址，由商户自定义。
     * 【非必填】
     */
    public String address;
}
