package com.pos.priory.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class DayReportDetailBean {


    /**
     * id : 69
     * pay_amount : 3495.0
     * product_items : [{"category_name":"黄金","amount_total":3345.2,"quantity_total":5,"weight_total":4.39,"return_goldpricd":0,"item":[{"product_name":"千足金链条心锁密钥","code":"1036083010363701","weight":1.94,"quantity":1,"price":1234.05,"price_total":1234.05},{"product_name":"千足金可爱-兔宝宝","code":"1441010217385701","weight":1.09,"quantity":1,"price":664.05,"price_total":664.05},{"product_name":"千足金满天星-字母C","code":"3520020420025802","weight":0.41,"quantity":1,"price":474.05,"price_total":474.05},{"product_name":"千足金满天星-字母I","code":"3526090415502203","weight":0.45,"quantity":1,"price":474.05,"price_total":474.05},{"product_name":"千足金财字锁 小","code":"2783092711200404","weight":0.5,"quantity":1,"price":499,"price_total":499}]},{"category_name":"配件","amount_total":150,"quantity_total":2,"weight_total":0,"return_goldpricd":0,"item":[{"product_name":"幸运100","code":"1998082216094608","weight":0,"quantity":1,"price":100,"price_total":100},{"product_name":"幸运50","code":"1997082216051402","weight":0,"quantity":1,"price":50,"price_total":50}]}]
     * shop_name : 關閘店(澳門)
     * status : true
     * created : 2021-03-02T17:38:53.962311+08:00
     * updated : 2021-03-02T17:38:53.962326+08:00
     * dayend_no : 0042021030203
     * dayendno_num : 3
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
    private double pay_amount;
    private String shop_name;
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
    private List<ProductItemsBean> product_items;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(double pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
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

    public List<ProductItemsBean> getProduct_items() {
        return product_items;
    }

    public void setProduct_items(List<ProductItemsBean> product_items) {
        this.product_items = product_items;
    }

    public static class ProductItemsBean implements MultiItemEntity {
        /**
         * category_name : 黄金
         * amount_total : 3345.2
         * quantity_total : 5
         * weight_total : 4.39
         * return_goldpricd : 0
         * item : [{"product_name":"千足金链条心锁密钥","code":"1036083010363701","weight":1.94,"quantity":1,"price":1234.05,"price_total":1234.05},{"product_name":"千足金可爱-兔宝宝","code":"1441010217385701","weight":1.09,"quantity":1,"price":664.05,"price_total":664.05},{"product_name":"千足金满天星-字母C","code":"3520020420025802","weight":0.41,"quantity":1,"price":474.05,"price_total":474.05},{"product_name":"千足金满天星-字母I","code":"3526090415502203","weight":0.45,"quantity":1,"price":474.05,"price_total":474.05},{"product_name":"千足金财字锁 小","code":"2783092711200404","weight":0.5,"quantity":1,"price":499,"price_total":499}]
         */
        public int itemType = 0;
        private String category_name;
        private double amount_total;
        private int quantity_total;
        private double weight_total;
        private int return_goldpricd;
        private List<ItemBean> item;

        @Override
        public int getItemType() {
            return itemType;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

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

        public int getReturn_goldpricd() {
            return return_goldpricd;
        }

        public void setReturn_goldpricd(int return_goldpricd) {
            this.return_goldpricd = return_goldpricd;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * product_name : 千足金链条心锁密钥
             * code : 1036083010363701
             * weight : 1.94
             * quantity : 1
             * price : 1234.05
             * price_total : 1234.05
             */

            private String product_name;
            private String code;
            private double weight;
            private int quantity;
            private double price;
            private double price_total;

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
        }
    }
}
