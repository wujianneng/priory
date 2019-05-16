package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/9.
 */

public class PurchasingItemBean {


    /**
     * id : 0
     * product : {"id":0,"name":"string","productcode":0,"price":"string","image":"string","catalog":{"name":"string","discounts":[{"name":"string","value":"string"}]}}
     * status : true
     * quantity : 0
     * staff : string
     */

    private int id;
    private ProductBean product;
    private boolean status;
    private int quantity;
    private String staff;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public static class ProductBean {
        /**
         * id : 0
         * name : string
         * productcode : 0
         * price : string
         * image : string
         * catalog : {"name":"string","discounts":[{"name":"string","value":"string"}]}
         */

        private int id;
        private String name;
        private int productcode;
        private String price;
        private String image;
        private CatalogBean catalog;

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
             * name : string
             * discounts : [{"name":"string","value":"string"}]
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
                 * name : string
                 * value : string
                 */

                private String name;
                private String value;

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
