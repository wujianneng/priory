package com.pos.priory.beans;

import java.util.List;

public class DataCountBean {

    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"id":3,"sales_quantity":1,"sales_amount":199,"sales_price":199,"status":true,"created":"2020-08-14T10:48:03.784793+08:00","updated":"2020-08-14T10:48:03.784827+08:00","name":"千足金闪闪生辉E","namezh":"","image":"http://annabella.infitack.cn/media/uploads/0005.jpg","productcode":1005,"weight":0,"returned":false,"available":true,"remark":"","category":1}]
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
         * id : 3
         * sales_quantity : 1
         * sales_amount : 199.0
         * sales_price : 199.0
         * status : true
         * created : 2020-08-14T10:48:03.784793+08:00
         * updated : 2020-08-14T10:48:03.784827+08:00
         * name : 千足金闪闪生辉E
         * namezh :
         * image : http://annabella.infitack.cn/media/uploads/0005.jpg
         * productcode : 1005
         * weight : 0.0
         * returned : false
         * available : true
         * remark :
         * category : 1
         */

        private int id;
        private int sales_quantity;
        private double sales_amount;
        private double sales_price;
        private boolean status;
        private String created;
        private String updated;
        private String name;
        private String namezh;
        private String image;
        private int productcode;
        private double weight;
        private boolean returned;
        private boolean available;
        private String remark;
        private int category;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSales_quantity() {
            return sales_quantity;
        }

        public void setSales_quantity(int sales_quantity) {
            this.sales_quantity = sales_quantity;
        }

        public double getSales_amount() {
            return sales_amount;
        }

        public void setSales_amount(double sales_amount) {
            this.sales_amount = sales_amount;
        }

        public double getSales_price() {
            return sales_price;
        }

        public void setSales_price(double sales_price) {
            this.sales_price = sales_price;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNamezh() {
            return namezh;
        }

        public void setNamezh(String namezh) {
            this.namezh = namezh;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getProductcode() {
            return productcode;
        }

        public void setProductcode(int productcode) {
            this.productcode = productcode;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public boolean isReturned() {
            return returned;
        }

        public void setReturned(boolean returned) {
            this.returned = returned;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }
    }
}
