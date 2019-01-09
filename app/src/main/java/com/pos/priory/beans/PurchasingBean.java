package com.pos.priory.beans;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class PurchasingBean {

    /**
     * product : {"id":1,"name":"千足金小熊","productcode":1001,"price":"1000.00","image":"static/img/products/flower_PecCFzH.jpg"}
     * type : return
     * location : 高士德
     * quantity : 1
     * requestby : staff01
     * confirmed : false
     */
    private int id;
    private ProductBean product;
    private String type;
    private String location;
    private int quantity;
    private String requestby;
    private boolean confirmed;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRequestby() {
        return requestby;
    }

    public void setRequestby(String requestby) {
        this.requestby = requestby;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public static class ProductBean {
        /**
         * id : 1
         * name : 千足金小熊
         * productcode : 1001
         * price : 1000.00
         * image : static/img/products/flower_PecCFzH.jpg
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
}
