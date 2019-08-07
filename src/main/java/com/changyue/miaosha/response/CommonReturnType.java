package com.changyue.miaosha.response;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 22:49
 */
public class CommonReturnType {
    //返回结果代码 错误或者是失败
    private String status;

    //顺着代码一起返回的数据
    private Object data;

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    private void setData(Object data) {
        this.data = data;
    }
}
