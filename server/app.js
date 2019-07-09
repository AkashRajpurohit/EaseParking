const express = require('express')
const cors = require('cors')
const mongoose = require('mongoose')
const path = require('path')
const morgan = require('morgan')

// Load keys
const keys = require('./config/keys')

const app = express()

// Get Routes
const users = require('./routes/api/users')
const admin = require('./routes/api/admin')
const parkings = require('./routes/api/parkings')
const bookings = require('./routes/api/bookings')

// BodyParser Middleware
app.use(express.urlencoded({ extended: false }))
app.use(express.json())

// CORS Middleware
app.use(cors())

// Morgan
app.use(morgan('dev'))

// Static folder
app.use(express.static(path.join(__dirname, 'public')))

// Database Conne"test": "echo \"Error: no test specified\" && exit 1"ction
mongoose
  .connect(keys.mongoURI, { useNewUrlParser: true })
  .then(() => console.log('MongoDB connected...'))
  .catch(err => console.log('ERR in MongoDB' + err))

// Use Routes
app.use('/api/v1/users', users)
app.use('/api/v1/admin', admin)
app.use('/api/v1/parkings', parkings)
app.use('/api/v1/bookings', bookings)

const port = process.env.PORT || 7418

app.listen(port, () => console.log(`Server started at port ${port}...`))
