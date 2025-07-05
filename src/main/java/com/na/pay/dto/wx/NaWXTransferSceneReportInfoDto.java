package com.na.pay.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaWXTransferSceneReportInfoDto {
    /**
     * 【信息类型】 不能超过15个字符，商户所属转账场景下的信息类型，此字段内容为固定值，需严格按照转账场景报备信息字段说明传参。
     * 【必填】
     */
    @JsonProperty("info_type")
    private String infoType;

    /**
     * 【信息内容】 不能超过32个字符，商户所属转账场景下的信息内容，商户可按实际业务场景自定义传参，需严格按照转账场景报备信息字段说明传参。
     * 【必填】
     */
    @JsonProperty("info_content")
    private String infoContent;
}
