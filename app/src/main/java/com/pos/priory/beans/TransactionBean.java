package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/10.
 */

public class TransactionBean {

    /**
     * invoice : {"id":118,"order":{"id":312,"discount":"1.00","fixdiscount":"0.00"},"invoicenumber":"INV1000041","amount":"3000.0","status":"draft"}
     * transactionnumber : T10000040
     * amount : 3000.0
     * paymentmethod : 現金
     */

    private InvoiceBean invoice;
    private String transactionnumber;
    private String amount;
    private String paymentmethod;

    public InvoiceBean getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceBean invoice) {
        this.invoice = invoice;
    }

    public String getTransactionnumber() {
        return transactionnumber;
    }

    public void setTransactionnumber(String transactionnumber) {
        this.transactionnumber = transactionnumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public static class InvoiceBean {
        /**
         * id : 118
         * order : {"id":312,"discount":"1.00","fixdiscount":"0.00"}
         * invoicenumber : INV1000041
         * amount : 3000.0
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
             * id : 312
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
}
