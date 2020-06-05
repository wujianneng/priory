package com.pos.priory.beans;

import java.util.List;

public class RepertoryFiltersBean {


    /**
     * code : 202
     * msg : 成功
     * level : info
     * result : {"warehouse":[{"id":1,"name":"主倉庫","wh_primary":true,"wh_returned":false},{"id":2,"name":"回收倉","wh_primary":false,"wh_returned":true}],"category":[{"cid":1,"name":"黃金"},{"cid":2,"name":"配件"}],"returntype":[{"name":""},{"name":"回收"},{"name":"換貨"}]}
     */

    private int code;
    private String msg;
    private String level;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<WarehouseBean> warehouse;
        private List<CategoryBean> category;
        private List<ReturntypeBean> returntype;

        public List<WarehouseBean> getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(List<WarehouseBean> warehouse) {
            this.warehouse = warehouse;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public List<ReturntypeBean> getReturntype() {
            return returntype;
        }

        public void setReturntype(List<ReturntypeBean> returntype) {
            this.returntype = returntype;
        }

        public static class WarehouseBean {
            /**
             * id : 1
             * name : 主倉庫
             * wh_primary : true
             * wh_returned : false
             */

            private int id;
            private String name;
            private boolean wh_primary;
            private boolean wh_returned;

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

            public boolean isWh_primary() {
                return wh_primary;
            }

            public void setWh_primary(boolean wh_primary) {
                this.wh_primary = wh_primary;
            }

            public boolean isWh_returned() {
                return wh_returned;
            }

            public void setWh_returned(boolean wh_returned) {
                this.wh_returned = wh_returned;
            }
        }

        public static class CategoryBean {
            /**
             * cid : 1
             * name : 黃金
             */

            private int cid;
            private String name;

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ReturntypeBean {
            /**
             * name :
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
