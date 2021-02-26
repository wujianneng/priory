package com.pos.priory.beans;

import java.util.List;

public class TranferStoresBean {


    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"id":3,"status":true,"storeno":"","name":"澳門測試分店","tel":"","address":"","starttime":"09:00:00","endtime":"17:00:00","currency":{"id":1,"status":true,"name":"MOP","symbol":"$"},"region":{"id":1,"status":true,"name":"澳門"},"created":"2020-08-13 16:18:42","updated":"2020-08-14 10:35:33"}]
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
         * id : 3
         * status : true
         * storeno :
         * name : 澳門測試分店
         * tel :
         * address :
         * starttime : 09:00:00
         * endtime : 17:00:00
         * currency : {"id":1,"status":true,"name":"MOP","symbol":"$"}
         * region : {"id":1,"status":true,"name":"澳門"}
         * created : 2020-08-13 16:18:42
         * updated : 2020-08-14 10:35:33
         */

        private int id;
        private boolean status;
        private String storeno;
        private String name;
        private String tel;
        private String address;
        private String starttime;
        private String endtime;
        private CurrencyBean currency;
        private RegionBean region;
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

        public String getStoreno() {
            return storeno;
        }

        public void setStoreno(String storeno) {
            this.storeno = storeno;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public CurrencyBean getCurrency() {
            return currency;
        }

        public void setCurrency(CurrencyBean currency) {
            this.currency = currency;
        }

        public RegionBean getRegion() {
            return region;
        }

        public void setRegion(RegionBean region) {
            this.region = region;
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

        public static class CurrencyBean {
            /**
             * id : 1
             * status : true
             * name : MOP
             * symbol : $
             */

            private int id;
            private boolean status;
            private String name;
            private String symbol;

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

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }
        }

        public static class RegionBean {
            /**
             * id : 1
             * status : true
             * name : 澳門
             */

            private int id;
            private boolean status;
            private String name;

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
        }
    }
}
