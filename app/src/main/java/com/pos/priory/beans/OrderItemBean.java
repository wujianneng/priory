package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/6.
 */

public class OrderItemBean {

    /**
     * id : 3
     * order : 20190115161512
     * stock : {"id":1,"batch":{"id":1,"product":{"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true},"batchno":"20190113","weight":"0.50"},"quantity":9,"location":{"name":"高士德"}}
     * quantity : 1
     * discount : 0.75
     * fixdiscount : 0.00
     * price : 750.0
     */

    private int id;
    private String order;
    private StockBean stock;
    private int quantity;
    private String discount;
    private String fixdiscount;
    private double price;
    private String weight = "";
    boolean isSelected = false;
    private int returnStockId = -1;
    private int oprateCount = 0;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getReturnStockId() {
        return returnStockId;
    }

    public void setReturnStockId(int returnStockId) {
        this.returnStockId = returnStockId;
    }

    public int getOprateCount() {
        return oprateCount;
    }

    public void setOprateCount(int oprateCount) {
        this.oprateCount = oprateCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public StockBean getStock() {
        return stock;
    }

    public void setStock(StockBean stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFixdiscount() {
        return fixdiscount;
    }

    public void setFixdiscount(String fixdiscount) {
        this.fixdiscount = fixdiscount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static class StockBean {
        /**
         * id : 1
         * batch : {"id":1,"product":{"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true},"batchno":"20190113","weight":"0.50"}
         * quantity : 9
         * location : {"name":"高士德"}
         */

        private int id;
        private BatchBean batch;
        private int quantity;
        private LocationBean location;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public BatchBean getBatch() {
            return batch;
        }

        public void setBatch(BatchBean batch) {
            this.batch = batch;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public static class BatchBean {
            /**
             * id : 1
             * product : {"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true}
             * batchno : 20190113
             * weight : 0.50
             */

            private int id;
            private ProductBean product;
            private String batchno;
            private String weight;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public ProductBean getProduct() {
                return product;
            }

            public void setProduct(ProductBean product) {
                this.product = product;
            }

            public String getBatchno() {
                return batchno;
            }

            public void setBatchno(String batchno) {
                this.batchno = batchno;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public static class ProductBean {
                /**
                 * id : 1
                 * name : 千足小福珠
                 * productcode : 1001
                 * price : 1000.00
                 * image : static/img/products/flower_j6q4NpE.jpg
                 * discountcontrol : true
                 */

                private int id;
                private String name;
                private int productcode;
                private String price;
                private String realPrice;
                private String image;
                private boolean discountcontrol;
                private String catalog;
                private boolean returnable;

                public boolean isReturnable() {
                    return returnable;
                }

                public void setReturnable(boolean returnable) {
                    this.returnable = returnable;
                }

                public String getRealPrice() {
                    return realPrice;
                }

                public void setRealPrice(String realPrice) {
                    this.realPrice = realPrice;
                }

                public String getCatalog() {
                    return catalog;
                }

                public void setCatalog(String catalog) {
                    this.catalog = catalog;
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

                public int getProductcode() {
                    return productcode;
                }

                public void setProductcode(int productcode) {
                    this.productcode = productcode;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public boolean isDiscountcontrol() {
                    return discountcontrol;
                }

                public void setDiscountcontrol(boolean discountcontrol) {
                    this.discountcontrol = discountcontrol;
                }
            }
        }

        public static class LocationBean {
            /**
             * name : 高士德
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
