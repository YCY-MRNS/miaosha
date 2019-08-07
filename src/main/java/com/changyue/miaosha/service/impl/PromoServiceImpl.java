package com.changyue.miaosha.service.impl;

import com.changyue.miaosha.bean.PromoDo;
import com.changyue.miaosha.dao.PromoDoMapper;
import com.changyue.miaosha.service.PromoService;
import com.changyue.miaosha.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-23 13:41
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoDoMapper promoDoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

        PromoDo promoDo = promoDoMapper.selectPromoByItemId(itemId);

        PromoModel promoModel = convertFromDataObject(promoDo);

        if (promoModel == null) {
            return null;
        }

        // 判断当前秒杀活动的状态
        if (promoModel.getStartTime().isAfterNow()) {
            promoModel.setStatus(1);
        } else if (promoModel.getEndTime().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoDo promoDo) {
        if (promoDo == null) {
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStartTime(new DateTime(promoDo.getStartTime()));
        promoModel.setEndTime(new DateTime(promoDo.getEndTime()));

        return promoModel;
    }
}
