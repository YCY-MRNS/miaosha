package com.changyue.miaosha.controller.viewobject;

import java.math.BigDecimal;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 14:41
 */
public class ItemVO {
    //商品信息
    private Integer id;
    private String title;
    private BigDecimal price;
    private Integer stoke;
    private String description;
    private Integer sales;
    private String imgUrl;

    //秒杀信息
    //该商品是否有秒杀活动 1 没有 2 正在进行 3 结束
    private Integer promoStatus;
    private BigDecimal promoPrice;
    private Integer promoId;
    private String startTime;

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStoke() {
        return stoke;
    }

    public void setStoke(Integer stoke) {
        this.stoke = stoke;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
