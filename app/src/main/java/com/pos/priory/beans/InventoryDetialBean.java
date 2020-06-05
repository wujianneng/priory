package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class InventoryDetialBean {


    /**
     * id : 1
     * item : [{"id":1,"name":"測試產品1001","code":"1001201903170001","done":true}]
     */

    private int id;
    private List<ItemBean> item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * id : 1
         * name : 測試產品1001
         * code : 1001201903170001
         * done : true
         */

        private int id;
        private String name;
        private String code;
        private boolean done;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
    }
}
