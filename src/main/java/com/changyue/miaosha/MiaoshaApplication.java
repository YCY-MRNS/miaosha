package com.changyue.miaosha;

import com.changyue.miaosha.bean.UserDo;
import com.changyue.miaosha.dao.UserDoMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.changyue.miaosha"})
@RestController
@MapperScan("com.changyue.miaosha.dao")
public class MiaoshaApplication {


    @Autowired
    UserDoMapper userDoMapper;

    @RequestMapping("/")
    public String home() {
        UserDo userDo = userDoMapper.selectByPrimaryKey(1);
        if (userDo == null) {
            return "不存在";
        } else {
            return userDo.getName();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaApplication.class, args);
    }

}
