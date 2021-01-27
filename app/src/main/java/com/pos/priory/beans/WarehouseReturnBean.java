package com.pos.priory.beans;

import java.util.List;

public class WarehouseReturnBean {

    /**
     * quantity_total : 7
     * weight_total : 51.89
     * count : 7
     * next : null
     * previous : null
     * results : [{"id":13,"prd_id":3,"prd_name":"千足金闪闪生辉E","prd_image":"http://annabella.infitack.cn/media/uploads/0005.jpg","prd_no":"1005122800000005","returncost":5000,"returnweight":50,"quantity":1,"returntype":"回收","updated":"2021-01-26 16:50:58"},{"id":58,"prd_id":2,"prd_name":"千足金闪闪生辉D","prd_image":"http://annabella.infitack.cn/media/uploads/0004.jpg","prd_no":"1004011811390521","returncost":425.7,"returnweight":0.99,"quantity":1,"returntype":"回收","updated":"2021-01-26 16:50:58"},{"id":249,"prd_id":26,"prd_name":"玉石333","prd_image":"http://annabella.infitack.cn/media/uploads/2030edd9-877c-429c-b299-583bd8eba4d5.jpg","prd_no":"1012012211453209","returncost":0,"returnweight":0,"quantity":1,"returntype":"換貨","updated":"2021-01-26 16:51:01"},{"id":250,"prd_id":26,"prd_name":"玉石333","prd_image":"http://annabella.infitack.cn/media/uploads/2030edd9-877c-429c-b299-583bd8eba4d5.jpg","prd_no":"1012012211453210","returncost":0,"returnweight":0,"quantity":1,"returntype":"換貨","updated":"2021-01-26 16:51:01"},{"id":288,"prd_id":26,"prd_name":"玉石333","prd_image":"http://annabella.infitack.cn/media/uploads/2030edd9-877c-429c-b299-583bd8eba4d5.jpg","prd_no":"1012012211453248","returncost":0,"returnweight":0,"quantity":1,"returntype":"換貨","updated":"2021-01-26 16:51:02"},{"id":239,"prd_id":25,"prd_name":"黃金999","prd_image":"http://annabella.infitack.cn/media/uploads/380cd7be-d74d-46d8-ba34-2103d0c03995.jpg","prd_no":"1011012211441349","returncost":0,"returnweight":0,"quantity":1,"returntype":"換貨","updated":"2021-01-26 16:51:03"},{"id":112,"prd_id":2,"prd_name":"千足金闪闪生辉D","prd_image":"http://annabella.infitack.cn/media/uploads/0004.jpg","prd_no":"1004011811390575","returncost":0,"returnweight":0.9,"quantity":1,"returntype":"回收","updated":"2021-01-26 16:51:03"}]
     */

    private int quantity_total;
    private double weight_total;
    private int count;
    private Object next;
    private Object previous;
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
         * id : 13
         * prd_id : 3
         * prd_name : 千足金闪闪生辉E
         * prd_image : http://annabella.infitack.cn/media/uploads/0005.jpg
         * prd_no : 1005122800000005
         * returncost : 5000.0
         * returnweight : 50.0
         * quantity : 1
         * returntype : 回收
         * updated : 2021-01-26 16:50:58
         */

        private int id;
        private int prd_id;
        private String prd_name;
        private String prd_image;
        private String prd_no;
        private double returncost;
        private double returnweight;
        private int quantity;
        private String returntype;
        private String updated;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPrd_id() {
            return prd_id;
        }

        public void setPrd_id(int prd_id) {
            this.prd_id = prd_id;
        }

        public String getPrd_name() {
            return prd_name;
        }

        public void setPrd_name(String prd_name) {
            this.prd_name = prd_name;
        }

        public String getPrd_image() {
            return prd_image;
        }

        public void setPrd_image(String prd_image) {
            this.prd_image = prd_image;
        }

        public String getPrd_no() {
            return prd_no;
        }

        public void setPrd_no(String prd_no) {
            this.prd_no = prd_no;
        }

        public double getReturncost() {
            return returncost;
        }

        public void setReturncost(double returncost) {
            this.returncost = returncost;
        }

        public double getReturnweight() {
            return returnweight;
        }

        public void setReturnweight(double returnweight) {
            this.returnweight = returnweight;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getReturntype() {
            return returntype;
        }

        public void setReturntype(String returntype) {
            this.returntype = returntype;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }
    }
}
