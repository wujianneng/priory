package com.pos.priory.beans;

import java.util.List;

public class DayReportBean {


    /**
     * date : 2019-06-18
     * turnovertotal : 1074.5
     * credit : 299.0
     * cash : 675.5
     * alipay : 0
     * wechatpay : 0
     * voucher : 100.0
     * refund : 670.7
     * goldturnover : 328.1
     * golditemcount : 2
     * golditemweight : 2.15
     * cystalturnover : 0
     * cystalitemcount : 0
     * braceletturnover : 0
     * braceletitemcount : 0
     * items : [{"stock":"2001061819421503","productname":"測試產品B","quantity":1,"catalog":"黃金","weight":1.15,"price":193,"discount":"9折","discountprice":173.7},{"stock":"2001061819421501","productname":"測試產品B","quantity":1,"catalog":"黃金","weight":1,"price":193,"discount":"8折","discountprice":154.4}]
     * refunditem : [{"stock":"2001061819421504","productname":"測試產品B","quantity":1,"catalog":"黃金","weight":1.16,"price":193,"discount":"9折","discountprice":173.7},{"stock":"2001061819421502","productname":"測試產品B","quantity":1,"catalog":"黃金","weight":1.1,"price":193,"discount":"9折","discountprice":173.7},{"stock":"1001061719162401","productname":"光面圓珠A","quantity":1,"catalog":"黃金","weight":1,"price":399,"discount":"原價","discountprice":399}]
     */

    private String date;
    private double turnovertotal;
    private double credit;
    private double cash;
    private int alipay;
    private int wechatpay;
    private double voucher;
    private double refund;
    private double goldturnover;
    private int golditemcount;
    private double golditemweight;
    private int cystalturnover;
    private int cystalitemcount;
    private int braceletturnover;
    private int braceletitemcount;
    private List<ItemsBean> items;
    private List<RefunditemBean> refunditem;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTurnovertotal() {
        return turnovertotal;
    }

    public void setTurnovertotal(double turnovertotal) {
        this.turnovertotal = turnovertotal;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public int getAlipay() {
        return alipay;
    }

    public void setAlipay(int alipay) {
        this.alipay = alipay;
    }

    public int getWechatpay() {
        return wechatpay;
    }

    public void setWechatpay(int wechatpay) {
        this.wechatpay = wechatpay;
    }

    public double getVoucher() {
        return voucher;
    }

    public void setVoucher(double voucher) {
        this.voucher = voucher;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public double getGoldturnover() {
        return goldturnover;
    }

    public void setGoldturnover(double goldturnover) {
        this.goldturnover = goldturnover;
    }

    public int getGolditemcount() {
        return golditemcount;
    }

    public void setGolditemcount(int golditemcount) {
        this.golditemcount = golditemcount;
    }

    public double getGolditemweight() {
        return golditemweight;
    }

    public void setGolditemweight(double golditemweight) {
        this.golditemweight = golditemweight;
    }

    public int getCystalturnover() {
        return cystalturnover;
    }

    public void setCystalturnover(int cystalturnover) {
        this.cystalturnover = cystalturnover;
    }

    public int getCystalitemcount() {
        return cystalitemcount;
    }

    public void setCystalitemcount(int cystalitemcount) {
        this.cystalitemcount = cystalitemcount;
    }

    public int getBraceletturnover() {
        return braceletturnover;
    }

    public void setBraceletturnover(int braceletturnover) {
        this.braceletturnover = braceletturnover;
    }

    public int getBraceletitemcount() {
        return braceletitemcount;
    }

    public void setBraceletitemcount(int braceletitemcount) {
        this.braceletitemcount = braceletitemcount;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public List<RefunditemBean> getRefunditem() {
        return refunditem;
    }

    public void setRefunditem(List<RefunditemBean> refunditem) {
        this.refunditem = refunditem;
    }

    public static class ItemsBean {
        /**
         * stock : 2001061819421503
         * productname : 測試產品B
         * quantity : 1
         * catalog : 黃金
         * weight : 1.15
         * price : 193.0
         * discount : 9折
         * discountprice : 173.7
         */

        private String stock;
        private String productname;
        private int quantity;
        private String catalog;
        private double weight;
        private double price;
        private String discount;
        private double discountprice;
        boolean isReturnItem = false;

        public boolean isReturnItem() {
            return isReturnItem;
        }

        public void setReturnItem(boolean returnItem) {
            isReturnItem = returnItem;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public double getDiscountprice() {
            return discountprice;
        }

        public void setDiscountprice(double discountprice) {
            this.discountprice = discountprice;
        }
    }

    public static class RefunditemBean {
        /**
         * stock : 2001061819421504
         * productname : 測試產品B
         * quantity : 1
         * catalog : 黃金
         * weight : 1.16
         * price : 193.0
         * discount : 9折
         * discountprice : 173.7
         */

        private String stock;
        private String productname;
        private int quantity;
        private String catalog;
        private double weight;
        private double price;
        private String discount;
        private double discountprice;

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public double getDiscountprice() {
            return discountprice;
        }

        public void setDiscountprice(double discountprice) {
            this.discountprice = discountprice;
        }
    }
}
