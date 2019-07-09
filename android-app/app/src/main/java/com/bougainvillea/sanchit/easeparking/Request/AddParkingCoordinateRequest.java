package com.bougainvillea.sanchit.easeparking.Request;

import java.util.List;

public class AddParkingCoordinateRequest {

    /**
     * name : Andheri
     * landmark : Banjara Hotel
     * city : mumbai
     * pincode : 400072
     * state : Maharashtra
     * id : 5bb5d78995c4ec045052c3e5
     * patch_type : freehand
     * capacity : 9
     * color : #ffff00
     * coordinates : [{"lat":19.11475720203527,"lng":72.89512731134892},{"lat":19.114771774399664,"lng":72.89513032883406}]
     */

    private String name;
    private String landmark;
    private String city;
    private int pincode;
    private String state;
    private String id;
    private String patch_type;
    private int capacity;
    private String color;
    private List<CoordinatesBean> coordinates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatch_type() {
        return patch_type;
    }

    public void setPatch_type(String patch_type) {
        this.patch_type = patch_type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<CoordinatesBean> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<CoordinatesBean> coordinates) {
        this.coordinates = coordinates;
    }

    public static class CoordinatesBean {
        /**
         * lat : 19.11475720203527
         * lng : 72.89512731134892
         */

        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
