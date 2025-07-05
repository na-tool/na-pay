package com.na.pay.dto.wx;

import lombok.Data;

@Data
public class NaResWXReturnResourceVO {
    private String algorithm;
    private String ciphertext;
    private String associated_data;
    private String original_type;
    private String nonce;
}
