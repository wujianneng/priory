package com.pos.priory.beans;

import java.util.List;

public class CouponParamBean {
    /**
     * member : 0
     * products_items : [{"id":1,"quantity":1,"amount":399,"category":1}]
     */

    private int member;
    private List<ProductsItemsBean> products_items;

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public List<ProductsItemsBean> getProducts_items() {
        return products_items;
    }

    public void setProducts_items(List<ProductsItemsBean> products_items) {
        this.products_items = products_items;
    }

    public static class ProductsItemsBean {
        /**
         * id : 1
         * quantity : 1
         * amount : 399
         * category : 1
         */

        private int id;
        private int quantity;
        private int amount;
        private int category;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }
    }
}
