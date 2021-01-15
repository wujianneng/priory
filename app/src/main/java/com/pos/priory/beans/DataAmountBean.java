package com.pos.priory.beans;

import java.util.List;

public class DataAmountBean {

    /**
     * shop : 澳門測試店
     * currency : MOP
     * symbol : $
     * data : [{"date":"2021-01-07","item":[{"category":"手繩","weight":0,"quanity":4,"turnover":61},{"category":"黃金","weight":0,"quanity":0,"turnover":0}],"payment":[{"method":"現金","amount":30}]}]
     */

    private String shop;
    private String currency;
    private String symbol;
    private List<DataBean> data;

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * date : 2021-01-07
         * item : [{"category":"手繩","weight":0,"quanity":4,"turnover":61},{"category":"黃金","weight":0,"quanity":0,"turnover":0}]
         * payment : [{"method":"現金","amount":30}]
         */

        boolean isExpand = false;
        private String date;
        private List<ItemBean> item;
        private List<PaymentBean> payment;

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public List<PaymentBean> getPayment() {
            return payment;
        }

        public void setPayment(List<PaymentBean> payment) {
            this.payment = payment;
        }

        public static class ItemBean {
            /**
             * category : 手繩
             * weight : 0
             * quanity : 4
             * turnover : 61.0
             */

            private String category;
            private double weight;
            private int quanity;
            private double turnover;

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getQuanity() {
                return quanity;
            }

            public void setQuanity(int quanity) {
                this.quanity = quanity;
            }

            public double getTurnover() {
                return turnover;
            }

            public void setTurnover(double turnover) {
                this.turnover = turnover;
            }
        }

        public static class PaymentBean {
            /**
             * method : 現金
             * amount : 30.0
             */

            private String method;
            private double amount;

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }
        }
    }
}
