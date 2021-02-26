package com.pos.priory.beans;

import java.util.List;

public class CouponParamBean {

    /**
     * member : 0
     * products_items : [{"id":0,"quantity":0}]
     */

    private int member_id;
    private List<ProductItemBean> product_item;

    public int getMember() {
        return member_id;
    }

    public void setMember(int member_id) {
        this.member_id = member_id;
    }

    public List<ProductItemBean> getProduct_item() {
        return product_item;
    }

    public void setProduct_item(List<ProductItemBean> product_item) {
        this.product_item = product_item;
    }

    public static class ProductItemBean {
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
