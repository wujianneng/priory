package com.pos.priory.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class DayReportDataBean implements MultiItemEntity {
    public int itemType = 0;

    @Override
    public int getItemType() {
        return itemType;
    }

    /**
     * amount_total : 0
     * quantity_total : 0
     * weight_total : 0
     * item : [{"product_name":"string","code":"string","weight":0,"quantity":0}]
     * price : 0
     * price_total : 0
     */
    private String title;
    private double amount_total;
    private int quantity_total;
    private double weight_total;
    private double price;
    private double price_total;
    private List<ItemBean> item;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(double amount_total) {
        this.amount_total = amount_total;
    }

    public int getQuantity_total() {
        return quantity_total;
    }

    public void setQuantity_total(int quantity_total) {
        this.quantity_total = quantity_total;
    }

    public double getWeight_total() {
        return weight_total;
    }

    public void setWeight_total(double weight_total) {
        this.weight_total = weight_total;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_total() {
        return price_total;
    }

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * product_name : string
         * code : string
         * weight : 0
         * quantity : 0
         */

        private String product_name;
        private String code;
        private double weight;
        private int quantity;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
