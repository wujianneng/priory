package com.pos.priory.beans;

import java.util.List;

public class RepertoryRecordBean {


    /**
     * quantity_total : 0
     * weight_total : 0
     * count : 0
     * next : string
     * previous : string
     * results : [{"id":0,"created":"2021-01-18T06:59:17.447Z","prd_name":"string","prd_no":"string","prd_weight":0,"shop_from":"string","type_display":"string","purpose_display":"string","username":"string","status":true,"updated":"2021-01-18T06:59:17.447Z","quantity":0,"type":1,"purpose":1,"user":0,"whitem":0,"warehouse":0,"whfrom":0}]
     */

    private int quantity_total;
    private double weight_total;
    private int count;
    private String next;
    private String previous;
    private List<ResultsBean> results;

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
         * created : 2021-01-18T06:59:17.447Z
         * prd_name : string
         * prd_no : string
         * prd_weight : 0
         * shop_from : string
         * type_display : string
         * purpose_display : string
         * username : string
         * status : true
         * updated : 2021-01-18T06:59:17.447Z
         * quantity : 0
         * type : 1
         * purpose : 1
         * user : 0
         * whitem : 0
         * warehouse : 0
         * whfrom : 0
         */

        private int id;
        private String created;
        private String prd_name;
        private String prd_no;
        private double prd_weight;
        private String shop_from;
        private String type_display;
        private String purpose_display;
        private String username;
        private boolean status;
        private String updated;
        private int quantity;
        private int type;
        private int purpose;
        private int user;
        private int whitem;
        private int warehouse;
        private int whfrom;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getPrd_name() {
            return prd_name;
        }

        public void setPrd_name(String prd_name) {
            this.prd_name = prd_name;
        }

        public String getPrd_no() {
            return prd_no;
        }

        public void setPrd_no(String prd_no) {
            this.prd_no = prd_no;
        }

        public double getPrd_weight() {
            return prd_weight;
        }

        public void setPrd_weight(double prd_weight) {
            this.prd_weight = prd_weight;
        }

        public String getShop_from() {
            return shop_from;
        }

        public void setShop_from(String shop_from) {
            this.shop_from = shop_from;
        }

        public String getType_display() {
            return type_display;
        }

        public void setType_display(String type_display) {
            this.type_display = type_display;
        }

        public String getPurpose_display() {
            return purpose_display;
        }

        public void setPurpose_display(String purpose_display) {
            this.purpose_display = purpose_display;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPurpose() {
            return purpose;
        }

        public void setPurpose(int purpose) {
            this.purpose = purpose;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public int getWhitem() {
            return whitem;
        }

        public void setWhitem(int whitem) {
            this.whitem = whitem;
        }

        public int getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(int warehouse) {
            this.warehouse = warehouse;
        }

        public int getWhfrom() {
            return whfrom;
        }

        public void setWhfrom(int whfrom) {
            this.whfrom = whfrom;
        }
    }
}
