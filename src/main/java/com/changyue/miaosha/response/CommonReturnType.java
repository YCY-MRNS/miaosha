package com.changyue.miaosha.response;

import com.sun.org.apache.regexp.internal.RE;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 22:49
 */
public class CommonReturnType {
    private String status;
    private Object data;

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result,String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
