document.getElementById('addData').addEventListener('submit', (e) => {
    e.preventDefault();
    name = document.getElementById('name').value;
    ward_no = document.getElementById('wardno').value;
    gender = document.getElementById('gender').value;
    dob = document.getElementById('dob').value;
    email = document.getElementById('email').value;
    mobileNo = document.getElementById('phone').value;
    password = document.getElementById('password').value;
    address = document.getElementById('address').value;
    city = document.getElementById('city').value;
    state = document.getElementById('state').value;

    fetch('http://192.168.1.21:4444/api/users/register', {
         method:'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body:JSON.stringify({
             name:name,
             ward_no:ward_no,
             gender:gender,
             dob:dob,
             email:email,
             mobileNo:mobileNo,
             password:password,
             address:address,
             city:city,
             state:state
         })
     })
     .then((res) => res.json())
     .then((data) => {
         // TODO : Validation messages
        console.log(data)
     })
});