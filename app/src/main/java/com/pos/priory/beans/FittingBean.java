package com.pos.priory.beans;

import java.util.List;

public class FittingBean {


    /**
     * count : 3
     * next : null
     * previous : null
     * results : [{"id":6,"status":true,"name":"幸運30","image":"http://annabella.infitack.cn/media/uploads/icon_gBCAzqN.jpg","category":"手繩","productcode":1996,"price":[{"price":30,"currencycode":"MOP","symbol":"$"}],"available":true,"whitem":[{"id":5,"whnumber":"","weight":0}],"accessory":true,"created":"2020-08-14 11:19:43","updated":"2020-08-14 11:19:43"},{"id":7,"status":true,"name":"幸運50","image":"http://annabella.infitack.cn/media/uploads/icon_gBCAzqN_kIOAfTx.jpg","category":"手繩","productcode":1997,"price":[{"price":50,"currencycode":"MOP","symbol":"$"}],"available":true,"whitem":[{"id":6,"whnumber":"","weight":0}],"accessory":true,"created":"2020-08-14 11:21:14","updated":"2020-08-14 11:21:14"},{"id":8,"status":true,"name":"幸運100","image":"http://annabella.infitack.cn/media/uploads/icon_gBCAzqN_9BsOINN.jpg","category":"手繩","productcode":1998,"price":[{"price":100,"currencycode":"MOP","symbol":"$"}],"available":true,"whitem":[],"accessory":true,"created":"2020-08-14 11:21:38","updated":"2020-08-14 11:21:38"}]
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
         * id : 6
         * status : true
         * name : 幸運30
         * image : http://annabella.infitack.cn/media/uploads/icon_gBCAzqN.jpg
         * category : 手繩
         * productcode : 1996
         * price : [{"price":30,"currencycode":"MOP","symbol":"$"}]
         * available : true
         * whitem : [{"id":5,"whnumber":"","weight":0}]
         * accessory : true
         * created : 2020-08-14 11:19:43
         * updated : 2020-08-14 11:19:43
         */

        private int id;
        private boolean status;
        private String name;
        private String image;
        private String category;
        private int categoryId;
        private int productcode;
        private boolean available;
        private boolean accessory;
        private String created;
        private String updated;
        private List<PriceBean> price;
        private List<WhitemBean> whitem;
        private int buyCount = 1;
        private boolean isSelected = false;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(int buyCount) {
            this.buyCount = buyCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
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

        public boolean isAccessory() {
            return accessory;
        }

        public void setAccessory(boolean accessory) {
            this.accessory = accessory;
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
             * price : 30.0
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
             * id : 5
             * whnumber :
             * weight : 0.0
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
