
window.onload = getAllCons();

function getAllCons(){

    fetch("http://192.168.1.21:4444/api/users/all")
        .then((res) => res.json())
        .then((data) => {
            
            let output = ''
            data.data.forEach((contractors) => {
                output += `<tr>
                            <td>${contractors.id}</td>
                            <td>${contractors.name}</td>
                            <td>${contractors.wardNo}</td>
                            <td>${contractors.email}</td>
                        `;
                if(contractors.active_status){
                    output += `
                                <td>
                                    <input type="button" value="Disable" id="activityButton" class="btn btn-success" onclick="changeStatus('${contractors.id}');">
                                </td>
                                </tr>
                                        `;
                }else{
                    output += `
                                <td>
                                    <input type="button" value="Enable" id="activityButton" class="btn btn-danger" onclick="changeStatus('${contractors.id}');">
                                </td>
                                      </tr>  `;
                }
            });
            document.getElementById('listView').innerHTML = output;
        })
}

const changeStatus = (id) => {
    console.log(id, typeof id);
    fetch('http://192.168.1.21:4444/api/users/'+ id, {
        method: 'PATCH'
    }).then(res => res.json())
    .then(data => getAllCons());
}

// function getAllCons(){

//     fetch("http://localhost:4444/api/users/all")
//         .then((res) => res.json())
//         .then((data) => console.log(data));
// }  