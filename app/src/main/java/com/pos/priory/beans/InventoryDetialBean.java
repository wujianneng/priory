package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class InventoryDetialBean {


    /**
     * id : 11070
     * check : false
     * productname : 父情节活动产品009
     * stockno : 155706100024
     * stockweight : 0.0
     */

    private int id;
    private boolean check;
    private String productname;
    private String stockno;
    private double stockweight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getStockno() {
        return stockno;
    }

    public void setStockno(String stockno) {
        this.stockno = stockno;
    }

    public double getStockweight() {
        return stockweight;
    }

    public void setStockweight(double stockweight) {
        this.stockweight = stockweight;
    }
}
