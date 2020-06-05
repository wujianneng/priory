package com.pos.priory.beans;

import java.util.List;

public class RepertoryRecordBean {

    /**
     * count : 3
     * next : null
     * previous : null
     * results : [{"id":5,"whitem":{"id":8,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"whnumber":"202003270002","returninfo":{},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"主倉庫","shop":"測試店"},"quantity":1,"type":"入庫","purpose":"入貨","created":"2020-03-30 01:01:04","updated":"2020-03-30 16:00:33"},{"id":4,"whitem":{"id":7,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"whnumber":"202003270001","returninfo":{},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"主倉庫","shop":"測試店"},"quantity":1,"type":"入庫","purpose":"入貨","created":"2020-03-30 00:44:52","updated":"2020-03-30 16:00:37"},{"id":6,"whitem":{"id":9,"product_id":2,"name":"測試配件1002","image":"http://annabella.infitack.cn/media/uploads/test_S4JJNon.jfif","productcode":1002,"whnumber":"202003270003","returninfo":{},"subtotal":{"weight":1.1,"quantity":2},"price":50,"warehouse":"主倉庫","shop":"測試店"},"quantity":1,"type":"入庫","purpose":"入貨","created":"2020-03-30 01:01:11","updated":"2020-03-30 16:00:49"}]
     */

    private int count;
    private Object next;
    private Object previous;
    private List<ResultsBean> results;

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
         * id : 5
         * whitem : {"id":8,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"whnumber":"202003270002","returninfo":{},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"主倉庫","shop":"測試店"}
         * quantity : 1
         * type : 入庫
         * purpose : 入貨
         * created : 2020-03-30 01:01:04
         * updated : 2020-03-30 16:00:33
         */

        private int id;
        private WhitemBean whitem;
        private int quantity;
        private String type;
        private String purpose;
        private String created;
        private String updated;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public WhitemBean getWhitem() {
            return whitem;
        }

        public void setWhitem(WhitemBean whitem) {
            this.whitem = whitem;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
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

        public static class WhitemBean {
            /**
             * id : 8
             * product_id : 1
             * name : 測試產品1001
             * image : http://annabella.infitack.cn/media/uploads/test.jfif
             * productcode : 1001
             * whnumber : 202003270002
             * returninfo : {}
             * subtotal : {"weight":6.5,"quantity":7}
             * price : 100.0
             * warehouse : 主倉庫
             * shop : 測試店
             */

            private int id;
            private int product_id;
            private String name;
            private String image;
            private int productcode;
            private String whnumber;
            private ReturninfoBean returninfo;
            private SubtotalBean subtotal;
            private double price;
            private String warehouse;
            private String shop;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getProductcode() {
                return productcode;
            }

            public void setProductcode(int productcode) {
                this.productcode = productcode;
            }

            public String getWhnumber() {
                return whnumber;
            }

            public void setWhnumber(String whnumber) {
                this.whnumber = whnumber;
            }

            public ReturninfoBean getReturninfo() {
                return returninfo;
            }

            public void setReturninfo(ReturninfoBean returninfo) {
                this.returninfo = returninfo;
            }

            public SubtotalBean getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(SubtotalBean subtotal) {
                this.subtotal = subtotal;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getWarehouse() {
                return warehouse;
            }

            public void setWarehouse(String warehouse) {
                this.warehouse = warehouse;
            }

            public String getShop() {
                return shop;
            }

            public void setShop(String shop) {
                this.shop = shop;
            }

            public static class ReturninfoBean {
            }

            public static class SubtotalBean {
                /**
                 * weight : 6.5
                 * quantity : 7
                 */

                private double weight;
                private int quantity;

                public double getWeight() {
                    return weight;
                }

                public void setWeight(double weight) {
                    this.weight = weight;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }
            }
        }
    }
}
