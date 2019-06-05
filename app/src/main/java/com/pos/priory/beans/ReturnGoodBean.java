package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/6.
 */

public class ReturnGoodBean {


    /**
     * goldcount : 2
     * goldweight : 4.1
     * returnstockitem : [{"id":8,"location":"测试店鋪","name":"黃金金","weight":"1.10","status":false,"order":"20190529172527","returnprice":1,"created":"2019-06-05T10:09:01.878255+08:00"},{"id":7,"location":"测试店鋪","name":"黃金金","weight":"3.00","status":false,"order":"20190530164725","returnprice":1,"created":"2019-06-05T10:09:01.878255+08:00"}]
     */

    private int goldcount;
    private double goldweight;
    private List<ReturnstockitemBean> returnstockitem;

    public int getGoldcount() {
        return goldcount;
    }

    public void setGoldcount(int goldcount) {
        this.goldcount = goldcount;
    }

    public double getGoldweight() {
        return goldweight;
    }

    public void setGoldweight(double goldweight) {
        this.goldweight = goldweight;
    }

    public List<ReturnstockitemBean> getReturnstockitem() {
        return returnstockitem;
    }

    public void setReturnstockitem(List<ReturnstockitemBean> returnstockitem) {
        this.returnstockitem = returnstockitem;
    }

    public static class ReturnstockitemBean {
        /**
         * id : 8
         * location : 测试店鋪
         * name : 黃金金
         * weight : 1.10
         * status : false
         * order : 20190529172527
         * returnprice : 1.0
         * created : 2019-06-05T10:09:01.878255+08:00
         */

        private int id;
        private String location;
        private String name;
        private String weight;
        private boolean status;
        private String order;
        private double returnprice;
        private String created;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public double getReturnprice() {
            return returnprice;
        }

        public void setReturnprice(double returnprice) {
            this.returnprice = returnprice;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
