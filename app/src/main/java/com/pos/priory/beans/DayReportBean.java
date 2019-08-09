package com.pos.priory.beans;

import java.util.List;

public class DayReportBean {


    /**
     * date : 2019-07-01
     * turnovertotal : 1737.1
     * credit : 2277.0
     * cash : 0
     * alipay : 119.8
     * wechatpay : 0
     * voucher : 0
     * refund : -1138.1
     * goldturnover : 1737.1
     * golditemcount : 3
     * golditemweight : 6.04
     * cystalturnover : 0
     * cystalitemcount : 0
     * braceletturnover : 0
     * braceletitemcount : 0
     * items : [{"stock":"2001070110321601","productname":"測試產品","quantity":1,"catalog":"黄金","weight":1.01,"price":599,"discount":"原价","discountprice":599},{"stock":"2001070110355901","productname":"測試產品","quantity":1,"catalog":"黄金","weight":2.01,"price":599,"discount":"9折","discountprice":539.1},{"stock":"2001070110371402","productname":"測試產品","quantity":1,"catalog":"黄金","weight":3.02,"price":599,"discount":"原价","discountprice":599}]
     * refunditem : [{"stock":"2001070110321694","productname":"測試產品","quantity":1,"catalog":"黄金","weight":1.02,"price":599,"discount":"9折","discountprice":539.1},{"stock":"2001070110371401","productname":"測試產品","quantity":1,"catalog":"黄金","weight":3.01,"price":599,"discount":"原价","discountprice":599}]
     */

    private String date;
    private double turnovertotal;
    private double credit;
    private double cash;
    private double alipay;
    private double wechatpay;
    private double voucher;
    private double refund;
    private double goldturnover;
    private double golditemcount;
    private double golditemweight;
    private double cystalturnover;
    private double cystalitemcount;
    private double braceletturnover;
    private double braceletitemcount;
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

    public void setCash(int cash) {
        this.cash = cash;
    }

    public double getAlipay() {
        return alipay;
    }

    public void setAlipay(double alipay) {
        this.alipay = alipay;
    }

    public double getWechatpay() {
        return wechatpay;
    }

    public void setWechatpay(int wechatpay) {
        this.wechatpay = wechatpay;
    }

    public double getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
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

    public double getGolditemcount() {
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

    public double getCystalturnover() {
        return cystalturnover;
    }

    public void setCystalturnover(int cystalturnover) {
        this.cystalturnover = cystalturnover;
    }

    public double getCystalitemcount() {
        return cystalitemcount;
    }

    public void setCystalitemcount(int cystalitemcount) {
        this.cystalitemcount = cystalitemcount;
    }

    public double getBraceletturnover() {
        return braceletturnover;
    }

    public void setBraceletturnover(int braceletturnover) {
        this.braceletturnover = braceletturnover;
    }

    public double getBraceletitemcount() {
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
         * stock : 2001070110321601
         * productname : 測試產品
         * quantity : 1
         * catalog : 黄金
         * weight : 1.01
         * price : 599.0
         * discount : 原价
         * discountprice : 599.0
         */

        private String stock;
        private String productname;
        private int quantity;
        private String catalog;
        private double weight;
        private double price;
        private String discount;
        private double discountprice;
        private boolean isReturnItem = false;

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
         * stock : 2001070110321694
         * productname : 測試產品
         * quantity : 1
         * catalog : 黄金
         * weight : 1.02
         * price : 599.0
         * discount : 9折
         * discountprice : 539.1
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
