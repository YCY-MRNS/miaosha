package com.changyue.miaosha.service;

import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.service.model.ItemModel;

import java.util.List;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 13:41
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //获得商品的list
    List<ItemModel> getALLItem();

    //获得商品对象 通过id
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount);

    //商品销量增加
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;

}
