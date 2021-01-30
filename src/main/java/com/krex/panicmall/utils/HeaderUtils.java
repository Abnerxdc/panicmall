package com.krex.panicmall.utils;

import com.krex.panicmall.bean.HeaderBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Admin on 2021/1/30.
 */
@Component
@EnableConfigurationProperties({HeaderBean.class})
public class HeaderUtils {
    @Autowired
    HeaderBean headerBean;

    public HttpHeaders getJDHeaders(){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, Arrays.asList(headerBean.getCookie()));
        requestHeaders.put(HttpHeaders.REFERER,Arrays.asList("https://marathon.jd.com/seckill/seckill.action?skuId={0}&num={1}&rid={2}"));
        requestHeaders.put(HttpHeaders.HOST,Arrays.asList("marathon.jd.com"));
        requestHeaders.put(HttpHeaders.USER_AGENT,Arrays.asList("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36"));

        requestHeaders.put("eid",Arrays.asList(headerBean.getEid()));
        requestHeaders.put("fp",Arrays.asList(headerBean.getFp()));
        requestHeaders.put("skuId",Arrays.asList(headerBean.getSkuId()));
        return requestHeaders;
    }
}
