package com.pos.priory.beans;

public class ExchangeCashCouponReslutBean {

    /**
     * id : 0
     * name : string
     * terms : string
     * value : string
     * starttime : 2020-12-18T09:54:07.917Z
     * endtime : 2020-12-18T09:54:07.917Z
     * per_order_min_amount : string
     * with_another_coupon : true
     * per_order_max_usage : 0
     * reduce_reward : 0
     * created : 2020-12-18T09:54:07.917Z
     * updated : 2020-12-18T09:54:07.917Z
     * member : 0
     * cashcouponitem : 0
     */

    private int id;
    private String name;
    private String terms;
    private String value;
    private String starttime;
    private String endtime;
    private String per_order_min_amount;
    private boolean with_another_coupon;
    private int per_order_max_usage;
    private int reduce_reward;
    private String created;
    private String updated;
    private int member;
    private int cashcouponitem;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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

    public String getPer_order_min_amount() {
        return per_order_min_amount;
    }

    public void setPer_order_min_amount(String per_order_min_amount) {
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

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getCashcouponitem() {
        return cashcouponitem;
    }

    public void setCashcouponitem(int cashcouponitem) {
        this.cashcouponitem = cashcouponitem;
    }
}
