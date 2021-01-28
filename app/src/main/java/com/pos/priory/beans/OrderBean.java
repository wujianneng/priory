package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class OrderBean{


    /**
     * count : 8
     * next : null
     * previous : null
     * date : 2021-01-07
     * gold_price_gram : 0
     * gold_price : 0
     * results : [{"id":68,"member_firstname":"","member_lastname":"測試會員1","member_mobile":"66668881","member_gender":"1","amount":0,"created":"2021-01-07 10:45:36","updated":"2021-01-07 10:45:36","orderno":"20210107104536","status":true,"remark":"","member":2,"staff":2,"shop":1},{"id":67,"member_firstname":"","member_lastname":"測試會員1","member_mobile":"66668881","member_gender":"1","amount":0,"created":"2021-01-07 10:43:12","updated":"2021-01-07 10:43:12","orderno":"20210107104312","status":true,"remark":"","member":2,"staff":2,"shop":1},{"id":66,"member_firstname":"","member_lastname":"測試會員1","member_mobile":"66668881","member_gender":"1","amount":0,"created":"2021-01-06 17:54:17","updated":"2021-01-06 17:54:17","orderno":"20210106175417","status":true,"remark":"","member":2,"staff":2,"shop":1},{"id":69,"member_firstname":"","member_lastname":"測試會員2","member_mobile":"66668882","member_gender":"1","amount":0,"created":"2021-01-07 10:56:49","updated":"2021-01-07 10:56:49","orderno":"20210107105649","status":true,"remark":"","member":3,"staff":2,"shop":1},{"id":70,"member_firstname":"","member_lastname":"測試會員3","member_mobile":"66668883","member_gender":"1","amount":0,"created":"2021-01-07 10:58:04","updated":"2021-01-07 10:58:04","orderno":"20210107105804","status":true,"remark":"","member":4,"staff":2,"shop":1},{"id":61,"member_firstname":"","member_lastname":"測試會員0","member_mobile":"66668880","member_gender":"1","amount":0,"created":"2021-01-05 10:39:52","updated":"2021-01-05 10:39:52","orderno":"20210105103952","status":true,"remark":"","member":1,"staff":12,"shop":1},{"id":16,"member_firstname":"","member_lastname":"測試會員0","member_mobile":"66668880","member_gender":"1","amount":0,"created":"2020-12-30 17:35:17","updated":"2020-12-30 17:35:17","orderno":"20201230173517","status":true,"remark":"","member":1,"staff":12,"shop":1},{"id":50,"member_firstname":"","member_lastname":"測試會員0","member_mobile":"66668880","member_gender":"1","amount":0,"created":"2021-01-04 17:50:43","updated":"2021-01-04 17:50:43","orderno":"20210104175043","status":true,"remark":"","member":1,"staff":12,"shop":1}]
     */

    private int count;
    private Object next;
    private Object previous;
    private String date;
    private double gram_goldprice;
    private double tael_goldprice;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getGram_goldprice() {
        return gram_goldprice;
    }

    public void setGram_goldprice(double gram_goldprice) {
        this.gram_goldprice = gram_goldprice;
    }

    public double getTael_goldprice() {
        return tael_goldprice;
    }

    public void setTael_goldprice(double tael_goldprice) {
        this.tael_goldprice = tael_goldprice;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : 68
         * member_firstname :
         * member_lastname : 測試會員1
         * member_mobile : 66668881
         * member_gender : 1
         * amount : 0.0
         * created : 2021-01-07 10:45:36
         * updated : 2021-01-07 10:45:36
         * orderno : 20210107104536
         * status : true
         * remark :
         * member : 2
         * staff : 2
         * shop : 1
         */

        private int id;
        private String member_firstname;
        private String member_lastname;
        private String member_mobile;
        private String member_gender;
        private String order_status_display;
        private double amount;
        private String created;
        private String updated;
        private String orderno;
        private boolean status;
        private String remark;
        private int member;
        private int staff;
        private int shop;

        public String getOrder_status_display() {
            return order_status_display;
        }

        public void setOrder_status_display(String order_status_display) {
            this.order_status_display = order_status_display;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMember_firstname() {
            return member_firstname;
        }

        public void setMember_firstname(String member_firstname) {
            this.member_firstname = member_firstname;
        }

        public String getMember_lastname() {
            return member_lastname;
        }

        public void setMember_lastname(String member_lastname) {
            this.member_lastname = member_lastname;
        }

        public String getMember_mobile() {
            return member_mobile;
        }

        public void setMember_mobile(String member_mobile) {
            this.member_mobile = member_mobile;
        }

        public String getMember_gender() {
            return member_gender;
        }

        public void setMember_gender(String member_gender) {
            this.member_gender = member_gender;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
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

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getMember() {
            return member;
        }

        public void setMember(int member) {
            this.member = member;
        }

        public int getStaff() {
            return staff;
        }

        public void setStaff(int staff) {
            this.staff = staff;
        }

        public int getShop() {
            return shop;
        }

        public void setShop(int shop) {
            this.shop = shop;
        }
    }
}
