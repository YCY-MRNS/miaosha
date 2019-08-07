package com.changyue.miaosha.service.model;

import java.math.BigDecimal;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 19:58
 */
public class OrderModel {

    private String id;
    private Integer userId;
    private Integer itemId;
    //购买的数量
    private Integer amount;
    //商品单价
    private BigDecimal itemPrice;

    //非空则是秒
    private Integer promoId;

    //购买金额 非空则是秒
    private BigDecimal orderPrice;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
