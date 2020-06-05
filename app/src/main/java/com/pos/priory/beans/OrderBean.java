package com.pos.priory.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class OrderBean{

    /**
     * count : 2
     * next : null
     * previous : null
     * results : [{"id":2,"status":1,"orderno":"20200420053529","member":{"name":"林","mobile":"12345678"},"staff":"","shop":"內測分店","totalprice":0,"created":"2020-04-20 05:34:32","updated":"2020-04-20 05:35:29"},{"id":1,"status":1,"orderno":"20200420053541","member":{"name":"林","mobile":"12345678"},"staff":"","shop":"測試店","totalprice":0,"created":"2020-04-20 05:32:30","updated":"2020-04-20 05:35:41"}]
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

    public static class ResultsBean implements MultiItemEntity {

        public int itemType = 0;

        @Override
        public int getItemType() {
            return itemType;
        }


        /**
         * id : 2
         * status : 1
         * orderno : 20200420053529
         * member : {"name":"林","mobile":"12345678"}
         * staff :
         * shop : 內測分店
         * totalprice : 0.0
         * created : 2020-04-20 05:34:32
         * updated : 2020-04-20 05:35:29
         */

        private int id;
        private int status;
        private String orderno;
        private MemberBean member;
        private String staff;
        private String shop;
        private double totalprice;
        private String created;
        private String updated;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
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

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public double getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(double totalprice) {
            this.totalprice = totalprice;
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

        public static class MemberBean {
            /**
             * name : 林
             * mobile : 12345678
             */

            private String name;
            private String mobile;
            private String gender;

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }
    }
}
