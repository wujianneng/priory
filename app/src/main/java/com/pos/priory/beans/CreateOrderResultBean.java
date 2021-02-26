package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/7.
 */

public class CreateOrderResultBean {

    /**
     * orderno : 20210106175417
     */

    private String orderno;

    private int orderid;

    public int getOrderId() {
        return orderid;
    }

    public void setOrderId(int orderId) {
        this.orderid = orderId;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
