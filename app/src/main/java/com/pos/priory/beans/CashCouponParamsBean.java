package com.pos.priory.beans;

public class CashCouponParamsBean {


    /**
     * member_id : 0
     * total_amount : string
     * shop_id : 0
     */

    private int member_id;
    private double total_amount;
    private int shop_id;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }
}
