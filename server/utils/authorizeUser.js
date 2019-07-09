module.exports = (typeGot, typeExpected) => {
  if (typeGot !== typeExpected) {
    return { success: false, message: 'You are unauthorized to view this' }
  } else {
    return false
  }
}
