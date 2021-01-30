package com.krex.panicmall.bean;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Admin on 2021/1/30.
 */
@Configuration
@PropertySource(value = "file:./config/header.yml")
@Data
public class HeaderBean {
    private String cookie;
    private String eid;
    private String fp;
    private String skuId;

}
