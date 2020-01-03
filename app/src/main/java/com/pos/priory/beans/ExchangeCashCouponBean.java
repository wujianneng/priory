package com.pos.priory.beans;

public class ExchangeCashCouponBean {
    String name;
    boolean isSelected = false;
    double needIntegal = 0;
    int count = 0;

    public ExchangeCashCouponBean(String name, double needIntegal) {
        this.name = name;
        this.needIntegal = needIntegal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public double getNeedIntegal() {
        return needIntegal;
    }

    public void setNeedIntegal(double needIntegal) {
        this.needIntegal = needIntegal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
