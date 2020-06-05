package com.pos.priory.beans;

import java.util.List;

public class ReturnFilterBean {

    /**
     * code : 202
     * msg : 成功
     * level : info
     * result : {"type":[{"name":"回收"},{"name":"換貨"}],"product":[{"id":3,"name":"回收產品1"}]}
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
        private List<TypeBean> type;
        private List<ProductBean> product;

        public List<TypeBean> getType() {
            return type;
        }

        public void setType(List<TypeBean> type) {
            this.type = type;
        }

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }

        public static class TypeBean {
            /**
             * name : 回收
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ProductBean {
            /**
             * id : 3
             * name : 回收產品1
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
