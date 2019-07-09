const express = require('express')
const router = express.Router()

// Load Parking Model
const Parking = require('../../models/Parking')
// Ensure Auth
const ensureAuthenticated = require('../../utils/ensureAuthenticated')
// Authorize user
const authUser = require('../../utils/authorizeUser')

/*
 * @route  - api/v1/parkings/add
 * @method - POST
 * @desc   - Add a parking coordinate
 * @access - Private
 */
router.post('/add', ensureAuthenticated, async (req, res) => {
  // Check is user is contractor
  const { user_type } = req
  const error = authUser(user_type, 'contractor')
  if (error) {
    return res.status(401).json(error)
  }

  const newParking = new Parking({
    patch_id: Math.floor(10000000 + Math.random() * 90000000),
    user_id: req.body.id,
    name: req.body.name,
    landmark: req.body.landmark,
    state: req.body.state,
    city: req.body.city,
    color: req.body.color,
    pincode: req.body.pincode,
    capacity: req.body.capacity || 1,
    patch_type: req.body.patch_type,
    coordinates: req.body.coordinates,
  })

  try {
    const parking = await newParking.save()
    if (parking) {
      res.json({
        success: true,
        message: 'Parking Patch Coordinate added successfully',
        data: {
          id: parking.id,
          patch_id: parking.patch_id,
          color: parking.color,
          color_active: parking.color_active,
          patch_active: parking.patch_active,
          capacity: parking.capacity,
          patch_type: parking.patch_type,
        },
      })
    }
  } catch (e) {
    console.log(e)
  }
})

/*
 * @route  - api/v1/parkings/all
 * @method - GET
 * @desc   - Get all parkings
 * @access - Private
 */
router.get('/all', ensureAuthenticated, async (req, res) => {
  try {
    const parking = await Parking.find()

    if (!parking || parking.length == 0) {
      return res.json({
        success: false,
        message: 'No parking coordinates added',
      })
    }

    const _data = parking.map(item => {
      let color
      if (item.capacity < item.capacity_count) color = '#4f4f4f'
      else color = item.color
      return {
        id: item.id,
        patch_id: item.patch_id,
        color: color,
        patch_active: item.patch_active,
        coordinates: item.coordinates,
        book_count: item.book_count,
        capacity: item.capacity,
        capacity_count: item.capacity_count,
        patch_type: item.patch_type,
      }
    })

    return res.json({
      success: true,
      message: 'Successfully fetched all patches',
      data: _data,
    })
  } catch (e) {
    console.log(e)
  }
})

/*
 * @route  - api/v1/parkings/:id
 * @method - GET
 * @desc   - Get individual parking
 * @access - Private
 */
router.get('/:id', ensureAuthenticated, async (req, res) => {
  try {
    const parking = await Parking.findOne({ patch_id: req.params.id })
    if (!parking) {
      return res.status(400).json({ success: false, message: 'No such patch' })
    }

    return res.json({
      success: true,
      message: 'Patch with id ' + parking.patch_id + ' retrieved successfully',
      patch_type: parking.patch_type,
      capacity: parking.capacity,
      capacity_count: parking.capacity_count,
    })
  } catch (e) {
    console.log(e)
  }
})

/*
 * @route  - api/v1/parkings/:id
 * @method - PATCH
 * @desc   - Disable a patch --> admin only
 * @access - Private
 */
router.patch('/:id', ensureAuthenticated, async (req, res) => {
  // Check is user is admin
  const { user_type } = req
  const error = authUser(user_type, 'admin')
  if (error) {
    return res.status(401).json(error)
  }

  const id = req.params.id
  try {
    const parking = await Parking.findOne({ patch_id: id })
    if (!parking) {
      return res
        .status(400)
        .json({ success: false, message: 'No such patch to disable' })
    }

    // Change patch status
    parking.patch_active = !parking.patch_active

    const saved = await parking.save()

    if (saved) {
      res.json({
        success: true,
        message: 'Parking status is changed',
        parking_status: parking.patch_active,
      })
    }
  } catch (e) {
    console.log(e)
  }
})

/*
 * @route  - api/v1/parkings/booked/all
 * @method - GET
 * @desc   - Get individual parking
 * @access - Private
 */
router.get('/booked/all', ensureAuthenticated, async (req, res) => {
  try {
    const parking = await Parking.find()
    if (parking.length === 0 || !parking) {
      return res
        .status(400)
        .json({ success: false, message: 'No patches booked' })
    }

    const _data = parking
      .filter(item => item.book_count > 0)
      .map(item => {
        if (item.patch_active) {
          return {
            patch_id: item.patch_id,
            book_count: item.book_count,
            name: item.name,
            city: item.city,
            color: item.color,
          }
        }
      })

    return res.json({
      success: true,
      message: 'Booked patches fetched',
      data: _data,
    })
  } catch (e) {
    console.log(e)
  }
})

module.exports = router
