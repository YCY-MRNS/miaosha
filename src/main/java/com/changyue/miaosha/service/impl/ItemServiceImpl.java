package com.changyue.miaosha.service.impl;

import com.changyue.miaosha.bean.ItemDo;
import com.changyue.miaosha.bean.ItemStockDo;
import com.changyue.miaosha.dao.ItemDoMapper;
import com.changyue.miaosha.dao.ItemStockDoMapper;
import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.service.ItemService;
import com.changyue.miaosha.service.PromoService;
import com.changyue.miaosha.service.model.ItemModel;
import com.changyue.miaosha.service.model.PromoModel;
import com.changyue.miaosha.validator.ValidatorImpl;
import com.changyue.miaosha.validator.ValidatorResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 13:44
 */
@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    ValidatorImpl validator;

    @Autowired
    ItemDoMapper itemDoMapper;

    @Autowired
    PromoService promoService;

    @Autowired
    ItemStockDoMapper itemStockDoMapper;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        //1 校验入参对象
        ValidatorResult validator = this.validator.vlidator(itemModel);
        if (validator.isHasError()) {
            throw new BusinessException(validator.getErrMsg(), EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //2 转换
        ItemDo itemDo = this.convertItemFormItemModel(itemModel);

        //3 加入数据库
        itemDoMapper.insertSelective(itemDo);
        itemModel.setId(itemDo.getId());

        ItemStockDo itemStockDo = this.convertItemStockFormItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);
        //4 返回对象

        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> getALLItem() {

        List<ItemDo> itemDosList = itemDoMapper.listItem();

        return itemDosList.stream().map(itemDo -> {
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            return this.convertModelFromDataObject(itemDo, itemStockDo);
        }).collect(Collectors.toList());

    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null) {
            return null;
        }

        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(id);

        ItemModel itemModel = convertModelFromDataObject(itemDo, itemStockDo);

        //当前商品是否存在秒杀活动
        PromoModel promoModel = promoService.getPromoByItemId(id);

        if (promoModel != null && promoModel.getStatus() != 3) {
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int effectRow = itemStockDoMapper.decreaseStoke(itemId, amount);
        return effectRow > 0;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDoMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDo itemDo, ItemStockDo itemStockDo) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo, itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        itemModel.setStoke(itemStockDo.getStock());
        return itemModel;
    }

    private ItemDo convertItemFormItemModel(ItemModel itemModel) {

        if (itemModel == null) {
            return null;
        }

        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel, itemDo);
        itemDo.setPrice(itemModel.getPrice().doubleValue());

        return itemDo;
    }

    private ItemStockDo convertItemStockFormItemModel(ItemModel itemModel) {

        if (itemModel == null) {
            return null;
        }

        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStoke());

        return itemStockDo;
    }


}
