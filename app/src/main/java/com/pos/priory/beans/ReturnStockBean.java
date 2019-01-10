package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class ReturnStockBean {


    /**
     * id : 36
     * rmaorder : RMA1000048
     * name : 虎眼石
     * location : 高士德
     * quantity : 1
     * weight : 6.00
     */

    private int id;
    private String rmaorder;
    private String name;
    private String location;
    private int quantity;
    private String weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRmaorder() {
        return rmaorder;
    }

    public void setRmaorder(String rmaorder) {
        this.rmaorder = rmaorder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
