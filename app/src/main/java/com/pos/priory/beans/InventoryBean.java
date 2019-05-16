package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class InventoryBean {


    /**
     * id : 80
     * golditemcount : 1
     * golditemweight : 1.1
     * cystalitemcount : 0
     * status : 未完成
     * inventoryitem : [{"id":70,"check":false,"productname":"測試2","stockno":"100211111","stockweight":1.1}]
     * created : 2019-05-07T08:27:16.249586Z
     */

    private int id;
    private String golditemcount;
    private double golditemweight;
    private String cystalitemcount;
    private String status;
    private String created;
    private List<InventoryitemBean> inventoryitem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGolditemcount() {
        return golditemcount;
    }

    public void setGolditemcount(String golditemcount) {
        this.golditemcount = golditemcount;
    }

    public double getGolditemweight() {
        return golditemweight;
    }

    public void setGolditemweight(double golditemweight) {
        this.golditemweight = golditemweight;
    }

    public String getCystalitemcount() {
        return cystalitemcount;
    }

    public void setCystalitemcount(String cystalitemcount) {
        this.cystalitemcount = cystalitemcount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<InventoryitemBean> getInventoryitem() {
        return inventoryitem;
    }

    public void setInventoryitem(List<InventoryitemBean> inventoryitem) {
        this.inventoryitem = inventoryitem;
    }

    public static class InventoryitemBean {
        /**
         * id : 70
         * check : false
         * productname : 測試2
         * stockno : 100211111
         * stockweight : 1.1
         */

        private int id;
        private boolean check;
        private String productname;
        private String stockno;
        private double stockweight;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getStockno() {
            return stockno;
        }

        public void setStockno(String stockno) {
            this.stockno = stockno;
        }

        public double getStockweight() {
            return stockweight;
        }

        public void setStockweight(double stockweight) {
            this.stockweight = stockweight;
        }
    }
}
