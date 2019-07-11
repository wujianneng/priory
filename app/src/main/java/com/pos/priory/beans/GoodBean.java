package com.pos.priory.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/6.
 */

public class GoodBean {


    /**
     * id : 3
     * stockno : 12345678
     * location : 测试
     * product : {"id":1,"name":"測試1","productcode":1001,"price":"1000.00","image":"static/img/products/test123.jpg","catalog":{"name":"黃金","discounts":[{"name":"95折","value":"0.950000"}]}}
     * weight : 1.3
     */

    private int id;
    private String stockno;
    private String location;
    private ProductBean product;
    private double weight;
    private int quantity;
    private int discountId = 1;
    private double discountRate = 1;
    /**
     * weight : 1.2
     */

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockno() {
        return stockno;
    }

    public void setStockno(String stockno) {
        this.stockno = stockno;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public static class ProductBean {
        /**
         * id : 1
         * name : 測試1
         * productcode : 1001
         * price : 1000.00
         * image : static/img/products/test123.jpg
         * catalog : {"name":"黃金","discounts":[{"name":"95折","value":"0.950000"}]}
         */

        private int id;
        private String name;
        private int productcode;
        private String price, prePrice;
        private String image;
        private CatalogBean catalog;

        public String getPrePrice() {
            return prePrice;
        }

        public void setPrePrice(String prePrice) {
            this.prePrice = prePrice;
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

        public CatalogBean getCatalog() {
            return catalog;
        }

        public void setCatalog(CatalogBean catalog) {
            this.catalog = catalog;
        }

        public static class CatalogBean {
            /**
             * name : 黃金
             * discounts : [{"name":"95折","value":"0.950000"}]
             */

            private String name;
            private List<DiscountsBean> discounts;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<DiscountsBean> getDiscounts() {
                return discounts;
            }

            public void setDiscounts(List<DiscountsBean> discounts) {
                this.discounts = discounts;
            }

            public static class DiscountsBean {
                /**
                 * name : 95折
                 * value : 0.950000
                 */
                private int id;
                private String name;
                private String value;

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

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }


}
