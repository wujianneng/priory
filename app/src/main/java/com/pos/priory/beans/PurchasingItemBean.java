package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/9.
 */

public class PurchasingItemBean {

    /**
     * id : 2
     * purchasing : {"id":1,"location":"白馬行","confirmed":true,"created":"2019-01-09T18:43:28.485912+08:00"}
     * stock : {"id":1,"product":{"id":5,"name":"虎眼石","productcode":2001,"price":"1000.00","image":"static/img/products/flower_GPfBfO8.jpg"},"quantity":5,"location":{"name":"高士德"}}
     * type : purchase
     * quantity : 1
     * staff : staff01
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
         * location : 白馬行
         * confirmed : true
         * created : 2019-01-09T18:43:28.485912+08:00
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
         * product : {"id":5,"name":"虎眼石","productcode":2001,"price":"1000.00","image":"static/img/products/flower_GPfBfO8.jpg"}
         * quantity : 5
         * location : {"name":"高士德"}
         */

        private int id;
        private ProductBean product;
        private int quantity;
        private LocationBean location;

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

        public static class ProductBean {
            /**
             * id : 5
             * name : 虎眼石
             * productcode : 2001
             * price : 1000.00
             * image : static/img/products/flower_GPfBfO8.jpg
             */

            private int id;
            private String name;
            private int productcode;
            private String price;
            private String image;

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
