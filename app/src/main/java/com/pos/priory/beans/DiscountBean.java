package com.pos.priory.beans;

import java.io.Serializable;

public class DiscountBean implements Serializable {

    private int id;
    private String name;
    private String detail;
    private boolean isSelected;
    private boolean isShowDetail;

    public DiscountBean(int id, String name,  String detail, boolean isSelected, boolean isShowDetail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.isSelected = isSelected;
        this.isShowDetail = isShowDetail;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isShowDetail() {
        return isShowDetail;
    }

    public void setShowDetail(boolean showDetail) {
        isShowDetail = showDetail;
    }

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

}
