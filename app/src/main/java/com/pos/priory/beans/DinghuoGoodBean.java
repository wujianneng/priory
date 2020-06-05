package com.pos.priory.beans;

import java.util.List;

public class DinghuoGoodBean {


    /**
     * count : 0
     * next : string
     * previous : string
     * results : [{"id":0,"purchase_id":"string","name":"string","productcode":"string","category":"string","image":"string","quantity":0,"weight":"string","created":"2020-03-27T04:36:54.085Z","updated":"2020-03-27T04:36:54.085Z"}]
     */

    private int count;
    private String next;
    private String previous;
    private List<ResultsBean> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
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
         * id : 0
         * purchase_id : string
         * name : string
         * productcode : string
         * category : string
         * image : string
         * quantity : 0
         * weight : string
         * created : 2020-03-27T04:36:54.085Z
         * updated : 2020-03-27T04:36:54.085Z
         */

        private double price;
        private boolean isSelected = false;
        private int id;
        private String purchase_id;
        private String name;
        private String productcode;
        private String category;
        private String image;
        private int quantity;
        private String weight;
        private String created;
        private String updated;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

        public String getPurchase_id() {
            return purchase_id;
        }

        public void setPurchase_id(String purchase_id) {
            this.purchase_id = purchase_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProductcode() {
            return productcode;
        }

        public void setProductcode(String productcode) {
            this.productcode = productcode;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
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
    }
}
