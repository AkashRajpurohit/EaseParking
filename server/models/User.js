const mongoose = require('mongoose')
const Schema = mongoose.Schema

const UserSchema = new Schema({
  user_type: {
    type: String,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  wardNo: {
    type: String,
  },
  gender: {
    type: String,
  },
  dob: {
    type: String,
  },
  email: {
    type: String,
    required: true,
  },
  password: {
    type: String,
    required: true,
  },
  mobileNo: {
    type: Number,
  },
  license_no: {
    type: String,
  },
  license_valid_date: {
    type: String,
  },
  vehicle: [
    {
      type: {
        type: String,
      },
      number: {
        type: String,
      },
    },
  ],
  address: {
    type: String,
  },
  city: {
    type: String,
  },
  state: {
    type: String,
  },
  active_status: {
    type: Boolean,
  },
})

module.exports = User = mongoose.model('users', UserSchema)
