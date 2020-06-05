package com.pos.priory.beans;

import java.util.List;

public class RepertoryRecordFiltersBean {


    /**
     * code : 202
     * msg : 成功
     * level : info
     * result : {"warehouse":[{"id":1,"name":"主倉庫"},{"id":2,"name":"回收倉"}],"type":[{"value":2,"name":"入庫"}],"purpose":[{"value":3,"name":"入貨"}],"whfrom":[{"id":5,"name":"內測分店- 主倉庫"}]}
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
        private List<TypeBean> type;
        private List<PurposeBean> purpose;
        private List<WhfromBean> whfrom;

        public List<WarehouseBean> getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(List<WarehouseBean> warehouse) {
            this.warehouse = warehouse;
        }

        public List<TypeBean> getType() {
            return type;
        }

        public void setType(List<TypeBean> type) {
            this.type = type;
        }

        public List<PurposeBean> getPurpose() {
            return purpose;
        }

        public void setPurpose(List<PurposeBean> purpose) {
            this.purpose = purpose;
        }

        public List<WhfromBean> getWhfrom() {
            return whfrom;
        }

        public void setWhfrom(List<WhfromBean> whfrom) {
            this.whfrom = whfrom;
        }

        public static class WarehouseBean {
            /**
             * id : 1
             * name : 主倉庫
             */

            private int id;
            private String name;

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
        }

        public static class TypeBean {
            /**
             * value : 2
             * name : 入庫
             */

            private int value;
            private String name;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PurposeBean {
            /**
             * value : 3
             * name : 入貨
             */

            private int value;
            private String name;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class WhfromBean {
            /**
             * id : 5
             * name : 內測分店- 主倉庫
             */

            private int id;
            private String name;

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
        }
    }
}
