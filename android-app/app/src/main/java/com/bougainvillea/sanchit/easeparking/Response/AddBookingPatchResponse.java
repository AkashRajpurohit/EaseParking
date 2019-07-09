package com.bougainvillea.sanchit.easeparking.Response;

public class AddBookingPatchResponse {

    /**
     * success : true
     * message : Parking successfully booked
     * data : {"booking_id":54110967130,"patch_id":69058199,"user_id":"5bb2e0b127fbae18b0e62595","vehicleNo":"abc123"}
     */

    private boolean success;
    private String message;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * booking_id : 54110967130
         * patch_id : 69058199
         * user_id : 5bb2e0b127fbae18b0e62595
         * vehicleNo : abc123
         */

        private long booking_id;
        private int patch_id;
        private String user_id;
        private String vehicleNo;

        public long getBooking_id() {
            return booking_id;
        }

        public void setBooking_id(long booking_id) {
            this.booking_id = booking_id;
        }

        public int getPatch_id() {
            return patch_id;
        }

        public void setPatch_id(int patch_id) {
            this.patch_id = patch_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }
    }
}
