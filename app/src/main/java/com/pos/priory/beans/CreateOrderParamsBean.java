package com.pos.priory.beans;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderParamsBean {

    /**
     * order_type : 0
     * member : 1
     * paymethods : [{"id":1,"amount":800},{"id":2,"amount":11},{"id":7,"amount":100,"cash_coupon_id":31}]
     * amount_payable : 911
     * cash_coupons : [31]
     * shop : 1
     * cache_token : 3f3142b17d85452b9e1ffc96c6d06ed8
     */

    private int order_type;
    private int member;
    private BigDecimal amount_payable;
    private int shop;
    private String cache_token;
    private List<PaymethodsBean> paymethods;
    private List<Integer> cash_coupons;

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public BigDecimal getAmount_payable() {
        return amount_payable;
    }

    public void setAmount_payable(BigDecimal amount_payable) {
        this.amount_payable = amount_payable;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public String getCache_token() {
        return cache_token;
    }

    public void setCache_token(String cache_token) {
        this.cache_token = cache_token;
    }

    public List<PaymethodsBean> getPaymethods() {
        return paymethods;
    }

    public void setPaymethods(List<PaymethodsBean> paymethods) {
        this.paymethods = paymethods;
    }

    public List<Integer> getCash_coupons() {
        return cash_coupons;
    }

    public void setCash_coupons(List<Integer> cash_coupons) {
        this.cash_coupons = cash_coupons;
    }

    public static class PaymethodsBean {
        /**
         * id : 1
         * amount : 800
         * cash_coupon_id : 31
         */

        private int id;
        private double amount;
        private int cash_coupon_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCash_coupon_id() {
            return cash_coupon_id;
        }

        public void setCash_coupon_id(int cash_coupon_id) {
            this.cash_coupon_id = cash_coupon_id;
        }
    }
}
