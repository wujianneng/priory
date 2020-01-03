package com.pos.priory.beans;

public class FittingBean {
    private boolean isSeleted = false;
    private String name;
    private String price;

    public FittingBean(boolean isSeleted, String name, String price) {
        this.isSeleted = isSeleted;
        this.name = name;
        this.price = price;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
