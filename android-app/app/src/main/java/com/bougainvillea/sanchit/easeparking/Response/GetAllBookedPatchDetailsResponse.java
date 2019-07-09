package com.bougainvillea.sanchit.easeparking.Response;

import java.util.List;

public class GetAllBookedPatchDetailsResponse {


    /**
     * success : true
     * message : Fetched all bookings
     * data : [{"patch_id":21641353,"_pid":{"capacity":1,"capacity_count":0,"_id":"5bb769b434039f116819acd6"},"booking_id":95700205647,"user_id":{"_id":"5bb79ee2a4cd4f1c507bebc2","name":"Aditya","vehicle":[{"_id":"5bb79ee2a4cd4f1c507bebc4","type":"car","number":"MH-201800"},{"_id":"5bb79ee2a4cd4f1c507bebc3","type":"bike","number":"MH-201811"}]},"start_time":"14:23:45","end_time":"16:00:00","end_date":"2018-10-05","vehicleNo":"MH-04AD1234","book_status":true,"color":"#00ff00"}]
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
         * patch_id : 21641353
         * _pid : {"capacity":1,"capacity_count":0,"_id":"5bb769b434039f116819acd6"}
         * booking_id : 95700205647
         * user_id : {"_id":"5bb79ee2a4cd4f1c507bebc2","name":"Aditya","vehicle":[{"_id":"5bb79ee2a4cd4f1c507bebc4","type":"car","number":"MH-201800"},{"_id":"5bb79ee2a4cd4f1c507bebc3","type":"bike","number":"MH-201811"}]}
         * start_time : 14:23:45
         * end_time : 16:00:00
         * end_date : 2018-10-05
         * vehicleNo : MH-04AD1234
         * book_status : true
         * color : #00ff00
         */

        private int patch_id;
        private PidBean _pid;
        private long booking_id;
        private UserIdBean user_id;
        private String start_time;
        private String end_time;
        private String end_date;
        private String vehicleNo;
        private boolean book_status;
        private String color;

        public int getPatch_id() {
            return patch_id;
        }

        public void setPatch_id(int patch_id) {
            this.patch_id = patch_id;
        }

        public PidBean get_pid() {
            return _pid;
        }

        public void set_pid(PidBean _pid) {
            this._pid = _pid;
        }

        public long getBooking_id() {
            return booking_id;
        }

        public void setBooking_id(long booking_id) {
            this.booking_id = booking_id;
        }

        public UserIdBean getUser_id() {
            return user_id;
        }

        public void setUser_id(UserIdBean user_id) {
            this.user_id = user_id;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public boolean isBook_status() {
            return book_status;
        }

        public void setBook_status(boolean book_status) {
            this.book_status = book_status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public static class PidBean {
            /**
             * capacity : 1
             * capacity_count : 0
             * _id : 5bb769b434039f116819acd6
             */

            private int capacity;
            private int capacity_count;
            private String _id;
            private String patch_type;

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

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }

        public static class UserIdBean {
            /**
             * _id : 5bb79ee2a4cd4f1c507bebc2
             * name : Aditya
             * vehicle : [{"_id":"5bb79ee2a4cd4f1c507bebc4","type":"car","number":"MH-201800"},{"_id":"5bb79ee2a4cd4f1c507bebc3","type":"bike","number":"MH-201811"}]
             */

            private String _id;
            private String name;
            private List<VehicleBean> vehicle;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<VehicleBean> getVehicle() {
                return vehicle;
            }

            public void setVehicle(List<VehicleBean> vehicle) {
                this.vehicle = vehicle;
            }

            public static class VehicleBean {
                /**
                 * _id : 5bb79ee2a4cd4f1c507bebc4
                 * type : car
                 * number : MH-201800
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
}
