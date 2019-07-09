package com.bougainvillea.sanchit.easeparking.Beans;

import java.io.Serializable;

public class CustomPosition implements Serializable {
    double lati;
    double longi;
    String colour;
    int patch_id;

    public CustomPosition(double lati, double longi, String colour, int patch_id) {
        this.lati = lati;
        this.longi = longi;
        this.colour = colour;
        this.patch_id = patch_id;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getPatch_id() {
        return patch_id;
    }

    public void setPatch_id(int patch_id) {
        this.patch_id = patch_id;
    }
}

