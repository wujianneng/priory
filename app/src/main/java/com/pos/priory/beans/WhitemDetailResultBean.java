package com.pos.priory.beans;

import java.util.List;

public class WhitemDetailResultBean {

    /**
     * id : 0
     * prd_code : string
     * prd_image : string
     * whitem : [{"id":0,"prd_no":"string","weight":0}]
     * prd_name : string
     * prd_weight : 0
     * quantity_total : 0
     */

    private int id;
    private String prd_code;
    private String prd_image;
    private String prd_name;
    private double prd_weight;
    private int quantity_total;
    private List<WhitemBean> whitem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrd_code() {
        return prd_code;
    }

    public void setPrd_code(String prd_code) {
        this.prd_code = prd_code;
    }

    public String getPrd_image() {
        return prd_image;
    }

    public void setPrd_image(String prd_image) {
        this.prd_image = prd_image;
    }

    public String getPrd_name() {
        return prd_name;
    }

    public void setPrd_name(String prd_name) {
        this.prd_name = prd_name;
    }

    public double getPrd_weight() {
        return prd_weight;
    }

    public void setPrd_weight(double prd_weight) {
        this.prd_weight = prd_weight;
    }

    public int getQuantity_total() {
        return quantity_total;
    }

    public void setQuantity_total(int quantity_total) {
        this.quantity_total = quantity_total;
    }

    public List<WhitemBean> getWhitem() {
        return whitem;
    }

    public void setWhitem(List<WhitemBean> whitem) {
        this.whitem = whitem;
    }

    public static class WhitemBean {
        /**
         * id : 0
         * prd_no : string
         * weight : 0
         */

        private int id;
        private String prd_no;
        private double weight;
        boolean isSelected;

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

        public String getPrd_no() {
            return prd_no;
        }

        public void setPrd_no(String prd_no) {
            this.prd_no = prd_no;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }
}
