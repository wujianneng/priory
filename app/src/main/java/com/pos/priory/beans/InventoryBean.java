package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class InventoryBean {

    /**
     * id : 1
     * check : false
     * stock : {"id":1,"batch":{"id":1,"product":{"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true},"batchno":"20190113","weight":"0.50"},"quantity":9,"location":{"name":"高士德"}}
     * systemcheck : false
     */

    private int id;
    private boolean check;
    private StockBean stock;
    private boolean systemcheck;

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

    public StockBean getStock() {
        return stock;
    }

    public void setStock(StockBean stock) {
        this.stock = stock;
    }

    public boolean isSystemcheck() {
        return systemcheck;
    }

    public void setSystemcheck(boolean systemcheck) {
        this.systemcheck = systemcheck;
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
                private String image;
                private boolean discountcontrol;
                private String catalog;

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
