package com.changyue.miaosha.service;

import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.service.model.UserModel;

public interface UserService {

    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String phone, String encryptPassword) throws BusinessException;

}
