const express = require('express')
const router = express.Router()

// Proctection of routes
const ensureAuthenticated = require('../../utils/ensureAuthenticated')
// Authorize user
const authUser = require('../../utils/authorizeUser')
// Load models
const User = require('../../models/User')

/*
 * @route  - /api/v1/admin/:id
 * @method - PATCH
 * @desc   - Disable a contractor
 * @access - Private
 */
router.patch('/:id', ensureAuthenticated, async (req, res) => {
  // Check is user is admin
  const { user_type } = req
  const error = authUser(user_type, 'admin')
  if (error) {
    return res.status(401).json(error)
  }
  try {
    const user = await User.findById(req.params.id)

    if (!user) {
      return res.status(400).json({ success: false, message: 'No such user' })
    }

    if (user.user_type !== 'contractor') {
      return res
        .status(401)
        .json({ success: false, message: 'No authorized to update this user' })
    }

    user.active_status = !user.active_status

    const saved = await user.save()

    if (saved) {
      res.json({
        success: true,
        message: 'User status is changed',
        user_id: user.id,
        user_status: user.active_status,
      })
    } else {
      res.json({ success: false, message: 'something went wrong' })
    }
  } catch (err) {
    console.log(err)
  }
})

/*
 * @route  - /api/v1/admin/update/:id
 * @method - PATCH
 * @desc   - Update contractor details
 * @access - Private
 */
router.patch('/update/:id', ensureAuthenticated, async (req, res) => {
  // Check is user is admin
  const { user_type } = req
  const error = authUser(user_type, 'admin')
  if (error) {
    return res.status(401).json(error)
  }
  try {
    const user = await User.findById(req.params.id)

    if (!user) {
      return res.json({ success: false, message: 'No such user' })
    }

    if (user.user_type !== 'contractor') {
      return res.json({
        success: false,
        message: 'This user is not contractor',
      })
    }

    const { email, wardNo, mobileNo } = req.body

    if (email) user.email = email
    if (wardNo) user.wardNo = wardNo
    if (mobileNo) user.mobileNo = mobileNo

    const saved = await user.save()

    if (saved) {
      res.json({ success: true, message: 'User data updated', data: saved })
    } else {
      res.json({ success: false, message: 'something went wrong' })
    }
  } catch (err) {
    console.log(err)
  }
})

/*
 * @route  - /api/v1/admin/all
 * @method - GET
 * @desc   - Fetch all contractor
 * @access - Private
 */
router.get('/all', ensureAuthenticated, async (req, res) => {
  // Check is user is admin
  const { user_type } = req
  const error = authUser(user_type, 'admin')
  if (error) {
    return res.status(401).json(error)
  }
  try {
    const contractors = await User.find({ user_type: 'contractor' })

    if (contractors.length <= 0) {
      return res.json({ success: false, message: 'No contractors' })
    }

    const _data = contractors.map(item => {
      return {
        id: item._id,
        name: item.name,
        wardNo: item.wardNo,
        email: item.email,
        mobileNo: item.mobileNo,
        active_status: item.active_status,
      }
    })

    res.json({
      success: true,
      message: 'Successfully fetched all contractors',
      data: _data,
    })
  } catch (err) {
    console.log(err)
  }
})

module.exports = router
