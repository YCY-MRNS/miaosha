package com.changyue.miaosha.service.impl;

import com.changyue.miaosha.bean.OrderDo;
import com.changyue.miaosha.bean.SequenceDo;
import com.changyue.miaosha.dao.OrderDoMapper;
import com.changyue.miaosha.dao.SequenceDoMapper;
import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.service.ItemService;
import com.changyue.miaosha.service.OrderService;
import com.changyue.miaosha.service.UserService;
import com.changyue.miaosha.service.model.ItemModel;
import com.changyue.miaosha.service.model.OrderModel;
import com.changyue.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 20:10
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //1 校验下单内容的合法性  商品是否存在  用户是否合法  购买的数量是否合法
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException("商品不存在", EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException("用户信息不存在", EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if (amount <= 0 || amount > 99) {
            throw new BusinessException("购买数量不正确", EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //校验活动信息
        if (promoId != null) {
            // (1).校验对应活动是否为该商品的促销的商品活动
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException("活动的信息不正确！", EmBusinessError.PARAMETER_VALIDATION_ERROR);
            } else if (itemModel.getPromoModel().getStatus() != 2) {
                throw new BusinessException("活动还没开始！", EmBusinessError.PARAMETER_VALIDATION_ERROR);
            }

        }


        //2 落单减库存 支付减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }


        //3 订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setPromoId(promoId);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderModel.setId(generateOrderNo());

        OrderDo orderDo = this.convertFromOrderModel(orderModel);

        try {
            orderDoMapper.insertSelective(orderDo);
            //4 更新商品销量
            itemService.increaseSales(itemId, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return orderModel;
    }

    /**
     * @return 生成订单号
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo() {
        //订单号16位
        StringBuilder order = new StringBuilder();
        //前八位 时间信息 年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        order.append(nowDate);

        //中六位为自增序列
        int sequence = 0;
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        sequence = sequenceDo.getCurrentValue();
        sequenceDo.setCurrentValue(sequenceDo.getCurrentValue() + sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);

        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            order.append(0);
        }

        //最后两位分库分表位
        order.append("00");

        return order.toString();
    }

    private OrderDo convertFromOrderModel(OrderModel orderModel) {

        if (orderModel == null) {
            return null;
        }

        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel, orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDo;

    }
}
