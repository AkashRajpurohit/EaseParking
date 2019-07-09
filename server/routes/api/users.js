const express = require('express')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const router = express.Router()

// Load keys
const keys = require('../../config/keys')

// Load models
const User = require('../../models/User')

/*
 * @route  - /api/v1/users/register
 * @method - POST
 * @desc   - Register a user
 * @access - Public
 */
router.post('/register', async (req, res) => {
  const body = req.body
  try {
    const userEmailExists = await User.findOne({ email: body.email })

    if (userEmailExists) {
      return res.status(400).json({
        success: false,
        message: 'Email id already registered',
        flag: -1,
      })
    }

    const userMobileNoExists = await User.findOne({ mobileNo: body.mobileNo })

    if (userMobileNoExists) {
      return res.status(400).json({
        success: false,
        message: 'Mobile number already exists',
        flag: -2,
      })
    }

    if (body.license_valid_date) {
      if (new Date().getTime() > new Date(body.license_valid_date).getTime()) {
        return res
          .status(400)
          .json({ success: false, message: 'License date expired', flag: -3 })
      }
    }

    const newUser = new User({
      user_type: body.wardNo ? 'contractor' : 'customer',
      name: body.name,
      wardNo: body.wardNo,
      gender: body.gender,
      dob: body.dob,
      email: body.email,
      password: body.password,
      mobileNo: body.mobileNo,
      license_no: body.license_no,
      license_valid_date: body.license_valid_date,
      vehicle: body.vehicle,
      address: body.address,
      city: body.city,
      state: body.state,
      active_status: true,
    })

    const salt = await bcrypt.genSalt(10)

    const hashPassword = await bcrypt.hash(newUser.password, salt)

    newUser.password = hashPassword

    const user = await newUser.save()

    if (user) {
      res.json({
        success: true,
        message: 'Registration successful',
        id: user.id,
        flag: 1,
      })
    } else {
      res.json({ success: false, message: 'Something went wrong' })
    }
  } catch (err) {
    console.log(err)
    res.json({ success: false, message: 'Something went wrong' })
  }
})

/*
 * @route  - /api/v1/users/login
 * @method - POST
 * @desc   - Login a user
 * @access - Public
 */
router.post('/login', async (req, res) => {
  const email = req.body.email
  const password = req.body.password

  try {
    const user = await User.findOne({ email })

    if (!user) {
      return res
        .status(400)
        .json({ success: false, message: 'User not found', flag: -1 })
    }

    if(!user.active_status) {
      return res
        .status(400)
        .json({ success: false, message: 'User not active', flag: -3 })
    }

    const passwordMatched = await bcrypt.compare(password, user.password)

    if (!passwordMatched) {
      return res
        .status(400)
        .json({ success: false, message: 'Password Incorrect', flag: -2 })
    }

    // All ok
    const token = await jwt.sign(
      { id: user.id, type: user.user_type },
      keys.JWT_SECRET,
      {
        expiresIn: '7 days',
      }
    )

    res.json({
      success: true,
      message: 'User Login Successful',
      flag: 1,
      token: 'Bearer ' + token,
      data: {
        user_id: user.id,
        name: user.name,
        email: user.email,
        user_type: user.user_type,
        active_status: user.active_status,
        vehicle: user.vehicle,
      },
    })
  } catch (err) {
    console.log(err)
  }
})

module.exports = router
