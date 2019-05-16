package com.pos.priory.beans;

import java.util.List;

public class DayReportBean {

    /**
     * date : 2019-05-14
     * turnovertotal : 5000
     * credit : 7000
     * cash : 0
     * alipay : 0
     * wechatpay : 0
     * voucher : 0
     * refund : 4850
     * goldturnover : 10950
     * golditemcount : 11
     * golditemweight : 12.1
     * cystalturnover : 0
     * cystalitemcount : 0
     * braceletturnover : 0
     * braceletitemcount : 0
     * items : [{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"95折","discountprice":950},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000}]
     * refunditem : [{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000},{"stock":"10013333","productname":"測試1","quantity":1,"catalog":"黃金","weight":1.1,"price":1000,"discount":"沒折扣","discountprice":1000}]
     */

    private String date;
    private int turnovertotal;
    private int credit;
    private int cash;
    private int alipay;
    private int wechatpay;
    private int voucher;
    private int refund;
    private int goldturnover;
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

    public int getTurnovertotal() {
        return turnovertotal;
    }

    public void setTurnovertotal(int turnovertotal) {
        this.turnovertotal = turnovertotal;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
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

    public int getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
        this.voucher = voucher;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public int getGoldturnover() {
        return goldturnover;
    }

    public void setGoldturnover(int goldturnover) {
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
         * stock : 10013333
         * productname : 測試1
         * quantity : 1
         * catalog : 黃金
         * weight : 1.1
         * price : 1000
         * discount : 95折
         * discountprice : 950
         */

        private String stock;
        private String productname;
        private int quantity;
        private String catalog;
        private double weight;
        private int price;
        private String discount;
        private int discountprice;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getDiscountprice() {
            return discountprice;
        }

        public void setDiscountprice(int discountprice) {
            this.discountprice = discountprice;
        }
    }

    public static class RefunditemBean {
        /**
         * stock : 10013333
         * productname : 測試1
         * quantity : 1
         * catalog : 黃金
         * weight : 1.1
         * price : 1000
         * discount : 沒折扣
         * discountprice : 1000
         */

        private String stock;
        private String productname;
        private int quantity;
        private String catalog;
        private double weight;
        private int price;
        private String discount;
        private int discountprice;

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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getDiscountprice() {
            return discountprice;
        }

        public void setDiscountprice(int discountprice) {
            this.discountprice = discountprice;
        }
    }
}
