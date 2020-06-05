package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class InventoryBean {


    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"id":1,"status":true,"warehouse":"主倉庫","done":true,"count":"3 / 3","created":"2020-03-16 17:48:40","updated":"2020-03-26 09:39:45"}]
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
         * warehouse : 主倉庫
         * done : true
         * count : 3 / 3
         * created : 2020-03-16 17:48:40
         * updated : 2020-03-26 09:39:45
         */

        private int id;
        private boolean status;
        private String warehouse;
        private boolean done;
        private String count;
        private String created;
        private String updated;

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

        public String getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(String warehouse) {
            this.warehouse = warehouse;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
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
