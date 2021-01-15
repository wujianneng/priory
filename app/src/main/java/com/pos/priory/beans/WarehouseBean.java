package com.pos.priory.beans;

import java.util.List;

public class WarehouseBean {


    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"id":2,"name":"回收倉","wh_primary":false,"wh_returned":true,"total":{"weight":1.1,"quantity":2},"item":[{"id":6,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"returninfo":{"date":"2020-03-27 19:06:37","type":"換貨","cost":100},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"回收倉","shop":"測試店"},{"id":5,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"returninfo":{"date":"2020-03-27 19:06:42","type":"回收","cost":100},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"回收倉","shop":"測試店"}]}]
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
         * id : 2
         * name : 回收倉
         * wh_primary : false
         * wh_returned : true
         * total : {"weight":1.1,"quantity":2}
         * item : [{"id":6,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"returninfo":{"date":"2020-03-27 19:06:37","type":"換貨","cost":100},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"回收倉","shop":"測試店"},{"id":5,"product_id":1,"name":"測試產品1001","image":"http://annabella.infitack.cn/media/uploads/test.jfif","productcode":1001,"returninfo":{"date":"2020-03-27 19:06:42","type":"回收","cost":100},"subtotal":{"weight":6.5,"quantity":7},"price":100,"warehouse":"回收倉","shop":"測試店"}]
         */

        private int id;
        private String name;
        private boolean wh_primary;
        private boolean wh_returned;
        private TotalBean total;
        private List<ItemBean> item;

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

        public TotalBean getTotal() {
            return total;
        }

        public void setTotal(TotalBean total) {
            this.total = total;
        }

        public List<ItemBean> getItem() {
            return item;
        }

        public void setItem(List<ItemBean> item) {
            this.item = item;
        }

        public static class TotalBean {
            /**
             * weight : 1.1
             * quantity : 2
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

        public static class ItemBean {
            /**
             * id : 6
             * product_id : 1
             * name : 測試產品1001
             * image : http://annabella.infitack.cn/media/uploads/test.jfif
             * productcode : 1001
             * returninfo : {"date":"2020-03-27 19:06:37","type":"換貨","cost":100}
             * subtotal : {"weight":6.5,"quantity":7}
             * price : 100.0
             * warehouse : 回收倉
             * shop : 測試店
             */

            private boolean isSelected;
            private int id;
            private int product_id;
            private String name;
            private String image;
            private int productcode;
            private ReturninfoBean returninfo;
            private SubtotalBean subtotal;
            PriceBean price;
            private String warehouse;
            private String shop;

            public PriceBean getPrice() {
                return price;
            }

            public void setPrice(PriceBean price) {
                this.price = price;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

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
                /**
                 * date : 2020-03-27 19:06:37
                 * type : 換貨
                 * cost : 100.0
                 */

                private String date;
                private String type;
                private double cost;
                private double weight;

                public double getWeight() {
                    return weight;
                }

                public void setWeight(double weight) {
                    this.weight = weight;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public double getCost() {
                    return cost;
                }

                public void setCost(double cost) {
                    this.cost = cost;
                }
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

            public static class PriceBean {
                /**
                 * price : 100.0
                 * currencycode : MOP
                 * symbol : $
                 */

                private double price;
                private String currencycode;
                private String symbol;

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public String getCurrencycode() {
                    return currencycode;
                }

                public void setCurrencycode(String currencycode) {
                    this.currencycode = currencycode;
                }

                public String getSymbol() {
                    return symbol;
                }

                public void setSymbol(String symbol) {
                    this.symbol = symbol;
                }
            }
        }
    }
}
