package com.pos.priory.beans;

import java.util.List;

public class OrderCalculationParamBean {

    /**
     * order_type : 0  0=正常訂單， 1=換貨訂單
     * coupons : [0]
     * products : [{"id":2,"count":4,"whnumber":"122800000002"}]
     * shop : 0
     * member : 0
     * old_order : 0
     * old_order_items : [0]
     */

    private int order_type;
    private int shop;
    private int member;
    private int old_order;
    private List<Integer> coupons;
    private List<ProductsBean> products;
    private List<OldOrderItemBean> old_order_items;

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getOld_order() {
        return old_order;
    }

    public void setOld_order(int old_order) {
        this.old_order = old_order;
    }

    public List<Integer> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Integer> coupons) {
        this.coupons = coupons;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public List<OldOrderItemBean> getOld_order_items() {
        return old_order_items;
    }

    public void setOld_order_items(List<OldOrderItemBean> old_order_items) {
        this.old_order_items = old_order_items;
    }

    public static class OldOrderItemBean{

        /**
         * id : 0
         * weight : 0
         */

        private int id;
        private double weight;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public static class ProductsBean {
        /**
         * id : 2
         * quantity : 4
         * whnumber : 122800000002
         */

        private int id;
        private int quantity;
        private String whnumber;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return quantity;
        }

        public void setCount(int count) {
            this.quantity = count;
        }

        public String getWhnumber() {
            return whnumber;
        }

        public void setWhnumber(String whnumber) {
            this.whnumber = whnumber;
        }
    }
}
