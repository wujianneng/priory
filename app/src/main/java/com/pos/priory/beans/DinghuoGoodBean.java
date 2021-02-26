package com.pos.priory.beans;

import java.util.List;

public class DinghuoGoodBean {


    /**
     * code : 200
     * msg : OK
     * level : info
     * result : [{"id":5,"purchase_id":14,"name":"千足金閃閃生輝D","productcode":1004,"price":299,"category":"黃金","image":"http://annabella.infitack.cn/media/uploads/0004.jpg","quantity":5,"weight":0.87,"created":"2021-01-15 16:28:01","updated":"2021-01-15 16:30:55"},{"id":6,"purchase_id":14,"name":"測試","productcode":1009,"price":299,"category":"黃金","image":"http://annabella.infitack.cn/media/uploads/0a508d0a-b3c1-4f92-a3ae-f190210db52f.jpg","quantity":1,"weight":4,"created":"2021-01-15 16:31:08","updated":"2021-01-15 16:31:08"}]
     */

    private int code;
    private String msg;
    private String level;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 5
         * purchase_id : 14
         * name : 千足金閃閃生輝D
         * productcode : 1004
         * price : 299.0
         * category : 黃金
         * image : http://annabella.infitack.cn/media/uploads/0004.jpg
         * quantity : 5
         * weight : 0.87
         * created : 2021-01-15 16:28:01
         * updated : 2021-01-15 16:30:55
         */

        private int id;
        private int purchase_id;
        private String name;
        private String productcode;
        private double price;
        private String category;
        private String image;
        private int quantity;
        private double weight;
        private String created;
        private String updated;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPurchase_id() {
            return purchase_id;
        }

        public void setPurchase_id(int purchase_id) {
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
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
