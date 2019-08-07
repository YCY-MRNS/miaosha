package com.changyue.miaosha.service.impl;

import com.changyue.miaosha.bean.UserDo;
import com.changyue.miaosha.bean.UserPasswordDo;
import com.changyue.miaosha.dao.UserDoMapper;
import com.changyue.miaosha.dao.UserPasswordDoMapper;
import com.changyue.miaosha.error.BusinessException;
import com.changyue.miaosha.error.EmBusinessError;
import com.changyue.miaosha.service.UserService;
import com.changyue.miaosha.service.model.UserModel;
import com.changyue.miaosha.validator.ValidatorImpl;
import com.changyue.miaosha.validator.ValidatorResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);

        if (userDo == null) {
            return null;
        }

        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(id);

        return convertFromDataObject(userDo, userPasswordDo);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //校验bean
        ValidatorResult validator = this.validator.vlidator(userModel);
        if (validator.isHasError()) {
            throw new BusinessException(validator.getErrMsg(), EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserDo userDo = convertUserFromModel(userModel);

        try {
            userDoMapper.insertSelective(userDo);
        } catch (Exception e) {
            throw new BusinessException("手机号已重复注册", EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        userModel.setId(userDo.getId());

        UserPasswordDo userPasswordDo = convertPasswordFormModel(userModel);

        userPasswordDoMapper.insertSelective(userPasswordDo);

    }

    @Override
    public UserModel validateLogin(String phone, String encryptPassword) throws BusinessException {

        //通过手机号获得用户信息
        UserDo userDo = userDoMapper.selectByPrimaryPhone(phone);
        if (userDo == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());
        UserModel userModel = convertFromDataObject(userDo, userPasswordDo);

        //比对密码
        if (!StringUtils.equals(encryptPassword, userModel.getEncryptPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        return userModel;
    }


    private UserPasswordDo convertPasswordFormModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPasswordDo passwordDo = new UserPasswordDo();
        passwordDo.setEncryptPassword(userModel.getEncryptPassword());
        passwordDo.setUserId(userModel.getId());
        return passwordDo;
    }

    private UserDo convertUserFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel, userDo);
        return userDo;
    }

    private UserModel convertFromDataObject(UserDo userDo, UserPasswordDo userPasswordDo) {
        if (userDo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo, userModel);

        if (userPasswordDo != null) {
            userModel.setEncryptPassword(userPasswordDo.getEncryptPassword());
        }
        return userModel;
    }
}
