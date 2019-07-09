package com.bougainvillea.sanchit.easeparking.Response;

public class AddParkingCoordinateResponse {


    /**
     * success : true
     * message : Parking Patch Coordinate added successfully
     * data : {"id":"5bc04b12a3151410bc0ae7f8","patch_id":17436789,"color":"#ffff00","patch_active":true,"capacity":9,"patch_type":"freehand"}
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
         * id : 5bc04b12a3151410bc0ae7f8
         * patch_id : 17436789
         * color : #ffff00
         * patch_active : true
         * capacity : 9
         * patch_type : freehand
         */

        private String id;
        private int patch_id;
        private String color;
        private boolean patch_active;
        private int capacity;
        private String patch_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPatch_id() {
            return patch_id;
        }

        public void setPatch_id(int patch_id) {
            this.patch_id = patch_id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public boolean isPatch_active() {
            return patch_active;
        }

        public void setPatch_active(boolean patch_active) {
            this.patch_active = patch_active;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public String getPatch_type() {
            return patch_type;
        }

        public void setPatch_type(String patch_type) {
            this.patch_type = patch_type;
        }
    }
}
