package com.pos.priory.beans;

import java.util.List;

public class RepertoryRecordFiltersBean {

    /**
     * code : 200
     * msg : OK
     * level : info
     * result : {"warehouse":[{"id":"primary","name":"主倉庫"},{"id":"returned","name":"回收倉"}],"type":[{"value":1,"name":"出庫"},{"value":2,"name":"入庫"}],"purpose":[{"value":1,"name":"銷售"},{"value":3,"name":"入貨"},{"value":4,"name":"回收"},{"value":5,"name":"換貨"},{"value":6,"name":"撤回"},{"value":7,"name":"刪除"}],"whfrom":[{"id":1,"name":"澳門測試店- 主倉庫"},{"id":2,"name":"澳門測試店- 回收倉"}]}
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
             * id : primary
             * name : 主倉庫
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
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
             * value : 1
             * name : 出庫
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
             * value : 1
             * name : 銷售
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
             * id : 1
             * name : 澳門測試店- 主倉庫
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
