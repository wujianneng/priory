package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/6.
 */

public class GoodBean {

    /**
     * id : 2
     * product : {"id":3,"name":"皮繩","productcode":5001,"price":"50.00","image":"static/img/products/flower_0WGslUW.jpg"}
     * quantity : 5
     * location : {"name":"高士德"}
     */

    private int id;
    private ProductBean product;
    private int quantity;
    private int saleCount = 1;
    private double discountRate = 1;
    private LocationBean location;

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

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
        private boolean discountcontrol;

        public boolean isDiscountcontrol() {
            return discountcontrol;
        }

        public void setDiscountcontrol(boolean discountcontrol) {
            this.discountcontrol = discountcontrol;
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
