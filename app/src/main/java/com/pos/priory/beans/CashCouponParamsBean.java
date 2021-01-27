package com.pos.priory.beans;

import java.util.List;

public class CashCouponParamsBean {


    /**
     * member_id : 0
     * products_items : [{"id":0,"quantity":0}]
     */

    private int member_id;
    private List<ProductsItemsBean> products_items;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public List<ProductsItemsBean> getProducts_items() {
        return products_items;
    }

    public void setProducts_items(List<ProductsItemsBean> products_items) {
        this.products_items = products_items;
    }

    public static class ProductsItemsBean {
        /**
         * id : 0
         * quantity : 0
         */

        private int id;
        private int quantity;

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
    }
}
