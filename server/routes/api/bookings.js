const express = require('express')
const router = express.Router()

// Load Parking Model
const Parking = require('../../models/Parking')
// Load Booking Model
const Booking = require('../../models/Booking')
// Ensure Auth
const ensureAuthenticated = require('../../utils/ensureAuthenticated')
// Authorize user
const authUser = require('../../utils/authorizeUser')

/*
 * @route  - api/v1/bookings/add
 * @method - POST
 * @desc   - Book a parking space - Done by customer
 * @access - Private
 */
router.post('/add', ensureAuthenticated, async (req, res) => {
  // Check is user is customer
  const { user_type } = req
  const error = authUser(user_type, 'customer')
  if (error) {
    return res.status(401).json(error)
  }
  try {
    const parking = await Parking.findOne({ patch_id: req.body.patch_id })
    if (!parking) {
      // No patch exists for the patch id
      return res
        .status(400)
        .json({ success: false, message: 'Patch doesnt exist' })
    }
    // Check for the capacity of parking
    if (parking.capacity_count >= parking.capacity) {
      return res
        .status(400)
        .json({ success: false, message: 'The booking capacity exceeded' })
    }
    // Book on the patch
    const newBooking = new Booking({
      booking_id: Math.floor(10000000000 + Math.random() * 90000000000),
      _pid: parking._id,
      patch_id: req.body.patch_id,
      user_id: req.body.user_id,
      start_date: req.body.start_date,
      start_time: req.body.start_time,
      charge: req.body.charge,
      vehicleNo: req.body.vehicleNo,
    })

    const booking = await newBooking.save()

    // Change the capacity_count
    parking.capacity_count += 1

    await parking.save()

    return res.json({
      success: true,
      message: 'Parking successfully booked',
      data: {
        booking_id: booking.booking_id,
        patch_id: booking.patch_id,
        _pid: booking._pid,
        user_id: booking.user_id,
        vehicleNo: booking.vehicleNo,
      },
    })
  } catch (e) {
    console.log(e)
  }
})

/*
 * @route  - api/v1/bookings/all
 * @method - GET
 * @desc   - Get all bookings
 * @access - Private
 */
router.get('/all', ensureAuthenticated, async (req, res) => {
  // Check is user is customer
  const { user_type } = req
  const error = authUser(user_type, 'customer')
  if (error) {
    return res.status(401).json(error)
  }

  try {
    const booking = await Booking.find()
      .populate('user_id', ['user_id', 'name', 'vehicle'])
      .populate('_pid', ['_pid', 'capacity', 'capacity_count', 'patch_type'])
    if (!booking || !booking.length) {
      return res
        .status(400)
        .json({ success: false, message: 'No Bookings added' })
    }

    const _data = booking.map(item => {
      if (item._pid) {
        let capacity = item._pid.capacity
        let capacity_count = item._pid.capacity_count
        let capacity_percent = (capacity_count / capacity) * 100
        let color
        if (capacity_percent >= 100) color = '#4f4f4f'
        if (capacity_percent >= 50 && capacity_percent < 100) color = '#e27e3b'
        if (capacity_percent >= 0 && capacity_percent < 50) color = '#00ff00'
        return {
          patch_id: item.patch_id,
          _pid: item._pid,
          booking_id: item.booking_id,
          user_id: item.user_id,
          start_time: item.start_time,
          start_date: item.start_date,
          end_time: item.end_time,
          end_date: item.end_date,
          vehicleNo: item.vehicleNo,
          book_status: item.book_status,
          color: color,
        }
      }
    })

    res.json({
      success: true,
      message: 'Fetched all bookings',
      data: _data,
    })
  } catch (e) {
    console.log(e)
  }
})

/*
 * @route  - api/v1/bookings/disable/:id
 * @method - PATCH
 * @desc   - Complete the endPhase a particular patch
 * @access - Private
 */
router.patch('/disable', ensureAuthenticated, async (req, res) => {
  // Check is user is customer
  const { user_type } = req
  const error = authUser(user_type, 'customer')
  if (error) {
    return res.status(401).json(error)
  }

  try {
    const booking = await Booking.findOne({
      user_id: req.body.user_id,
      book_status: true,
    })
    if (!booking) {
      return res.status(400).json({
        success: false,
        message: 'No running booking for this user',
      })
    }

    const parking = await Parking.findById(booking._pid)

    if (!parking) {
      return res.status(400).json({ success: false, message: 'No patch found' })
    }

    booking.book_status = !booking.book_status
    booking.end_time = req.body.end_time
    booking.end_date = req.body.end_date

    // Change the book_count
    parking.book_count += 1
    // Change capacity_count
    parking.capacity_count -= 1
    // Save

    await booking.save()
    await parking.save()

    return res.json({
      success: true,
      message:
        'Booking finished for booking id ' +
        booking.booking_id +
        ' with parking id ' +
        parking.patch_id,
    })
  } catch (e) {
    console.log(e)
  }
})

module.exports = router
