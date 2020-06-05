package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class MemberBean {


    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"id":1,"status":true,"name":"林","mobile":"12345678","gender":"男","shop":"測試店","order":[1,2],"reward":0,"created":"2020-04-20 05:22:55","updated":"2020-04-20 05:22:55"}]
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
         * id : 1
         * status : true
         * name : 林
         * mobile : 12345678
         * gender : 男
         * shop : 測試店
         * order : [1,2]
         * reward : 0
         * created : 2020-04-20 05:22:55
         * updated : 2020-04-20 05:22:55
         */

        private int id;
        private boolean status;
        private String name;
        private String mobile;
        private String gender;
        private String shop;
        private int reward;
        private String created;
        private String updated;
        private List<Integer> order;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
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

        public List<Integer> getOrder() {
            return order;
        }

        public void setOrder(List<Integer> order) {
            this.order = order;
        }
    }
}
