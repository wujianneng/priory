package com.pos.priory.beans;

import java.util.List;

public class CouponResultBean {


    /**
     * id : 4
     * is_usable : false
     * status : true
     * coupon_type : 1
     * name : 满1000减100
     * terms : 满1000减100
     * code : VYVEYI4I
     * value : 100.00
     * xmin_amount : 1000.00
     * xmin_quantity : 0
     * coupon_type_y : 0
     * yquantity : 0
     * yvalue : 0.00
     * with_another_coupon : true
     * per_order_max_usage : 1
     * reduce_reward : 0
     * starttime : 2020-12-16T00:00:00+08:00
     * endtime : 2021-01-31T23:59:59+08:00
     * created : 2020-12-15T15:07:36.287117+08:00
     * updated : 2020-12-15T15:07:36.287117+08:00
     * xitem : [1]
     * xcategory : []
     * yitem : []
     * ycategory : []
     */

    private int id;
    private boolean is_usable;
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

    boolean isShowDetail = false;
    boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isShowDetail() {
        return isShowDetail;
    }

    public void setShowDetail(boolean showDetail) {
        isShowDetail = showDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_usable() {
        return is_usable;
    }

    public void setIs_usable(boolean is_usable) {
        this.is_usable = is_usable;
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
