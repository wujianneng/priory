package com.pos.priory.beans;

import java.util.List;

public class DayReportBean {


    /**
     * id : 0
     * dayend_item : [{"id":0,"status":true,"created":"2021-01-14T09:34:32.417Z","updated":"2021-01-14T09:34:32.417Z","dayend_type":0,"price":0,"quantity":0,"weight":0,"dayend_table":0,"whitem":0}]
     * status : true
     * created : 2021-01-14T09:34:32.417Z
     * updated : 2021-01-14T09:34:32.417Z
     * dayend_no : string
     * dayendno_num : 0
     * dayend_date : 2021-01-14
     * invoice_amount : 0
     * cash : 0
     * credit_card : 0
     * alipay : 0
     * wechatpay : 0
     * cashcoupon : 0
     * other_pay : 0
     * exchange : 0
     * returned : 0
     * return_goldprice : 0
     * shop : 0
     */

    private int id;
    private boolean status;
    private String created;
    private String updated;
    private String dayend_no;
    private int dayendno_num;
    private String dayend_date;
    private int invoice_amount;
    private int cash;
    private int credit_card;
    private int alipay;
    private int wechatpay;
    private int cashcoupon;
    private int other_pay;
    private int exchange;
    private int returned;
    private int return_goldprice;
    private int shop;
    private List<DayendItemBean> dayend_item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDayend_no() {
        return dayend_no;
    }

    public void setDayend_no(String dayend_no) {
        this.dayend_no = dayend_no;
    }

    public int getDayendno_num() {
        return dayendno_num;
    }

    public void setDayendno_num(int dayendno_num) {
        this.dayendno_num = dayendno_num;
    }

    public String getDayend_date() {
        return dayend_date;
    }

    public void setDayend_date(String dayend_date) {
        this.dayend_date = dayend_date;
    }

    public int getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(int invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCredit_card() {
        return credit_card;
    }

    public void setCredit_card(int credit_card) {
        this.credit_card = credit_card;
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

    public int getCashcoupon() {
        return cashcoupon;
    }

    public void setCashcoupon(int cashcoupon) {
        this.cashcoupon = cashcoupon;
    }

    public int getOther_pay() {
        return other_pay;
    }

    public void setOther_pay(int other_pay) {
        this.other_pay = other_pay;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    public int getReturned() {
        return returned;
    }

    public void setReturned(int returned) {
        this.returned = returned;
    }

    public int getReturn_goldprice() {
        return return_goldprice;
    }

    public void setReturn_goldprice(int return_goldprice) {
        this.return_goldprice = return_goldprice;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public List<DayendItemBean> getDayend_item() {
        return dayend_item;
    }

    public void setDayend_item(List<DayendItemBean> dayend_item) {
        this.dayend_item = dayend_item;
    }

    public static class DayendItemBean {
        /**
         * id : 0
         * status : true
         * created : 2021-01-14T09:34:32.417Z
         * updated : 2021-01-14T09:34:32.417Z
         * dayend_type : 0
         * price : 0
         * quantity : 0
         * weight : 0
         * dayend_table : 0
         * whitem : 0
         */

        private int id;
        private boolean status;
        private String created;
        private String updated;
        private int dayend_type;
        private int price;
        private int quantity;
        private int weight;
        private int dayend_table;
        private int whitem;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public int getDayend_type() {
            return dayend_type;
        }

        public void setDayend_type(int dayend_type) {
            this.dayend_type = dayend_type;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getDayend_table() {
            return dayend_table;
        }

        public void setDayend_table(int dayend_table) {
            this.dayend_table = dayend_table;
        }

        public int getWhitem() {
            return whitem;
        }

        public void setWhitem(int whitem) {
            this.whitem = whitem;
        }
    }
}
