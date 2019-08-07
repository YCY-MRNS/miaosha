package com.changyue.miaosha.controller;

import com.changyue.miaosha.controller.viewobject.ItemVO;
import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.response.CommonReturnType;
import com.changyue.miaosha.service.ItemService;
import com.changyue.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 14:41
 */
@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", origins = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;


    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getItemList() {
        List<ItemModel> allItem = itemService.getALLItem();
        List<ItemVO> itemVOList = allItem.stream().map(this::convertVoFormModel).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {

        ItemModel itemModel = itemService.getItemById(id);

        return CommonReturnType.create(convertVoFormModel(itemModel));
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType creteItem(@RequestParam(name = "title") String title,
                                      @RequestParam(name = "description") String description,
                                      @RequestParam(name = "price") BigDecimal price,
                                      @RequestParam(name = "stock") Integer stock,
                                      @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {

        ItemModel itemModel = new ItemModel();
        itemModel.setStoke(stock);
        itemModel.setPrice(price);
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelFormReturn = itemService.createItem(itemModel);

        ItemVO itemVO = convertVoFormModel(itemModelFormReturn);

        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertVoFormModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        if (itemModel.getPromoModel() != null) {
            //进行中或者即将进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getItemId());
            itemVO.setStartTime(itemModel.getPromoModel().getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            itemVO.setPromoStatus(0);
        }

        return itemVO;
    }

}
