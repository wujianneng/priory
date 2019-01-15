package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/9.
 */

public class PurchasingItemBean {


    /**
     * id : 1
     * purchasing : {"id":1,"location":"高士德","confirmed":false,"created":"2019-01-15T21:43:54.893367+08:00"}
     * stock : {"id":1,"batch":{"id":1,"product":{"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true,"catalog":"黃金","returnable":true},"batchno":"20190113","weight":"0.50"},"quantity":9,"location":{"name":"高士德"}}
     * type : purchase
     * quantity : 3
     * staff : store01manager
     */

    private int id;
    private PurchasingBean purchasing;
    private StockBean stock;
    private String type;
    private int quantity;
    private String staff;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PurchasingBean getPurchasing() {
        return purchasing;
    }

    public void setPurchasing(PurchasingBean purchasing) {
        this.purchasing = purchasing;
    }

    public StockBean getStock() {
        return stock;
    }

    public void setStock(StockBean stock) {
        this.stock = stock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public static class PurchasingBean {
        /**
         * id : 1
         * location : 高士德
         * confirmed : false
         * created : 2019-01-15T21:43:54.893367+08:00
         */

        private int id;
        private String location;
        private boolean confirmed;
        private String created;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }

    public static class StockBean {
        /**
         * id : 1
         * batch : {"id":1,"product":{"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true,"catalog":"黃金","returnable":true},"batchno":"20190113","weight":"0.50"}
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
             * product : {"id":1,"name":"千足小福珠","productcode":1001,"price":"1000.00","image":"static/img/products/flower_j6q4NpE.jpg","discountcontrol":true,"catalog":"黃金","returnable":true}
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
                 * catalog : 黃金
                 * returnable : true
                 */

                private int id;
                private String name;
                private int productcode;
                private String price;
                private String image;
                private boolean discountcontrol;
                private String catalog;
                private boolean returnable;

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

                public String getCatalog() {
                    return catalog;
                }

                public void setCatalog(String catalog) {
                    this.catalog = catalog;
                }

                public boolean isReturnable() {
                    return returnable;
                }

                public void setReturnable(boolean returnable) {
                    this.returnable = returnable;
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
