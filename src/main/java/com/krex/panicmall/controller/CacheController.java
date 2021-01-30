package com.krex.panicmall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Admin on 2019/4/20.
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public void cacheBookUrl(@RequestParam(value = "name")String pageName)throws Exception{

    }

    @RequestMapping(value = "/read")
    public void cacheBookDetail()throws Exception{

    }
}
