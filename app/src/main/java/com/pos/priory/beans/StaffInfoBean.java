package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class StaffInfoBean {

    /**
     * user : staff01
     * sex : M
     * staffno : ab00001
     * mobile : 12345678
     * store : 高士德
     */

    private String user;
    private String sex;
    private String staffno;
    private String mobile;
    private String store;
    private String permission;
    private int storeid;

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permissionString) {
        this.permission = permissionString;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
