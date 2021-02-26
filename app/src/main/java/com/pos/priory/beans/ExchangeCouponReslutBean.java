package com.pos.priory.beans;

import java.util.List;

public class ExchangeCouponReslutBean {

    /**
     * id : 0
     * status : true
     * coupon_type : 0
     * name : string
     * terms : string
     * code : string
     * value : string
     * xmin_amount : string
     * xmin_quantity : 0
     * coupon_type_y : 0
     * yquantity : 0
     * yvalue : string
     * with_another_coupon : true
     * per_order_max_usage : 0
     * reduce_reward : 0
     * starttime : 2020-12-18T06:51:05.530Z
     * endtime : 2020-12-18T06:51:05.530Z
     * created : 2020-12-18T06:51:05.530Z
     * updated : 2020-12-18T06:51:05.530Z
     * xitem : [0]
     * xcategory : [0]
     * yitem : [0]
     * ycategory : [0]
     */

    private int id;
    private boolean status;
    private int coupon_type;
    private String name;
    private String terms;
    private String code;
    private String value;
    private String xmin_amount;
    private int xmin_quantity;
    private int coupon_type_y;
    private int yquantity;
    private String yvalue;
    private boolean with_another_coupon;
    private int per_order_max_usage;
    private int reduce_reward;
    private String starttime;
    private String endtime;
    private String created;
    private String updated;
    private List<Integer> xitem;
    private List<Integer> xcategory;
    private List<Integer> yitem;
    private List<Integer> ycategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getXmin_amount() {
        return xmin_amount;
    }

    public void setXmin_amount(String xmin_amount) {
        this.xmin_amount = xmin_amount;
    }

    public int getXmin_quantity() {
        return xmin_quantity;
    }

    public void setXmin_quantity(int xmin_quantity) {
        this.xmin_quantity = xmin_quantity;
    }

    public int getCoupon_type_y() {
        return coupon_type_y;
    }

    public void setCoupon_type_y(int coupon_type_y) {
        this.coupon_type_y = coupon_type_y;
    }

    public int getYquantity() {
        return yquantity;
    }

    public void setYquantity(int yquantity) {
        this.yquantity = yquantity;
    }

    public String getYvalue() {
        return yvalue;
    }

    public void setYvalue(String yvalue) {
        this.yvalue = yvalue;
    }

    public boolean isWith_another_coupon() {
        return with_another_coupon;
    }

    public void setWith_another_coupon(boolean with_another_coupon) {
        this.with_another_coupon = with_another_coupon;
    }

    public int getPer_order_max_usage() {
        return per_order_max_usage;
    }

    public void setPer_order_max_usage(int per_order_max_usage) {
        this.per_order_max_usage = per_order_max_usage;
    }

    public int getReduce_reward() {
        return reduce_reward;
    }

    public void setReduce_reward(int reduce_reward) {
        this.reduce_reward = reduce_reward;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
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

    public List<Integer> getXitem() {
        return xitem;
    }

    public void setXitem(List<Integer> xitem) {
        this.xitem = xitem;
    }

    public List<Integer> getXcategory() {
        return xcategory;
    }

    public void setXcategory(List<Integer> xcategory) {
        this.xcategory = xcategory;
    }

    public List<Integer> getYitem() {
        return yitem;
    }

    public void setYitem(List<Integer> yitem) {
        this.yitem = yitem;
    }

    public List<Integer> getYcategory() {
        return ycategory;
    }

    public void setYcategory(List<Integer> ycategory) {
        this.ycategory = ycategory;
    }
}
