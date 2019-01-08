package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/7.
 */

public class CreateOrderItemResultBean {

    /**
     * id : 179
     * order : 20190107140119
     * stock : {"id":2,"product":{"id":3,"name":"皮繩","productcode":5001,"price":"50.00","image":"static/img/products/flower_0WGslUW.jpg"},"quantity":5,"location":{"name":"高士德"}}
     * quantity : 1
     * discount : 1.00
     * fixdiscount : 0.00
     * price : 50.0
     */

    private int id;
    private String order;
    private StockBean stock;
    private int quantity;
    private String discount;
    private String fixdiscount;
    private double price;

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
         * id : 2
         * product : {"id":3,"name":"皮繩","productcode":5001,"price":"50.00","image":"static/img/products/flower_0WGslUW.jpg"}
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
             * id : 3
             * name : 皮繩
             * productcode : 5001
             * price : 50.00
             * image : static/img/products/flower_0WGslUW.jpg
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
