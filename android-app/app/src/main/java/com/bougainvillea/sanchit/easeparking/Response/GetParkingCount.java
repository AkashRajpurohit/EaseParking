package com.bougainvillea.sanchit.easeparking.Response;

public class GetParkingCount {

    /**
     * success : true
     * message : Patch with id 21641353 retrieved successfully
     * patch_type : patch
     * capacity : 1
     * capacity_count : 2
     */

    private boolean success;
    private String message;
    private String patch_type;
    private int capacity;
    private int capacity_count;

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

    public int getCapacity_count() {
        return capacity_count;
    }

    public void setCapacity_count(int capacity_count) {
        this.capacity_count = capacity_count;
    }
}
