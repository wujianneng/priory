package com.pos.priory.beans;

import java.util.List;

public class DayReportBean {


    /**
     * id : 68
     * dayend_item : [{"id":1163,"status":true,"created":"2021-03-02T17:35:48.178154+08:00","updated":"2021-03-02T17:35:48.178163+08:00","dayend_type":0,"price":100,"originprice":100,"quantity":1,"weight":0,"dayend_table":68,"whitem":3246},{"id":1164,"status":true,"created":"2021-03-02T17:35:48.181231+08:00","updated":"2021-03-02T17:35:48.181240+08:00","dayend_type":0,"price":1234.05,"originprice":1299,"quantity":1,"weight":1.94,"dayend_table":68,"whitem":10711},{"id":1165,"status":true,"created":"2021-03-02T17:35:48.184139+08:00","updated":"2021-03-02T17:35:48.184148+08:00","dayend_type":0,"price":664.05,"originprice":699,"quantity":1,"weight":1.09,"dayend_table":68,"whitem":25179},{"id":1166,"status":true,"created":"2021-03-02T17:35:48.186978+08:00","updated":"2021-03-02T17:35:48.186987+08:00","dayend_type":0,"price":474.05,"originprice":499,"quantity":1,"weight":0.41,"dayend_table":68,"whitem":43336},{"id":1167,"status":true,"created":"2021-03-02T17:35:48.189975+08:00","updated":"2021-03-02T17:35:48.189984+08:00","dayend_type":0,"price":474.05,"originprice":499,"quantity":1,"weight":0.45,"dayend_table":68,"whitem":35767},{"id":1168,"status":true,"created":"2021-03-02T17:35:48.192807+08:00","updated":"2021-03-02T17:35:48.192815+08:00","dayend_type":0,"price":499,"originprice":499,"quantity":1,"weight":0.5,"dayend_table":68,"whitem":15047},{"id":1169,"status":true,"created":"2021-03-02T17:35:48.195710+08:00","updated":"2021-03-02T17:35:48.195719+08:00","dayend_type":0,"price":50,"originprice":50,"quantity":1,"weight":0,"dayend_table":68,"whitem":3180}]
     * status : true
     * created : 2021-03-02T17:35:48.174532+08:00
     * updated : 2021-03-02T17:35:48.174545+08:00
     * dayend_no : 0042021030202
     * dayendno_num : 2
     * dayend_date : 2021-03-02
     * invoice_amount : 3495.0
     * cash : 3495.0
     * e_pay : 0.0
     * cashcoupon : 0.0
     * other_pay : 0.0
     * manual_exchange : 0.0
     * exchange : 0.0
     * returned : 0.0
     * return_goldprice : 0.0
     * shop : 7
     */

    private int id;
    private boolean status;
    private String created;
    private String updated;
    private String dayend_no;
    private int dayendno_num;
    private String dayend_date;
    private double invoice_amount;
    private double cash;
    private double e_pay;
    private double cashcoupon;
    private double other_pay;
    private double manual_exchange;
    private double exchange;
    private double returned;
    private double return_goldprice;
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

    public double getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(double invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getE_pay() {
        return e_pay;
    }

    public void setE_pay(double e_pay) {
        this.e_pay = e_pay;
    }

    public double getCashcoupon() {
        return cashcoupon;
    }

    public void setCashcoupon(double cashcoupon) {
        this.cashcoupon = cashcoupon;
    }

    public double getOther_pay() {
        return other_pay;
    }

    public void setOther_pay(double other_pay) {
        this.other_pay = other_pay;
    }

    public double getManual_exchange() {
        return manual_exchange;
    }

    public void setManual_exchange(double manual_exchange) {
        this.manual_exchange = manual_exchange;
    }

    public double getExchange() {
        return exchange;
    }

    public void setExchange(double exchange) {
        this.exchange = exchange;
    }

    public double getReturned() {
        return returned;
    }

    public void setReturned(double returned) {
        this.returned = returned;
    }

    public double getReturn_goldprice() {
        return return_goldprice;
    }

    public void setReturn_goldprice(double return_goldprice) {
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
         * id : 1163
         * status : true
         * created : 2021-03-02T17:35:48.178154+08:00
         * updated : 2021-03-02T17:35:48.178163+08:00
         * dayend_type : 0
         * price : 100.0
         * originprice : 100.0
         * quantity : 1
         * weight : 0.0
         * dayend_table : 68
         * whitem : 3246
         */

        private int id;
        private boolean status;
        private String created;
        private String updated;
        private int dayend_type;
        private double price;
        private double originprice;
        private int quantity;
        private double weight;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getOriginprice() {
            return originprice;
        }

        public void setOriginprice(double originprice) {
            this.originprice = originprice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
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
