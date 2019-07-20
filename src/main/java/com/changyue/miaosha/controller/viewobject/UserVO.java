package com.changyue.miaosha.controller.viewobject;

/**
 * @program: miaosha
 * @description:
 * @author: ChangYue
 * @create: 2019-07-19 22:21
 */
public class UserVO {
    private Integer id;
    private String name;
    private Byte gender;
    private String phone;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
