package com.pos.priory.beans;

import java.util.List;

public class WarehouseBean {


    /**
     * quantity_total : 405
     * weight_total : 5325.05
     * count : 9
     * next : null
     * previous : null
     * results : [{"id":28,"name":"玉石777","namezh":"玉石777","image":"http://annabella.infitack.cn/media/uploads/11bae2c6-7c08-4a44-a821-b22ef3af52f5.jpg","productcode":"1014","symbol":"$","unitprice":118,"prd_stock_quantity":48,"prd_stock_weight":1512},{"id":27,"name":"玉石555","namezh":"玉石555","image":"http://annabella.infitack.cn/media/uploads/cec38b1b-d6b6-443b-a23b-47b3015dde04.jpg","productcode":"1013","symbol":"$","unitprice":98,"prd_stock_quantity":46,"prd_stock_weight":1173},{"id":26,"name":"玉石333","namezh":"玉石333","image":"http://annabella.infitack.cn/media/uploads/2030edd9-877c-429c-b299-583bd8eba4d5.jpg","productcode":"1012","symbol":"$","unitprice":68,"prd_stock_quantity":47,"prd_stock_weight":2350},{"id":25,"name":"黃金999","namezh":"黃金999","image":"http://annabella.infitack.cn/media/uploads/380cd7be-d74d-46d8-ba34-2103d0c03995.jpg","productcode":"1011","symbol":"$","unitprice":368,"prd_stock_quantity":49,"prd_stock_weight":61.25},{"id":24,"name":"黃金888","namezh":"黃金888","image":"http://annabella.infitack.cn/media/uploads/8289242c-43c3-436f-bafd-3eb0efe1eea2.jpg","productcode":"1010","symbol":"$","unitprice":300,"prd_stock_quantity":49,"prd_stock_weight":49},{"id":4,"name":"千足金闪闪生辉F","namezh":"","image":"http://annabella.infitack.cn/media/uploads/0006.jpg","productcode":"1006","symbol":"$","unitprice":199,"prd_stock_quantity":49,"prd_stock_weight":53.9},{"id":3,"name":"千足金闪闪生辉E","namezh":"","image":"http://annabella.infitack.cn/media/uploads/0005.jpg","productcode":"1005","symbol":"$","unitprice":199,"prd_stock_quantity":49,"prd_stock_weight":53.9},{"id":2,"name":"千足金闪闪生辉D","namezh":"","image":"http://annabella.infitack.cn/media/uploads/0004.jpg","productcode":"1004","symbol":"$","unitprice":299,"prd_stock_quantity":28,"prd_stock_weight":28},{"id":1,"name":"千足金闪闪生辉C","namezh":"千足金閃閃生輝C","image":"http://annabella.infitack.cn/media/uploads/0003_Sk1CUr5.jpg","productcode":"1003","symbol":"$","unitprice":399,"prd_stock_quantity":40,"prd_stock_weight":44}]
     */

    private int quantity_total;
    private double weight_total;
    private int count;
    private Object next;
    private Object previous;
    private List<ResultsBean> results;

    public int getQuantity_total() {
        return quantity_total;
    }

    public void setQuantity_total(int quantity_total) {
        this.quantity_total = quantity_total;
    }

    public double getWeight_total() {
        return weight_total;
    }

    public void setWeight_total(double weight_total) {
        this.weight_total = weight_total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : 28
         * name : 玉石777
         * namezh : 玉石777
         * image : http://annabella.infitack.cn/media/uploads/11bae2c6-7c08-4a44-a821-b22ef3af52f5.jpg
         * productcode : 1014
         * symbol : $
         * unitprice : 118.0
         * prd_stock_quantity : 48
         * prd_stock_weight : 1512.0
         */

        private int id;
        private String name;
        private String namezh;
        private String image;
        private String productcode;
        private String symbol;
        private double unitprice;
        private int prd_stock_quantity;
        private double prd_stock_weight;

        private WarehouseReturnBean.ResultsBean resultsBean;

        public WarehouseReturnBean.ResultsBean getResultsBean() {
            return resultsBean;
        }

        public void setResultsBean(WarehouseReturnBean.ResultsBean resultsBean) {
            this.resultsBean = resultsBean;
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

        public String getNamezh() {
            return namezh;
        }

        public void setNamezh(String namezh) {
            this.namezh = namezh;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProductcode() {
            return productcode;
        }

        public void setProductcode(String productcode) {
            this.productcode = productcode;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(double unitprice) {
            this.unitprice = unitprice;
        }

        public int getPrd_stock_quantity() {
            return prd_stock_quantity;
        }

        public void setPrd_stock_quantity(int prd_stock_quantity) {
            this.prd_stock_quantity = prd_stock_quantity;
        }

        public double getPrd_stock_weight() {
            return prd_stock_weight;
        }

        public void setPrd_stock_weight(double prd_stock_weight) {
            this.prd_stock_weight = prd_stock_weight;
        }
    }
}
