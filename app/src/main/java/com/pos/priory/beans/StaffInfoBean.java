package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class StaffInfoBean {


    /**
     * id : 5
     * shop : 測試店
     * shopid : 2
     * user : manager01
     * permission : 店长
     * name :
     * gender :
     * staffno :
     * mobile :
     * status : true
     * created : 2020-03-02T07:41:38.534605+08:00
     * updated : 2020-03-04T10:29:29.553227+08:00
     */

    private int id;
    private String shop;
    private int shopid;
    private String user;
    private String permission;
    private String name;
    private String gender;
    private String staffno;
    private String mobile;
    private boolean status;
    private String created;
    private String updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStaffno() {
        return staffno;
    }

    public void setStaffno(String staffno) {
        this.staffno = staffno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
