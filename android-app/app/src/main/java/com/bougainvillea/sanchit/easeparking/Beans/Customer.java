package com.bougainvillea.sanchit.easeparking.Beans;

import com.bougainvillea.sanchit.easeparking.Response.LoginResponse;

import java.util.List;

public class Customer {
    private String user_id;
    private String name;
    private String email;
    private String user_type;
    private boolean active_status;
    private List<LoginResponse.DataBean.VehicleBean> vehicle;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public boolean isActive_status() {
        return active_status;
    }

    public void setActive_status(boolean active_status) {
        this.active_status = active_status;
    }

    public List<LoginResponse.DataBean.VehicleBean> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<LoginResponse.DataBean.VehicleBean> vehicle) {
        this.vehicle = vehicle;
    }

    public static class VehicleBean {
        /**
         * _id : 5bb2e0b127fbae18b0e62597
         * type : bike
         * number : abc123
         */

        private String _id;
        private String type;
        private String number;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

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
