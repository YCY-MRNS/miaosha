package com.changyue.miaosha.error;

public enum EmBusinessError implements CommonError {

    //00001通用错误类型
    PARAMETER_VALIDATION_ERRROR(00001, "参数不合法"),
    UNLKNOWN_ERRROR(00002, "未知错误"),

    //10000关于用户
    USER_NOT_EXIST(10001, "用户不存在");

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
