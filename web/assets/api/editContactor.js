
window.onload = getContractors();
var _data;
function getContractors(){

	fetch("http://192.168.1.21:4444/api/users/all")
	.then((res) => res.json())
	.then((data) => {
		_data = data.data;
		let output = '';
		
		data.data.forEach((contractors) => {
			output += `<tr>
						<td>${contractors.id}</td>
						<td>${contractors.name}</td>
						<td>${contractors.wardNo}</td>
						<td>${contractors.email}</td>
						<td><button class="btn btn-primary" onclick="editForm('${contractors.id}');">Edit</button></td>
						</tr>
					`;
		});
		document.getElementById('listView123').innerHTML = output;
	});
}

//document.getElementById('editForm').addEventListener('click', addEditForm);
function editForm(id){
	var output = '';
	console.log(id);
	console.log(_data);
	_data.forEach(item => {
		if(item.id === id) {
			output += `
				<div class="row mt-4">
					<div class="col-md-12">
						<div class="card">
							<div class="header">
								<h4 class="title">Edit Profile</h4>
							</div>
							<div class="content">
									<div class="row">
										<div class="col-md-5">
											<div class="form-group">
												<label>Contractor ID</label>
												<input type="text" class="form-control" disabled id="id" value=${item.id}>
											</div>
										</div>
										<div class="col-md-7">
											<div class="form-group">
												<label>Name</label>
												<input type="text" class="form-control" id="name" value=${item.name} disabled>
											</div>
										</div>
									</div>

					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label>Ward No</label>
								<input type="text" class="form-control" id="wardNo" value=${item.wardNo} id="editWard">
							</div>
						</div>
						<div class="col-md-5">
							<div class="form-group">
								<label>Mobile No</label>
								<input type="text" class="form-control" id="mob" value=${item.mobileNo} id="editMob">
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-md-8">
							<div class="form-group">
								<label>Email</label>
								<input type="text" class="form-control" id="email" value=${item.email}>
							</div>
						</div>
					</div>

					<button class="btn btn-info btn-fill pull-right" onclick="updateCons('${item.id}')">Update Profile</button>
					<div class="clearfix"></div>
			</div>
		</div>
	</div>
</div>
				`;
		}
	})
	

			document.getElementById("editFormData").innerHTML = output;	
}

function updateCons(id){
	
	userid = document.getElementById('id').value;
	email = document.getElementById('email').value;
	ward = document.getElementById('wardNo').value;
	mob = document.getElementById('mob').value;
	console.log(email, ward, mob, userid);

	fetch('http://192.168.1.21:4444/api/users/update/'+ userid ,{
		method: "PATCH",
		headers: {
			'content-type': 'application/json'
		},
		body:JSON.stringify({
			email:email,
			ward_no:ward,
			mobileNo:mob
		})
	})
	.then((res) => res.json())
	.then((data)=> {
		alert(data.message);	
	});
}



