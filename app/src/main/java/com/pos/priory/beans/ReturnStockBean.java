package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class ReturnStockBean {

    /**
     * id : 2
     * rmaorder : 228
     * name : test
     * quantity : 1
     * weight : 0.20
     */

    private int id;
    private int rmaorder;
    private String name;
    private int quantity;
    private String weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRmaorder() {
        return rmaorder;
    }

    public void setRmaorder(int rmaorder) {
        this.rmaorder = rmaorder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
