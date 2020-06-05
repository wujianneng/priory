package com.pos.priory.beans;

import java.util.List;

public class TranferStoresBean {

    /**
     * code : 202
     * msg : 成功
     * level : info
     * result : [{"id":9,"status":true,"storeno":"12344","name":"內測分店","tel":"","address":"","starttime":"09:00:00","endtime":"21:00:00","currency":{"id":1,"status":true,"name":"MOP","symbol":"$"},"region":{"id":1,"status":true,"name":"中國澳門"},"created":"2020-03-26 04:23:27","updated":"2020-03-26 04:23:36"}]
     */

    private int code;
    private String msg;
    private String level;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 9
         * status : true
         * storeno : 12344
         * name : 內測分店
         * tel :
         * address :
         * starttime : 09:00:00
         * endtime : 21:00:00
         * currency : {"id":1,"status":true,"name":"MOP","symbol":"$"}
         * region : {"id":1,"status":true,"name":"中國澳門"}
         * created : 2020-03-26 04:23:27
         * updated : 2020-03-26 04:23:36
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
             * name : 中國澳門
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
