const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const ParkingSchema = new Schema({
  patch_id: {
    type: Number,
    required: true
  },
  user_id: {
    type: Schema.Types.ObjectId,
    ref: "users"
  },
  name: {
    type: String
  },
  landmark: {
    type: String
  },
  state: {
    type: String
  },
  city: {
    type: String
  },
  pincode: {
    type: Number
  },
  color: {
    type: String
  },
  capacity: {
    type: Number,
    default: 1
  },
  capacity_count: {
    type: Number,
    default: 0
  },
  patch_active: {
    type: Boolean,
    default: true
  },
  patch_type: {
    type: String,
    default: "patch"
  },
  book_count: {
    type: Number,
    default: 0
  },
  coordinates: [
    {
      lat: {
        type: Number,
        required: true
      },
      lng: {
        type: Number,
        required: true
      }
    }
  ],
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = Parking = mongoose.model("parkings", ParkingSchema);
