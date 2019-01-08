package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class InventoryBean {

    /**
     * id : 5
     * staff : staff01
     * check : false
     * stock : {"id":2,"product":{"id":3,"name":"皮繩","productcode":5001,"price":"50.00","image":"static/img/products/flower_0WGslUW.jpg"},"quantity":5,"location":{"name":"高士德"}}
     * systemcheck : false
     */

    private int id;
    private String staff;
    private boolean check;
    private StockBean stock;
    private boolean systemcheck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
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
