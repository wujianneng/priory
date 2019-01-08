package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/7.
 */

public class CreateOrderResultBean {

    /**
     * id : 252
     * ordernumber : 1546838004333
     * member : {"id":9,"mobile":"15019840888","first_name":"DG","last_name":"and","sex":"男","reward":0}
     * staff : staff01
     * status : draft
     * totalprice : 0
     * discount : 1.00
     * fixdiscount : 0.00
     * location : 高士德
     * created : 2019-01-07T13:13:23.939761+08:00
     */

    private int id;
    private String ordernumber;
    private MemberBean member;
    private String staff;
    private String status;
    private int totalprice;
    private String discount;
    private String fixdiscount;
    private String location;
    private String created;

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

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
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

    public static class MemberBean {
        /**
         * id : 9
         * mobile : 15019840888
         * first_name : DG
         * last_name : and
         * sex : 男
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
