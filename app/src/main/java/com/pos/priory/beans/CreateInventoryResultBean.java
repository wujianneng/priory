package com.pos.priory.beans;

import java.util.List;

public class CreateInventoryResultBean {

    /**
     * id : 0
     * item : [{"id":0,"status":true,"created":"2021-01-15T02:24:48.733Z","updated":"2021-01-15T02:24:48.733Z","quantity":0,"done":true,"inventory":0,"whitem":0}]
     * status : true
     * created : 2021-01-15T02:24:48.733Z
     * updated : 2021-01-15T02:24:48.733Z
     * done : true
     * warehouse : 0
     */

    private int id;
    private boolean status;
    private String created;
    private String updated;
    private boolean done;
    private int warehouse;
    private List<ItemBean> item;

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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int warehouse) {
        this.warehouse = warehouse;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * id : 0
         * status : true
         * created : 2021-01-15T02:24:48.733Z
         * updated : 2021-01-15T02:24:48.733Z
         * quantity : 0
         * done : true
         * inventory : 0
         * whitem : 0
         */

        private int id;
        private boolean status;
        private String created;
        private String updated;
        private int quantity;
        private boolean done;
        private int inventory;
        private int whitem;

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

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public int getWhitem() {
            return whitem;
        }

        public void setWhitem(int whitem) {
            this.whitem = whitem;
        }
    }
}
