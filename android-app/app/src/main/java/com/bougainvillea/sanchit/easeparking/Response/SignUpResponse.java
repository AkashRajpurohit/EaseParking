package com.bougainvillea.sanchit.easeparking.Response;

public class SignUpResponse {

    /**
     * success : true
     * message : Customer Registration successful
     * id : 5bb2e0b127fbae18b0e62595
     * flag : 1
     */

    private boolean success;
    private String message;
    private String id;
    private int flag;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
