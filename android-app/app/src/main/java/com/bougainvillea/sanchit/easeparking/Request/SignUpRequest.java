package com.bougainvillea.sanchit.easeparking.Request;

import java.util.List;

public class SignUpRequest {

    /**
     * name : Akash
     * gender : male
     * email : akash@gmail.com
     * password : 123456
     * dob : 2018-09-01
     * mobileNo : 8698015453
     * licenseNo : MH-123456
     * licenseValidDate : 2020-10-15
     * address : vasai
     * city : mumbai
     * state : Maharashtra
     * vehicle : [{"type":"bike","number":"abc123"},{"type":"car","number":"xyz987"}]
     */

    private String name;
    private String gender;
    private String email;
    private String password;
    private String dob;
    private long mobileNo;
    private String licenseNo;
    private String licenseValidDate;
    private String address;
    private String city;
    private String state;
    private List<VehicleBean> vehicle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getLicenseValidDate() {
        return licenseValidDate;
    }

    public void setLicenseValidDate(String licenseValidDate) {
        this.licenseValidDate = licenseValidDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<VehicleBean> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<VehicleBean> vehicle) {
        this.vehicle = vehicle;
    }

    public static class VehicleBean {
        /**
         * type : bike
         * number : abc123
         */

        private String type;
        private String number;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
