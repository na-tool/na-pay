package com.na.pay.dto.wx;

import lombok.Data;

@Data
public class NaResWXReturnVO {
    private String id;
    private String create_time;
    private String event_type;
    private String resource_type;
    private NaResWXReturnResourceVO resource;
    private String summary;
}
