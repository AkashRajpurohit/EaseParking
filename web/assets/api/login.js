document.getElementById('loginButton').addEventListener('click', validateLogin);

function validateLogin(e){

	e.preventDefault();

	username = document.getElementById('username').value;
	password = document.getElementById('password').value;

	fetch('http://192.168.1.21:4444/api/users/login', {
		method:"POST",
		headers:{
			'content-type':'application/json'
		},
		body:JSON.stringify({
			email:username,
			password:password
		})
	})
	.then((res)=>res.json())
	.then((data) => {
		var output="";

		if(data.data){
			if(data.data.user_type === 'admin'){
				window.location = 'user.html';
			}
		}else{
			output += `<h2>${data.message}</h2>`;
			document.getElementById('loginType').innerHTML = output;
		}
	})
}