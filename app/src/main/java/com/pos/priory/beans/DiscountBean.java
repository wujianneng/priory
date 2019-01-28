package com.pos.priory.beans;

public class DiscountBean {

    /**
     * id : 137
     * name : 沒有折扣
     * value : 1.00
     * location : 測試
     */

    private int id;
    private String name;
    private String value;
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
