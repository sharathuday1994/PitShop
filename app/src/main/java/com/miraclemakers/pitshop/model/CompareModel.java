package com.miraclemakers.pitshop.model;

/**
 * Created by nihalpradeep on 20/10/16.
 */
public class CompareModel {
    String spec1,spec2,paramter,type;

    public String getParamter() {
        return paramter;
    }

    public String getSpec1() {
        return spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public String getType() {
        return type;
    }

    public void setParamter(String paramter) {
        this.paramter = paramter;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public void setType(String type) {
        this.type = type;
    }
}
