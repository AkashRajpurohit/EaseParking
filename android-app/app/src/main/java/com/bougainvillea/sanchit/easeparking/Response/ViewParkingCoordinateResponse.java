package com.bougainvillea.sanchit.easeparking.Response;

import java.util.List;

public class ViewParkingCoordinateResponse {


    /**
     * success : true
     * message : Successfully fetched all patches
     * data : [{"id":"5bc04b12a3151410bc0ae7f8","patch_id":17436789,"color":"#ffff00","patch_active":true,"coordinates":[{"_id":"5bc04b12a3151410bc0ae7fa","lat":19.11475720203527,"lng":72.89512731134892},{"_id":"5bc04b12a3151410bc0ae7f9","lat":19.114771774399664,"lng":72.89513032883406}],"book_count":0,"capacity":9,"capacity_count":0,"patch_type":"freehand"}]
     */

    private boolean success;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5bc04b12a3151410bc0ae7f8
         * patch_id : 17436789
         * color : #ffff00
         * patch_active : true
         * coordinates : [{"_id":"5bc04b12a3151410bc0ae7fa","lat":19.11475720203527,"lng":72.89512731134892},{"_id":"5bc04b12a3151410bc0ae7f9","lat":19.114771774399664,"lng":72.89513032883406}]
         * book_count : 0
         * capacity : 9
         * capacity_count : 0
         * patch_type : freehand
         */

        private String id;
        private int patch_id;
        private String color;
        private boolean patch_active;
        private int book_count;
        private int capacity;
        private int capacity_count;
        private String patch_type;
        private List<CoordinatesBean> coordinates;

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

        public int getBook_count() {
            return book_count;
        }

        public void setBook_count(int book_count) {
            this.book_count = book_count;
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

        public String getPatch_type() {
            return patch_type;
        }

        public void setPatch_type(String patch_type) {
            this.patch_type = patch_type;
        }

        public List<CoordinatesBean> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<CoordinatesBean> coordinates) {
            this.coordinates = coordinates;
        }

        public static class CoordinatesBean {
            /**
             * _id : 5bc04b12a3151410bc0ae7fa
             * lat : 19.11475720203527
             * lng : 72.89512731134892
             */

            private String _id;
            private double lat;
            private double lng;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

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
}
