package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class OrderBean {

    /**
     * id : 7
     * ordernumber : 12345
     * member : {"id":1,"mobile":"66236133","first_name":"","last_name":"Lam","sex":"M","reward":0}
     * staff : 1
     * status : 未完成
     * items : []
     * totalprice : 0
     * discount : 1.000000
     * fixdiscount : 0.00
     * location : 调试
     * created : 2019-04-26T15:38:19.301616+08:00
     */

    private int id;
    private String ordernumber;
    private MemberBean member;
    private int staff;
    private String status;
    private int totalprice;
    private String discount;
    private String fixdiscount;
    private String location;
    private String created;
    private List<OrderItemBean> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFixdiscount() {
        return fixdiscount;
    }

    public void setFixdiscount(String fixdiscount) {
        this.fixdiscount = fixdiscount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<OrderItemBean> getItems() {
        return items;
    }

    public void setItems(List<OrderItemBean> items) {
        this.items = items;
    }

    public static class OrderItemBean{
        private int id;
        private int stock;
        private String discount;
        private String fixdiscount;
        private String price;
        private boolean returned;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getFixdiscount() {
            return fixdiscount;
        }

        public void setFixdiscount(String fixdiscount) {
            this.fixdiscount = fixdiscount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isReturned() {
            return returned;
        }

        public void setReturned(boolean returned) {
            this.returned = returned;
        }
    }

    public static class MemberBean {
        /**
         * id : 1
         * mobile : 66236133
         * first_name :
         * last_name : Lam
         * sex : M
         * reward : 0
         */

        private int id;
        private String mobile;
        private String first_name;
        private String last_name;
        private String sex;
        private int reward;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }
    }
}
