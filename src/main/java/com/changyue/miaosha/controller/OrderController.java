package com.changyue.miaosha.controller;

import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.response.CommonReturnType;
import com.changyue.miaosha.service.OrderService;
import com.changyue.miaosha.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-22 21:41
 */

@Controller("/order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", origins = "*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/createorder", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "promoId", required = false) Integer promoId,
                                        @RequestParam(name = "amount") Integer amount) throws BusinessException {

        Boolean is_login = (Boolean) request.getSession().getAttribute("IS_LOGIN");

        if (is_login == null || !is_login) {
            throw new BusinessException("用户还未登录，不能下单", EmBusinessError.USER_NOT_LOGIN);
        }

        UserModel loginUser = (UserModel) request.getSession().getAttribute("LOGIN_USER");

        orderService.createOrder(loginUser.getId(), itemId, promoId, amount);

        return CommonReturnType.create(null);

    }

}
