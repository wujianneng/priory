package com.pos.priory.beans;

public class MemberDetailResultBean {

    /**
     * id : 0
     * status : string
     * mobile : string
     * firstname : string
     * lastname : string
     * gender : 0
     * shop : 0
     * reward : string
     * created : 2021-01-18T08:37:10.269Z
     * updated : 2021-01-18T08:37:10.269Z
     */

    private int id;
    private String status;
    private String mobile;
    private String firstname;
    private String lastname;
    private int gender;
    private int shop;
    private String reward;
    private String created;
    private String updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
