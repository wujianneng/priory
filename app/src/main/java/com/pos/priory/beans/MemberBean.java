package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class MemberBean {

    /**
     * id : 4
     * mobile : 12345678
     * first_name : 小明
     * last_name : 陳
     * sex : 男
     * reward : 0
     */

    private int id;
    private String mobile;
    private String first_name;
    private String last_name;
    private String sex;
    private int reward;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
