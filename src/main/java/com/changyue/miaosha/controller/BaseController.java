package com.changyue.miaosha.controller;

import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: miaosha
 * @description: 通用Controller
 * @author: ChangYue
 * @create: 2019-07-21 16:29
 */

public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    //定义exception handler 捕获和解决未被controller层吸收exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception e) {

        //需要返回的异常信息
        Map<String, Object> response = new HashMap<>();

        if (e instanceof BusinessException) {
            BusinessException exception = (BusinessException) e;
            response.put("errCode", exception.getErrCode());
            response.put("errMsg", exception.getErrMsg());
        } else {
            response.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            response.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.create(response, "fail");
    }

}
