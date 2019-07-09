const jwt = require('jsonwebtoken')
const keys = require('../config/keys')

module.exports = async (req, res, next) => {
  const bearerHeader = req.headers['authorization']
  if (bearerHeader) {
    const bearer = bearerHeader.split(' ')
    const bearerToken = bearer[1]
    try {
      // Verify the token against the signature
      const { id, type } = await jwt.verify(bearerToken, keys.JWT_SECRET)
      // All ok. Move to next middleware
      req.user_id = id
      req.user_type = type
      next()
    } catch (e) {
      res.status(403).json({
        success: false,
        message: 'Unauthorized to view this.',
      })
    }
  } else {
    res.status(400).json({
      success: false,
      message: 'Unauthorized to view this.',
    })
  }
}
