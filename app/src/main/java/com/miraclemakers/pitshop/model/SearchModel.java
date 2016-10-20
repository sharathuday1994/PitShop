package com.miraclemakers.pitshop.model;

/**
 * Created by nihalpradeep on 01/10/16.
 */
public class SearchModel {
String id,name,brand,country,year,seats,mileage,bodyType,price;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBodyType() {
        return bodyType;
    }

    public String getBrand() {
        return brand;
    }

    public String getCountry() {
        return country;
    }

    public String getMileage() {
        return mileage;
    }

    public String getSeats() {
        return seats;
    }

    public String getYear() {
        return year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
