package com.pos.priory.beans;

import java.util.List;

public class ExchangeCashCouponBean {

    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"id":1,"name":"积分现金1粉300减10000分换取","reducereward":10000,"value":"300.00"}]
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
         * name : 积分现金1粉300减10000分换取
         * reducereward : 10000
         * value : 300.00
         */

        private int id;
        private String name;
        private int reducereward;
        private String value;
        int count = 1;
        boolean isSelected;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getReducereward() {
            return reducereward;
        }

        public void setReducereward(int reducereward) {
            this.reducereward = reducereward;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
