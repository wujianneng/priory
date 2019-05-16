package com.pos.priory.beans;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class OrderBean {

    /**
     * id : 27
     * ordernumber : 20190429160310
     * member : {"id":2,"mobile":"15019840934","first_name":"","last_name":"Tom","sex":"男","reward":-60000}
     * staff : testmanager
     * status : 完成
     * items : [{"id":13,"stock":{"id":3,"stockno":"12345678","location":"测试","product":{"id":1,"name":"測試1","productcode":1001,"price":"1000.00","image":"static/img/products/test123.jpg","catalog":{"name":"黃金","discounts":[{"name":"95折","value":"0.950000"}]}},"weight":1.3},"discount":"0.000000","fixdiscount":"0.00","price":0,"returned":false}]
     * invoiceitem : [{"id":19,"invoicenumber":"INV1000012","amount":"0.0","status":"完成","transactionitem":[{"id":32,"transactionnumber":"T10000019","amount":"1000.0","paymentmethod":"信用卡"}]}]
     * totalprice : 0.0
     * itemsweight : 1.3
     * location : 测试
     * created : 2019-04-29T16:03:10.038197+08:00
     */

    private int id;
    private String ordernumber;
    private MemberBean member;
    private String staff;
    private String status;
    private double totalprice;
    private double itemsweight;
    private String location;
    private String created;
    private List<ItemsBean> items;
    private List<InvoiceitemBean> invoiceitem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public double getItemsweight() {
        return itemsweight;
    }

    public void setItemsweight(double itemsweight) {
        this.itemsweight = itemsweight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public List<InvoiceitemBean> getInvoiceitem() {
        return invoiceitem;
    }

    public void setInvoiceitem(List<InvoiceitemBean> invoiceitem) {
        this.invoiceitem = invoiceitem;
    }

    public static class MemberBean {
        /**
         * id : 2
         * mobile : 15019840934
         * first_name :
         * last_name : Tom
         * sex : 男
         * reward : -60000
         */

        private int id;
        private String mobile;
        private String first_name;
        private String last_name;
        private String sex;
        private int reward;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }
    }

    public static class ItemsBean {
        /**
         * id : 13
         * stock : {"id":3,"stockno":"12345678","location":"测试","product":{"id":1,"name":"測試1","productcode":1001,"price":"1000.00","image":"static/img/products/test123.jpg","catalog":{"name":"黃金","discounts":[{"name":"95折","value":"0.950000"}]}},"weight":1.3}
         * discount : 0.000000
         * fixdiscount : 0.00
         * price : 0.0
         * returned : false
         */

        private int id;
        private StockBean stock;
        private String discount;
        private String fixdiscount;
        private double price;
        private boolean returned;
        private String weight = "";

        boolean isSelected = false;
        private int returnStockId = -1;
        private int oprateCount = 1;

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getReturnStockId() {
            return returnStockId;
        }

        public void setReturnStockId(int returnStockId) {
            this.returnStockId = returnStockId;
        }

        public int getOprateCount() {
            return oprateCount;
        }

        public void setOprateCount(int oprateCount) {
            this.oprateCount = oprateCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public StockBean getStock() {
            return stock;
        }

        public void setStock(StockBean stock) {
            this.stock = stock;
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

        public boolean isReturned() {
            return returned;
        }

        public void setReturned(boolean returned) {
            this.returned = returned;
        }

        public static class StockBean {
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
                private String price;
                private String realPrice;
                private String image;
                private CatalogBean catalog;

                public String getRealPrice() {
                    return realPrice;
                }

                public void setRealPrice(String realPrice) {
                    this.realPrice = realPrice;
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
    }

    public static class InvoiceitemBean {
        /**
         * id : 19
         * invoicenumber : INV1000012
         * amount : 0.0
         * status : 完成
         * transactionitem : [{"id":32,"transactionnumber":"T10000019","amount":"1000.0","paymentmethod":"信用卡"}]
         */

        private int id;
        private String invoicenumber;
        private String amount;
        private String status;
        private List<TransactionitemBean> transactionitem;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInvoicenumber() {
            return invoicenumber;
        }

        public void setInvoicenumber(String invoicenumber) {
            this.invoicenumber = invoicenumber;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<TransactionitemBean> getTransactionitem() {
            return transactionitem;
        }

        public void setTransactionitem(List<TransactionitemBean> transactionitem) {
            this.transactionitem = transactionitem;
        }

        public static class TransactionitemBean {
            /**
             * id : 32
             * transactionnumber : T10000019
             * amount : 1000.0
             * paymentmethod : 信用卡
             */

            private int id;
            private String transactionnumber;
            private String amount;
            private String paymentmethod;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTransactionnumber() {
                return transactionnumber;
            }

            public void setTransactionnumber(String transactionnumber) {
                this.transactionnumber = transactionnumber;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getPaymentmethod() {
                return paymentmethod;
            }

            public void setPaymentmethod(String paymentmethod) {
                this.paymentmethod = paymentmethod;
            }
        }
    }
}
