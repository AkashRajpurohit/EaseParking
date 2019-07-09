const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const BookingSchema = new Schema({
  _pid: {
    type: Schema.Types.ObjectId,
    ref: "parkings"
  },
  patch_id: {
    type: Number,
    required: true
  },
  booking_id: {
    type: Number,
    required: true
  },
  user_id: {
    type: Schema.Types.ObjectId,
    ref: "users"
  },
  start_date: {
    type: String
  },
  start_time: {
    type: String
  },
  end_date: {
    type: String
  },
  end_time: {
    type: String
  },
  charge: {
    type: Number,
    default: 0
  },
  vehicleNo: {
    type: String
  },
  book_status: {
    type: Boolean,
    default: true
  }
});

module.exports = Booking = mongoose.model("bookings", BookingSchema);
