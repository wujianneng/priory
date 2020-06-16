package com.pos.priory.beans;

import java.util.List;

public class FittingBean {

    /**
     * count : 3
     * next : null
     * previous : null
     * results : [{"id":2,"status":true,"name":"測試配件1002","image":"http://annabella.infitack.cn/media/uploads/test_S4JJNon.jfif","category":2,"productcode":1002,"price":[{"price":50,"currencycode":"MOP","symbol":"$"}],"available":true,"whitem":[{"id":2,"whnumber":"201903170002","weight":1.1},{"id":9,"whnumber":"202003270003","weight":0}],"created":"2020-03-16 18:30:04","updated":"2020-03-26 17:54:19"},{"id":3,"status":true,"name":"回收產品1","image":"http://annabella.infitack.cn/media/uploads/test_S4JJNon_PyJosnu.jfif","category":1,"productcode":9999,"price":[],"available":true,"whitem":[],"created":"2020-04-10 08:01:46","updated":"2020-04-10 08:01:46"},{"id":1,"status":true,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","category":1,"productcode":1001,"price":[{"price":100,"currencycode":"MOP","symbol":"$"}],"available":true,"whitem":[{"id":1,"whnumber":"201903170001","weight":1},{"id":3,"whnumber":"201903210001","weight":1.1},{"id":7,"whnumber":"202003270001","weight":1.1},{"id":8,"whnumber":"202003270002","weight":1.2},{"id":6,"whnumber":"","weight":0},{"id":5,"whnumber":"","weight":11}],"created":"2020-03-16 17:47:34","updated":"2020-04-23 04:59:40"}]
     */

    private int count;
    private Object next;
    private Object previous;
    private List<ResultsBean> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : 2
         * status : true
         * name : 測試配件1002
         * image : http://annabella.infitack.cn/media/uploads/test_S4JJNon.jfif
         * category : 2
         * productcode : 1002
         * price : [{"price":50,"currencycode":"MOP","symbol":"$"}]
         * available : true
         * whitem : [{"id":2,"whnumber":"201903170002","weight":1.1},{"id":9,"whnumber":"202003270003","weight":0}]
         * created : 2020-03-16 18:30:04
         * updated : 2020-03-26 17:54:19
         */

        private int id;
        private boolean status;
        private String name;
        private String image;
        private int category;
        private int productcode;
        private boolean available;
        private String created;
        private String updated;
        private List<PriceBean> price;
        private List<WhitemBean> whitem;
        private boolean isSelected;
        private int buyCount = 1;

        public int getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(int buyCount) {
            this.buyCount = buyCount;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getProductcode() {
            return productcode;
        }

        public void setProductcode(int productcode) {
            this.productcode = productcode;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
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

        public List<PriceBean> getPrice() {
            return price;
        }

        public void setPrice(List<PriceBean> price) {
            this.price = price;
        }

        public List<WhitemBean> getWhitem() {
            return whitem;
        }

        public void setWhitem(List<WhitemBean> whitem) {
            this.whitem = whitem;
        }

        public static class PriceBean {
            /**
             * price : 50.0
             * currencycode : MOP
             * symbol : $
             */

            private double price;
            private String currencycode;
            private String symbol;

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getCurrencycode() {
                return currencycode;
            }

            public void setCurrencycode(String currencycode) {
                this.currencycode = currencycode;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }
        }

        public static class WhitemBean {
            /**
             * id : 2
             * whnumber : 201903170002
             * weight : 1.1
             */

            private int id;
            private String whnumber;
            private double weight;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getWhnumber() {
                return whnumber;
            }

            public void setWhnumber(String whnumber) {
                this.whnumber = whnumber;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }
        }
    }
}
