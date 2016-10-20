package com.miraclemakers.pitshop.model;

/**
 * Created by nihalpradeep on 20/10/16.
 */
public class VintageModel {
    String name,brand,price,model,year,driven;

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDriven() {
        return driven;
    }

    public String getPrice() {
        return price;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDriven(String driven) {
        this.driven = driven;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
