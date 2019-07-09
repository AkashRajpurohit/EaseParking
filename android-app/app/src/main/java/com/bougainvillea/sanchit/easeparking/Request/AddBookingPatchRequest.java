package com.bougainvillea.sanchit.easeparking.Request;

public class AddBookingPatchRequest {

    /**
     * patch_id : 69058199
     * user_id : 5bb2e0b127fbae18b0e62595
     * time : 12:40:00
     * charge : 100
     * vehicleNo : abc123
     */

    private int patch_id;
    private String user_id;
    private String start_time;
    private int charge;
    private String start_date;
    private String vehicleNo;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_Date() {
        return start_date;
    }

    public void setStart_Date(String start_date) {
        this.start_date = start_date;
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

    public String getStart_Time() {
        return start_time;
    }

    public void setTime(String start_time) {
        this.start_time = start_time;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
