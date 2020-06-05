package com.pos.priory.beans;

import java.util.List;

public class InventoryTypeDetialBean {


    /**
     * id : 1
     * status : true
     * warehouse : 主倉庫
     * done : false
     * category : [{"id":1,"category":"黃金","count":2,"total":2},{"id":2,"category":"配件","count":0,"total":1}]
     * created : 2020-03-16 17:48:40
     * updated : 2020-03-16 17:48:40
     */

    private int id;
    private boolean status;
    private String warehouse;
    private boolean done;
    private String created;
    private String updated;
    private List<CategoryBean> category;

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

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class CategoryBean {
        /**
         * id : 1
         * category : 黃金
         * count : 2
         * total : 2
         */

        private int id;
        private String category;
        private int count;
        private int total;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
