package com.changyue.miaosha.error;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 23:12
 * 包装器业务异常类实现
 */
public class BusinessException extends Exception implements CommonError {
    private CommonError commonError;

    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public BusinessException(String message, CommonError commonError) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(message);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
