package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class StaffInfoBean {


    /**
     * id : 4
     * shop : 澳門測試店
     * shopid : 1
     * user : manager01
     * apppermit : 店长
     * name :
     * gender :
     * staffno :
     * mobile :
     * status : true
     * created : 2020-08-13T16:09:12.832735+08:00
     * updated : 2020-08-13T16:19:02.280433+08:00
     * backendpermit : []
     */

    private int id;
    private String shop;
    private int shopid;
    private String user;
    private String apppermit;
    private String name;
    private String gender;
    private String staffno;
    private String mobile;
    private boolean status;
    private String created;
    private String updated;
    private List<?> backendpermit;

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

    public String getApppermit() {
        return apppermit;
    }

    public void setApppermit(String apppermit) {
        this.apppermit = apppermit;
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

    public List<?> getBackendpermit() {
        return backendpermit;
    }

    public void setBackendpermit(List<?> backendpermit) {
        this.backendpermit = backendpermit;
    }
}
