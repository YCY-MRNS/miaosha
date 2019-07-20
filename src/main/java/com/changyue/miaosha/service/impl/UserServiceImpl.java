package com.changyue.miaosha.service.impl;

import com.changyue.miaosha.bean.UserDo;
import com.changyue.miaosha.bean.UserPasswordDo;
import com.changyue.miaosha.dao.UserDoMapper;
import com.changyue.miaosha.dao.UserPasswordDoMapper;
import com.changyue.miaosha.service.UserService;
import com.changyue.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 21:44
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;
    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Override
    public UserModel getUserById(Integer id) {
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);

        if (userDo == null) {
            return null;
        }

        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(id);

        return converrtFromDataObject(userDo, userPasswordDo);
    }

    public UserModel converrtFromDataObject(UserDo userDo, UserPasswordDo userPasswordDo) {

        if (userDo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo, userModel);

        if (userPasswordDo != null) {
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }

        return userModel;
    }
}
