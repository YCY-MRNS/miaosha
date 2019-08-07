package com.changyue.miaosha.service;

import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.service.model.OrderModel;

public interface OrderService {

    /**
     * 1. 通过前端url上传过来的秒杀活动Id 然后下单接口内校验对应的秒话活动是否开始
     */
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;

}
