package com.pos.priory.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class OrderDetailReslutBean {

    private int id;
    private int order_status;
    private String order_status_display;
    private GoldInfoBean gold_info;
    private int quantity_accessory;
    private double amount_payable;
    private double amount_paid;
    private double exchange_rate;
    private double goldprice;
    private String created;
    private String updated;
    private String orderno;
    private boolean status;
    private String remark;
    private int staff;
    private int shop;
    private int member;
    private List<OrderItemsBean> order_items;
    private PayDetailBean pay_detail;
    private MemberDetailBean member_detail;

    public double getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public double getGoldprice() {
        return goldprice;
    }

    public void setGoldprice(double goldprice) {
        this.goldprice = goldprice;
    }

    public MemberDetailBean getMember_detail() {
        return member_detail;
    }

    public void setMember_detail(MemberDetailBean member_detail) {
        this.member_detail = member_detail;
    }

    public String getOrder_status_display() {
        return order_status_display;
    }

    public void setOrder_status_display(String order_status_display) {
        this.order_status_display = order_status_display;
    }

    public PayDetailBean getPay_detail() {
        return pay_detail;
    }

    public void setPay_detail(PayDetailBean pay_detail) {
        this.pay_detail = pay_detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }


    public GoldInfoBean getGold_info() {
        return gold_info;
    }

    public void setGold_info(GoldInfoBean gold_info) {
        this.gold_info = gold_info;
    }

    public int getQuantity_accessory() {
        return quantity_accessory;
    }

    public void setQuantity_accessory(int quantity_accessory) {
        this.quantity_accessory = quantity_accessory;
    }

    public double getAmount_payable() {
        return amount_payable;
    }

    public void setAmount_payable(double amount_payable) {
        this.amount_payable = amount_payable;
    }

    public double getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(double amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public List<OrderItemsBean> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderItemsBean> order_items) {
        this.order_items = order_items;
    }

    public static class MemberDetailBean{

        /**
         * id : 0
         * shop_name : string
         * status : true
         * created : 2021-01-08T02:09:15.508Z
         * updated : 2021-01-08T02:09:15.508Z
         * mobile : string
         * firstname : string
         * lastname : string
         * gender : 2
         * reward : 0
         * remark : string
         * shop : 0
         */

        private int id;
        private String shop_name;
        private boolean status;
        private String created;
        private String updated;
        private String mobile;
        private String firstname;
        private String lastname;
        private int gender;
        private int reward;
        private String remark;
        private int shop;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getShop() {
            return shop;
        }

        public void setShop(int shop) {
            this.shop = shop;
        }
    }

    public static class GoldInfoBean {
        /**
         * quantity : 0
         * weight : 0.0
         */

        private int quantity;
        private double weight;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public static class PayDetailBean{


        /**
         * originprice : 978.0
         * quantity_total : 12
         * quantity_gold : 2
         * weight_gold : 100.0
         * quantity_accessory : 10
         * coupons : [{"name":"手繩類 滿500 9折","coupon_amount":0},{"name":"手繩類 滿500 9折","coupon_amount":0}]
         * coupons_amount : 0.0
         * payable_amount : 978.0
         * pay_methods : {"cash_coupon":[{"paymethod":"現金券","amount":100,"cash_coupon_name":"現金券100"}],"other":[{"paymethod":"信用卡","amount":900},{"paymethod":"現金","amount":48}],"exchange_or_refund":[{"amount":-48}]}
         * pay_total : 948.0
         */

        private double originprice;
        private int quantity_total;
        private int quantity_gold;
        private double weight_gold;
        private int quantity_accessory;
        private double coupons_amount;
        private double payable_amount;
        private PayMethodsBean pay_methods;
        private double pay_total;
        private List<CouponsBean> coupons;

        public double getOriginprice() {
            return originprice;
        }

        public void setOriginprice(double originprice) {
            this.originprice = originprice;
        }

        public int getQuantity_total() {
            return quantity_total;
        }

        public void setQuantity_total(int quantity_total) {
            this.quantity_total = quantity_total;
        }

        public int getQuantity_gold() {
            return quantity_gold;
        }

        public void setQuantity_gold(int quantity_gold) {
            this.quantity_gold = quantity_gold;
        }

        public double getWeight_gold() {
            return weight_gold;
        }

        public void setWeight_gold(double weight_gold) {
            this.weight_gold = weight_gold;
        }

        public int getQuantity_accessory() {
            return quantity_accessory;
        }

        public void setQuantity_accessory(int quantity_accessory) {
            this.quantity_accessory = quantity_accessory;
        }

        public double getCoupons_amount() {
            return coupons_amount;
        }

        public void setCoupons_amount(double coupons_amount) {
            this.coupons_amount = coupons_amount;
        }

        public double getPayable_amount() {
            return payable_amount;
        }

        public void setPayable_amount(double payable_amount) {
            this.payable_amount = payable_amount;
        }

        public PayMethodsBean getPay_methods() {
            return pay_methods;
        }

        public void setPay_methods(PayMethodsBean pay_methods) {
            this.pay_methods = pay_methods;
        }

        public double getPay_total() {
            return pay_total;
        }

        public void setPay_total(double pay_total) {
            this.pay_total = pay_total;
        }

        public List<CouponsBean> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<CouponsBean> coupons) {
            this.coupons = coupons;
        }

        public static class PayMethodsBean {
            private List<CashCouponBean> cash_coupon;
            private List<OtherBean> other;
            private List<ExchangeOrRefundBean> exchange_or_refund;

            public List<CashCouponBean> getCash_coupon() {
                return cash_coupon;
            }

            public void setCash_coupon(List<CashCouponBean> cash_coupon) {
                this.cash_coupon = cash_coupon;
            }

            public List<OtherBean> getOther() {
                return other;
            }

            public void setOther(List<OtherBean> other) {
                this.other = other;
            }

            public List<ExchangeOrRefundBean> getExchange_or_refund() {
                return exchange_or_refund;
            }

            public void setExchange_or_refund(List<ExchangeOrRefundBean> exchange_or_refund) {
                this.exchange_or_refund = exchange_or_refund;
            }

            public static class CashCouponBean {
                /**
                 * paymethod : 現金券
                 * amount : 100.0
                 * cash_coupon_name : 現金券100
                 */

                private String paymethod;
                private double amount;
                private String cash_coupon_name;

                public String getPaymethod() {
                    return paymethod;
                }

                public void setPaymethod(String paymethod) {
                    this.paymethod = paymethod;
                }

                public double getAmount() {
                    return amount;
                }

                public void setAmount(double amount) {
                    this.amount = amount;
                }

                public String getCash_coupon_name() {
                    return cash_coupon_name;
                }

                public void setCash_coupon_name(String cash_coupon_name) {
                    this.cash_coupon_name = cash_coupon_name;
                }
            }

            public static class OtherBean {
                /**
                 * paymethod : 信用卡
                 * amount : 900.0
                 */

                private String paymethod;
                private double amount;

                public String getPaymethod() {
                    return paymethod;
                }

                public void setPaymethod(String paymethod) {
                    this.paymethod = paymethod;
                }

                public double getAmount() {
                    return amount;
                }

                public void setAmount(double amount) {
                    this.amount = amount;
                }
            }

            public static class ExchangeOrRefundBean {
                /**
                 * amount : -48.0
                 */

                private double amount;

                public double getAmount() {
                    return amount;
                }

                public void setAmount(double amount) {
                    this.amount = amount;
                }
            }
        }

        public static class CouponsBean {
            /**
             * name : 手繩類 滿500 9折
             * coupon_amount : 0.0
             */

            private String name;
            private double coupon_amount;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getCoupon_amount() {
                return coupon_amount;
            }

            public void setCoupon_amount(double coupon_amount) {
                this.coupon_amount = coupon_amount;
            }
        }
    }

    public static class OrderItemsBean implements MultiItemEntity {
        public int itemType = 0;

        @Override
        public int getItemType() {
            return itemType;
        }


        /**
         * id : 25
         * prd_name : 千足金闪闪生辉E
         * number : 1005122800000005
         * item_status : 正常
         * quantity : 2
         * returntype : 3
         * returned : false
         * originprice : 199.0
         * price : 199.0
         * created : 2020-12-30T17:35:17.227455+08:00
         * updated : 2020-12-30T17:35:17.227455+08:00
         * order : 16
         * item : 13
         * currency : 1
         */

        private int id;
        private String prd_name;
        private String prd_image;
        private String number;
        private String item_status;
        private int quantity;
        private boolean is_gold;
        private int returntype;
        private boolean returned;
        private double originprice;
        private double price;
        private String created;
        private String updated;
        private int order;
        private int item;
        private int currency;

        private double weight;

        public boolean isIs_gold() {
            return is_gold;
        }

        public void setIs_gold(boolean is_gold) {
            this.is_gold = is_gold;
        }

        public String getPrd_image() {
            return prd_image;
        }

        public void setPrd_image(String prd_image) {
            this.prd_image = prd_image;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        int oprateCount = 0;

        boolean isSelected = false;

        public int getOprateCount() {
            return oprateCount;
        }

        public void setOprateCount(int oprateCount) {
            this.oprateCount = oprateCount;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrd_name() {
            return prd_name;
        }

        public void setPrd_name(String prd_name) {
            this.prd_name = prd_name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getItem_status() {
            return item_status;
        }

        public void setItem_status(String item_status) {
            this.item_status = item_status;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getReturntype() {
            return returntype;
        }

        public void setReturntype(int returntype) {
            this.returntype = returntype;
        }

        public boolean isReturned() {
            return returned;
        }

        public void setReturned(boolean returned) {
            this.returned = returned;
        }

        public double getOriginprice() {
            return originprice;
        }

        public void setOriginprice(double originprice) {
            this.originprice = originprice;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getItem() {
            return item;
        }

        public void setItem(int item) {
            this.item = item;
        }

        public int getCurrency() {
            return currency;
        }

        public void setCurrency(int currency) {
            this.currency = currency;
        }
    }
}
