package com.miraclemakers.pitshop.model;

/**
 * Created by nihalpradeep on 19/10/16.
 */
public class VariantModel {
    String id,name,price,colour;

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
