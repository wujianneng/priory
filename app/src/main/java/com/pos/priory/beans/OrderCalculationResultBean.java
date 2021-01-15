package com.pos.priory.beans;

import java.util.List;

public class OrderCalculationResultBean {

    /**
     * results : [{"prd_id":0,"prd_name":"string","prd_unitprice":0,"quantity":0,"category":0,"category_name":"string","total_price":0,"coupon_info":[{"coupons_id":0,"coupon_amount":0}],"y_info":[{"coupon_id":0,"product_id":0,"quantity":0,"origin_price":0,"price":0}],"price":0}]
     * y_info : [{"coupon_id":0,"product_id":"string","quantity":0,"origin_price":"string","price":0}]
     * products_data : [{"id":0,"quantity":0,"whnumber":"string"}]
     * coupons_data : [0]
     * total_amount : 0
     * coupon_total_amount : 0
     * cache_token : string
     */

    private double total_amount;
    private double coupon_total_amount;
    private double amount_payable;
    private double pay_spread;
    private double exchange_amount;
    private String cache_token;
    private List<ResultsBean> results;
    private List<YInfoBeanX> y_info;
    private List<ProductsDataBean> products_data;
    private List<Integer> coupons_data;

    public double getPay_spread() {
        return pay_spread;
    }

    public void setPay_spread(double pay_spread) {
        this.pay_spread = pay_spread;
    }

    public double getExchange_amount() {
        return exchange_amount;
    }

    public void setExchange_amount(double exchange_amount) {
        this.exchange_amount = exchange_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public void setCoupon_total_amount(double coupon_total_amount) {
        this.coupon_total_amount = coupon_total_amount;
    }

    public double getAmount_payable() {
        return amount_payable;
    }

    public void setAmount_payable(double amount_payable) {
        this.amount_payable = amount_payable;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public double getCoupon_total_amount() {
        return coupon_total_amount;
    }

    public void setCoupon_total_amount(int coupon_total_amount) {
        this.coupon_total_amount = coupon_total_amount;
    }

    public String getCache_token() {
        return cache_token;
    }

    public void setCache_token(String cache_token) {
        this.cache_token = cache_token;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public List<YInfoBeanX> getY_info() {
        return y_info;
    }

    public void setY_info(List<YInfoBeanX> y_info) {
        this.y_info = y_info;
    }

    public List<ProductsDataBean> getProducts_data() {
        return products_data;
    }

    public void setProducts_data(List<ProductsDataBean> products_data) {
        this.products_data = products_data;
    }

    public List<Integer> getCoupons_data() {
        return coupons_data;
    }

    public void setCoupons_data(List<Integer> coupons_data) {
        this.coupons_data = coupons_data;
    }

    public static class ResultsBean {
        /**
         * prd_id : 0
         * prd_name : string
         * prd_unitprice : 0
         * quantity : 0
         * category : 0
         * category_name : string
         * total_price : 0
         * coupon_info : [{"coupons_id":0,"coupon_amount":0}]
         * y_info : [{"coupon_id":0,"product_id":0,"quantity":0,"origin_price":0,"price":0}]
         * price : 0
         */

        private int prd_id;
        private String prd_name;
        private int prd_unitprice;
        private int quantity;
        private int category;
        private String category_name;
        private int total_price;
        private int price;
        private List<CouponInfoBean> coupon_info;
        private List<YInfoBean> y_info;

        public int getPrd_id() {
            return prd_id;
        }

        public void setPrd_id(int prd_id) {
            this.prd_id = prd_id;
        }

        public String getPrd_name() {
            return prd_name;
        }

        public void setPrd_name(String prd_name) {
            this.prd_name = prd_name;
        }

        public int getPrd_unitprice() {
            return prd_unitprice;
        }

        public void setPrd_unitprice(int prd_unitprice) {
            this.prd_unitprice = prd_unitprice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public List<CouponInfoBean> getCoupon_info() {
            return coupon_info;
        }

        public void setCoupon_info(List<CouponInfoBean> coupon_info) {
            this.coupon_info = coupon_info;
        }

        public List<YInfoBean> getY_info() {
            return y_info;
        }

        public void setY_info(List<YInfoBean> y_info) {
            this.y_info = y_info;
        }

        public static class CouponInfoBean {
            /**
             * coupons_id : 0
             * coupon_amount : 0
             */

            private int coupons_id;
            private int coupon_amount;

            public int getCoupons_id() {
                return coupons_id;
            }

            public void setCoupons_id(int coupons_id) {
                this.coupons_id = coupons_id;
            }

            public int getCoupon_amount() {
                return coupon_amount;
            }

            public void setCoupon_amount(int coupon_amount) {
                this.coupon_amount = coupon_amount;
            }
        }

        public static class YInfoBean {
            /**
             * coupon_id : 0
             * product_id : 0
             * quantity : 0
             * origin_price : 0
             * price : 0
             */

            private int coupon_id;
            private int product_id;
            private int quantity;
            private int origin_price;
            private int price;

            public int getCoupon_id() {
                return coupon_id;
            }

            public void setCoupon_id(int coupon_id) {
                this.coupon_id = coupon_id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public int getOrigin_price() {
                return origin_price;
            }

            public void setOrigin_price(int origin_price) {
                this.origin_price = origin_price;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }
        }
    }

    public static class YInfoBeanX {
        /**
         * coupon_id : 0
         * product_id : string
         * quantity : 0
         * origin_price : string
         * price : 0
         */

        private int coupon_id;
        private String product_id;
        private int quantity;
        private String origin_price;
        private int price;

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getOrigin_price() {
            return origin_price;
        }

        public void setOrigin_price(String origin_price) {
            this.origin_price = origin_price;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    public static class ProductsDataBean {
        /**
         * id : 0
         * quantity : 0
         * whnumber : string
         */

        private int id;
        private int quantity;
        private String whnumber;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getWhnumber() {
            return whnumber;
        }

        public void setWhnumber(String whnumber) {
            this.whnumber = whnumber;
        }
    }
}
