package com.changyue.miaosha.controller;

import com.alibaba.druid.util.StringUtils;
import com.changyue.miaosha.controller.viewobject.UserVO;
import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.response.CommonReturnType;
import com.changyue.miaosha.service.UserService;
import com.changyue.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 21:42
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;


    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "phone")String phone,
                                  @RequestParam(name = "password")String password) throws BusinessException, NoSuchAlgorithmException {

        //入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(password)|| org.apache.commons.lang3.StringUtils.isEmpty(phone)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //校验登录是否合法
        UserModel userModel = userService.validateLogin(phone, this.EncodedByMD5(password));

        this.request.getSession().setAttribute("IS_LOGIN",true);
        this.request.getSession().setAttribute("LOGIN_USER", userModel);

        System.out.println("登录成功");

        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "phone") String phone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age) throws BusinessException, NoSuchAlgorithmException {
        //验证 otp
        String inSessionOtpCode = (String) this.request.getSession().getAttribute(phone);
        if (!StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException("短信验证不符合！", EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户注册
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setPhone(phone);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setRegisterMode("byphone");
        userModel.setEncryptPassword(this.EncodedByMD5(password));

        userService.register(userModel);

        System.out.println("注册成功！");

        return CommonReturnType.create(null);
    }

    private String EncodedByMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encode = base64Encoder.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
        return encode;
    }

    @RequestMapping(value = "/getotp", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "phone") String phone) {

        //1.生成验证码
        Random random = new Random();
        int randomNum = random.nextInt(99999);
        randomNum += 10000;
        String otpCode = String.valueOf(randomNum);

        //2.关联手机号 可以放在redis 便于分布式 简单的方式HttpSession
        request.getSession().setAttribute(phone, otpCode);

        //3.发送otp给用户
        System.out.println(phone + "----->" + otpCode);

        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {

        UserModel userModel = userService.getUserById(id);

        //用户不存在
        if (userModel == null) {
            //抛出异常 同时需要去处理
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO = convertFromModel(userModel);

        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
