package com.pos.priory.beans;

import java.util.List;

public class CouponResultBean {


    /**
     * code : 200
     * msg : OK
     * level : info
     * result : [{"id":1,"name":"test","description":"test - test","date":"2020-06-10 - 2020-06-15","multiple":true},{"id":12,"name":"12test","description":"12test - test","date":"2020-06-10 - 2020-06-15","multiple":false}]
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
         * id : 1
         * name : test
         * description : test - test
         * date : 2020-06-10 - 2020-06-15
         * multiple : true
         */

        private int id;
        private String name;
        private String description;
        private String date;
        String value;
        private boolean multiple;
        boolean isShowDetail = false,isSelected = false;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isShowDetail() {
            return isShowDetail;
        }

        public void setShowDetail(boolean showDetail) {
            isShowDetail = showDetail;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isMultiple() {
            return multiple;
        }

        public void setMultiple(boolean multiple) {
            this.multiple = multiple;
        }
    }
}
