package com.bougainvillea.sanchit.easeparking.Response;

import java.util.List;

public class LoginResponse {

    /**
     * success : true
     * message : User Login Successful
     * flag : 1
     * data : {"user_id":"5bb2e0b127fbae18b0e62595","name":"Akash","email":"akash@gmail.com","user_type":"customer","active_status":true,"vehicle":[{"_id":"5bb2e0b127fbae18b0e62597","type":"bike","number":"abc123"},{"_id":"5bb2e0b127fbae18b0e62596","type":"car","number":"xyz987"}]}
     */

    private boolean success;
    private String message;
    private int flag;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 5bb2e0b127fbae18b0e62595
         * name : Akash
         * email : akash@gmail.com
         * user_type : customer
         * active_status : true
         * vehicle : [{"_id":"5bb2e0b127fbae18b0e62597","type":"bike","number":"abc123"},{"_id":"5bb2e0b127fbae18b0e62596","type":"car","number":"xyz987"}]
         */

        private String user_id;
        private String name;
        private String email;
        private String user_type;
        private boolean active_status;
        private List<VehicleBean> vehicle;

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

        public List<VehicleBean> getVehicle() {
            return vehicle;
        }

        public void setVehicle(List<VehicleBean> vehicle) {
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
}
