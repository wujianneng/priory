package com.pos.priory.beans;

import java.util.List;

public class GoldpriceBean {

    /**
     * count : 0
     * next : string
     * previous : string
     * results : [{"id":0,"currency":"string","gramprice":"string","taelprice":"string","created":"2020-03-26T03:48:05.996Z","updated":"2020-03-26T03:48:05.996Z"}]
     */

    private int count;
    private String next;
    private String previous;
    private List<ResultsBean> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
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
         * id : 0
         * currency : string
         * gramprice : string
         * taelprice : string
         * created : 2020-03-26T03:48:05.996Z
         * updated : 2020-03-26T03:48:05.996Z
         */

        private int id;
        private String currency;
        private String gramprice;
        private String taelprice;
        private String created;
        private String updated;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getGramprice() {
            return gramprice;
        }

        public void setGramprice(String gramprice) {
            this.gramprice = gramprice;
        }

        public String getTaelprice() {
            return taelprice;
        }

        public void setTaelprice(String taelprice) {
            this.taelprice = taelprice;
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
