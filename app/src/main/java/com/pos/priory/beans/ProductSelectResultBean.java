package com.pos.priory.beans;


public class ProductSelectResultBean {

    /**
     * id : 13
     * name : 测试
     * namezh : 测试
     * productcode : 1009
     * weight : 0.0
     * unitprice : 299.0
     * image : http://192.168.50.237:8000/media/uploads/0a508d0a-b3c1-4f92-a3ae-f190210db52f.jpg
     * sales_quantity : 0
     */

    private int id;
    private String name;
    private String namezh;
    private String productcode;
    private double weight;
    private double unitprice;
    private String image;
    private int sales_quantity;

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

    public String getNamezh() {
        return namezh;
    }

    public void setNamezh(String namezh) {
        this.namezh = namezh;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSales_quantity() {
        return sales_quantity;
    }

    public void setSales_quantity(int sales_quantity) {
        this.sales_quantity = sales_quantity;
    }
}
