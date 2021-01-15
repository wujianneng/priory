package com.pos.priory.beans;

import java.util.List;

public class DayReportDetailBean {

    /**
     * id : 0
     * gold_item : {"amount_total":0,"quantity_total":0,"weight_total":0,"item":[{"product_name":"string","code":"string","weight":0,"quantity":0}],"price":0,"price_total":0}
     * spar_item : {"amount_total":0,"quantity_total":0,"weight_total":0,"item":[{"product_name":"string","code":"string","weight":0,"quantity":0}],"price":0,"price_total":0}
     * accessory_item : {"amount_total":0,"quantity_total":0,"weight_total":0,"item":[{"product_name":"string","code":"string","weight":0,"quantity":0}],"price":0,"price_total":0}
     * exchange_item : {"amount_total":0,"quantity_total":0,"weight_total":0,"item":[{"product_name":"string","code":"string","weight":0,"quantity":0}],"price":0,"price_total":0}
     * returned_item : {"amount_total":0,"quantity_total":0,"weight_total":0,"item":[{"product_name":"string","code":"string","weight":0,"quantity":0}],"price":0,"return_goldpricd":0,"price_total":0}
     * pay_amount : 0
     * status : true
     * created : 2021-01-14T09:47:08.561Z
     * updated : 2021-01-14T09:47:08.561Z
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
    private GoldItemBean gold_item;
    private SparItemBean spar_item;
    private AccessoryItemBean accessory_item;
    private ExchangeItemBean exchange_item;
    private ReturnedItemBean returned_item;
    private double pay_amount;
    private boolean status;
    private String created;
    private String updated;
    private String dayend_no;
    private int dayendno_num;
    private String dayend_date;
    private double invoice_amount;
    private double cash;
    private double credit_card;
    private double alipay;
    private double wechatpay;
    private double cashcoupon;
    private double other_pay;
    private double exchange;
    private double returned;
    private double return_goldprice;
    private int shop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GoldItemBean getGold_item() {
        return gold_item;
    }

    public void setGold_item(GoldItemBean gold_item) {
        this.gold_item = gold_item;
    }

    public SparItemBean getSpar_item() {
        return spar_item;
    }

    public void setSpar_item(SparItemBean spar_item) {
        this.spar_item = spar_item;
    }

    public AccessoryItemBean getAccessory_item() {
        return accessory_item;
    }

    public void setAccessory_item(AccessoryItemBean accessory_item) {
        this.accessory_item = accessory_item;
    }

    public ExchangeItemBean getExchange_item() {
        return exchange_item;
    }

    public void setExchange_item(ExchangeItemBean exchange_item) {
        this.exchange_item = exchange_item;
    }

    public ReturnedItemBean getReturned_item() {
        return returned_item;
    }

    public void setReturned_item(ReturnedItemBean returned_item) {
        this.returned_item = returned_item;
    }

    public double getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(double pay_amount) {
        this.pay_amount = pay_amount;
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

    public double getCredit_card() {
        return credit_card;
    }

    public void setCredit_card(double credit_card) {
        this.credit_card = credit_card;
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

    public void setWechatpay(double wechatpay) {
        this.wechatpay = wechatpay;
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

    public static class GoldItemBean {
        /**
         * amount_total : 0
         * quantity_total : 0
         * weight_total : 0
         * item : [{"product_name":"string","code":"string","weight":0,"quantity":0}]
         * price : 0
         * price_total : 0
         */

        private double amount_total;
        private int quantity_total;
        private double weight_total;
        private double price;
        private double price_total;
        private List<ItemBean> item;

        public double getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(double amount_total) {
            this.amount_total = amount_total;
        }

        public int getQuantity_total() {
            return quantity_total;
        }

        public void setQuantity_total(int quantity_total) {
            this.quantity_total = quantity_total;
        }

        public double getWeight_total() {
            return weight_total;
        }

        public void setWeight_total(double weight_total) {
            this.weight_total = weight_total;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPrice_total() {
            return price_total;
        }

        public void setPrice_total(int price_total) {
            this.price_total = price_total;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * product_name : string
             * code : string
             * weight : 0
             * quantity : 0
             */

            private String product_name;
            private String code;
            private double weight;
            private int quantity;

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }
    }

    public static class SparItemBean {
        /**
         * amount_total : 0
         * quantity_total : 0
         * weight_total : 0
         * item : [{"product_name":"string","code":"string","weight":0,"quantity":0}]
         * price : 0
         * price_total : 0
         */

        private double amount_total;
        private int quantity_total;
        private double weight_total;
        private double price;
        private double price_total;
        private List<ItemBeanX> item;

        public double getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(double amount_total) {
            this.amount_total = amount_total;
        }

        public int getQuantity_total() {
            return quantity_total;
        }

        public void setQuantity_total(int quantity_total) {
            this.quantity_total = quantity_total;
        }

        public double getWeight_total() {
            return weight_total;
        }

        public void setWeight_total(double weight_total) {
            this.weight_total = weight_total;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPrice_total() {
            return price_total;
        }

        public void setPrice_total(double price_total) {
            this.price_total = price_total;
        }

        public List<ItemBeanX> getItem() {
            return item;
        }

        public void setItem(List<ItemBeanX> item) {
            this.item = item;
        }

        public static class ItemBeanX {
            /**
             * product_name : string
             * code : string
             * weight : 0
             * quantity : 0
             */

            private String product_name;
            private String code;
            private double weight;
            private int quantity;

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }
    }

    public static class AccessoryItemBean {
        /**
         * amount_total : 0
         * quantity_total : 0
         * weight_total : 0
         * item : [{"product_name":"string","code":"string","weight":0,"quantity":0}]
         * price : 0
         * price_total : 0
         */

        private double amount_total;
        private int quantity_total;
        private double weight_total;
        private double price;
        private double price_total;
        private List<ItemBeanXX> item;

        public double getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(double amount_total) {
            this.amount_total = amount_total;
        }

        public int getQuantity_total() {
            return quantity_total;
        }

        public void setQuantity_total(int quantity_total) {
            this.quantity_total = quantity_total;
        }

        public double getWeight_total() {
            return weight_total;
        }

        public void setWeight_total(double weight_total) {
            this.weight_total = weight_total;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPrice_total() {
            return price_total;
        }

        public void setPrice_total(double price_total) {
            this.price_total = price_total;
        }

        public List<ItemBeanXX> getItem() {
            return item;
        }

        public void setItem(List<ItemBeanXX> item) {
            this.item = item;
        }

        public static class ItemBeanXX {
            /**
             * product_name : string
             * code : string
             * weight : 0
             * quantity : 0
             */

            private String product_name;
            private String code;
            private double weight;
            private int quantity;

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }
    }

    public static class ExchangeItemBean {
        /**
         * amount_total : 0
         * quantity_total : 0
         * weight_total : 0
         * item : [{"product_name":"string","code":"string","weight":0,"quantity":0}]
         * price : 0
         * price_total : 0
         */

        private double amount_total;
        private int quantity_total;
        private double weight_total;
        private double price;
        private double price_total;
        private List<ItemBeanXXX> item;

        public double getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(double amount_total) {
            this.amount_total = amount_total;
        }

        public int getQuantity_total() {
            return quantity_total;
        }

        public void setQuantity_total(int quantity_total) {
            this.quantity_total = quantity_total;
        }

        public double getWeight_total() {
            return weight_total;
        }

        public void setWeight_total(double weight_total) {
            this.weight_total = weight_total;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPrice_total() {
            return price_total;
        }

        public void setPrice_total(double price_total) {
            this.price_total = price_total;
        }

        public List<ItemBeanXXX> getItem() {
            return item;
        }

        public void setItem(List<ItemBeanXXX> item) {
            this.item = item;
        }

        public static class ItemBeanXXX {
            /**
             * product_name : string
             * code : string
             * weight : 0
             * quantity : 0
             */

            private String product_name;
            private String code;
            private double weight;
            private int quantity;

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }
    }

    public static class ReturnedItemBean {
        /**
         * amount_total : 0
         * quantity_total : 0
         * weight_total : 0
         * item : [{"product_name":"string","code":"string","weight":0,"quantity":0}]
         * price : 0
         * return_goldpricd : 0
         * price_total : 0
         */

        private double amount_total;
        private int quantity_total;
        private double weight_total;
        private double price;
        private double return_goldpricd;
        private double price_total;
        private List<ItemBeanXXXX> item;

        public double getAmount_total() {
            return amount_total;
        }

        public void setAmount_total(double amount_total) {
            this.amount_total = amount_total;
        }

        public int getQuantity_total() {
            return quantity_total;
        }

        public void setQuantity_total(int quantity_total) {
            this.quantity_total = quantity_total;
        }

        public double getWeight_total() {
            return weight_total;
        }

        public void setWeight_total(double weight_total) {
            this.weight_total = weight_total;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getReturn_goldpricd() {
            return return_goldpricd;
        }

        public void setReturn_goldpricd(double return_goldpricd) {
            this.return_goldpricd = return_goldpricd;
        }

        public double getPrice_total() {
            return price_total;
        }

        public void setPrice_total(int price_total) {
            this.price_total = price_total;
        }

        public List<ItemBeanXXXX> getItem() {
            return item;
        }

        public void setItem(List<ItemBeanXXXX> item) {
            this.item = item;
        }

        public static class ItemBeanXXXX {
            /**
             * product_name : string
             * code : string
             * weight : 0
             * quantity : 0
             */

            private String product_name;
            private String code;
            private double weight;
            private int quantity;

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }
    }
}
