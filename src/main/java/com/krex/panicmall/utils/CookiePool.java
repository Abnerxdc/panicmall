package com.krex.panicmall.utils;

import com.krex.reptile.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangwd3 on 2019/10/12.
 */

public class CookiePool {

    private static final Logger logger = LoggerFactory.getLogger(CookiePool.class);
    private static final List<String> cookieList = new ArrayList<>();
    private static final Random random = new Random();

    public CookiePool() {
        /**
         * 读取cookie，放入集合中
         */
        String impPath = PropertiesUtil.getValue("cookie.filePath");
        File file = new File(impPath);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("读取文件:{}异常", impPath, e);
        }
        // 没有读取到文件则退出
        if (lines == null || lines.isEmpty()) {
            return;
        }

        for (String line : lines) {
            cookieList.add(line);
        }
    }


    public static String getCookie(){

        if(cookieList.size() ==0){
            new CookiePool();
        }
        int index = random.nextInt(cookieList.size());
        return cookieList.get(index);
    }


}
