package com.changyue.miaosha.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 11:24
 */
public class ValidatorResult {

    //是否有错
    private boolean hasError = false;

    //存放错误的信息
    private Map<String, String> errorMsgMap = new HashMap<>();

    public boolean isHasError() {
        return hasError;
    }

    void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现通用的格式化字符串信息获取的错误信息结果的msg方法
    public String getErrMsg() {
        return StringUtils.join(Arrays.toString(errorMsgMap.values().toArray()) + ",");
    }
    
}
