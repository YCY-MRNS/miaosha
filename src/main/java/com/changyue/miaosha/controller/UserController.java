package com.changyue.miaosha.controller;

import com.changyue.miaosha.controller.viewobject.UserVO;
import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.response.CommonReturnType;
import com.changyue.miaosha.service.UserService;
import com.changyue.miaosha.service.model.UserModel;
import com.sun.deploy.net.HttpRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 21:42
 */
@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {

        UserModel userModel = userService.getUserById(id);

        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);

        }

        UserVO userVO = convertFromModel(userModel);

        return CommonReturnType.create(userVO);
    }

    public UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    //定义exception handler 解决未被controller 层吸收exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request, Exception e) {

        Map<String, Object> response = new HashMap<>();

        if (e instanceof BusinessException) {
            BusinessException exception = (BusinessException) e;
            response.put("errCode", exception.getErrCode());
            response.put("errMsg", exception.getErrMsg());
        } else {
            response.put("errCode", EmBusinessError.UNLKNOWN_ERRROR.getErrCode());
            response.put("errMsg", EmBusinessError.UNLKNOWN_ERRROR.getErrMsg());
        }
        return CommonReturnType.create(response, "fail");

    }

}
