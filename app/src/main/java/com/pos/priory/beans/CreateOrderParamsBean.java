package com.pos.priory.beans;

import java.util.List;

public class CreateOrderParamsBean {

    /**
     * member : 0
     * staff : 0
     * status : 未完成
     * items : [{"stock":0,"discount":"string","fixdiscount":"string","returned":true}]
     * discount : string
     * fixdiscount : string
     */

    private int member;
    private int staff;
    private String status;
    private String discount;
    private String fixdiscount;
    private List<ItemsBean> items;

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFixdiscount() {
        return fixdiscount;
    }

    public void setFixdiscount(String fixdiscount) {
        this.fixdiscount = fixdiscount;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * stock : 0
         * discount : string
         * fixdiscount : string
         * returned : true
         */

        private int stock;
        private String discount;
        private String fixdiscount;
        private boolean returned;

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getFixdiscount() {
            return fixdiscount;
        }

        public void setFixdiscount(String fixdiscount) {
            this.fixdiscount = fixdiscount;
        }

        public boolean isReturned() {
            return returned;
        }

        public void setReturned(boolean returned) {
            this.returned = returned;
        }
    }
}
