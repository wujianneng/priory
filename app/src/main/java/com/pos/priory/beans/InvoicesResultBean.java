package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/7.
 */

public class InvoicesResultBean {

    /**
     * id : 81
     * order : {"id":262,"discount":"1.00","fixdiscount":"0.00"}
     * invoicenumber : INV1000006
     * amount : 50.0
     * status : draft
     */

    private int id;
    private OrderBean order;
    private String invoicenumber;
    private String amount;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public String getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class OrderBean {
        /**
         * id : 262
         * discount : 1.00
         * fixdiscount : 0.00
         */

        private int id;
        private String discount;
        private String fixdiscount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
