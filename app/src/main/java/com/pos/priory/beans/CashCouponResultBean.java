package com.pos.priory.beans;

import java.util.List;

public class CashCouponResultBean {

    /**
     * id : 24
     * name : 現金券100
     * terms : 現金券100
     * value : 100.0
     * starttime : 2020-12-18 07:31:01
     * endtime : 2021-12-18 07:31:01
     * per_order_min_amount : 100.1
     * with_another_coupon : true
     * per_order_max_usage : 1
     * reduce_reward : 0
     * usable : false
     * shops_list : [1,2,3]
     * code : ZFD35DQA
     * exchange : true
     * exchange_datetime : 2020-12-18T16:24:50.978121+08:00
     * used : false
     * used_datetime : string
     * created : 2020-12-18T15:33:09.022797+08:00
     * updated : 2020-12-18T16:24:51.031978+08:00
     * cashcoupon : 8
     */

    private int id;
    private String name;
    private String terms;
    private double value;
    private String starttime;
    private String endtime;
    private double per_order_min_amount;
    private boolean with_another_coupon;
    private int per_order_max_usage;
    private int reduce_reward;
    private boolean usable;
    private String code;
    private boolean exchange;
    private String exchange_datetime;
    private boolean used;
    private String used_datetime;
    private String created;
    private String updated;
    private int cashcoupon;
    private List<Integer> shops_list;

    boolean isSelected = false;
    boolean isShowDetail = false;

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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public double getPer_order_min_amount() {
        return per_order_min_amount;
    }

    public void setPer_order_min_amount(double per_order_min_amount) {
        this.per_order_min_amount = per_order_min_amount;
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

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isExchange() {
        return exchange;
    }

    public void setExchange(boolean exchange) {
        this.exchange = exchange;
    }

    public String getExchange_datetime() {
        return exchange_datetime;
    }

    public void setExchange_datetime(String exchange_datetime) {
        this.exchange_datetime = exchange_datetime;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getUsed_datetime() {
        return used_datetime;
    }

    public void setUsed_datetime(String used_datetime) {
        this.used_datetime = used_datetime;
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

    public int getCashcoupon() {
        return cashcoupon;
    }

    public void setCashcoupon(int cashcoupon) {
        this.cashcoupon = cashcoupon;
    }

    public List<Integer> getShops_list() {
        return shops_list;
    }

    public void setShops_list(List<Integer> shops_list) {
        this.shops_list = shops_list;
    }



}
