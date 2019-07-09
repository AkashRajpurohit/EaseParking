function initMap(){

    var options={
        zoom: 21,
        center: {lat:19.3839 , lng:72.8287}
    };

    var map = new google.maps.Map(document.getElementById('map'),options);

    var marker = new google.maps.Marker({
        position:{lat: 19.3839, lng:72.8287},
        map: map,
    });

    fetch('http://192.168.1.21:4444/api/parkings/all')
    .then((res) => res.json())
    .then((data) => {
       // console.log(data.data);
        console.log(data.data.length);
        for(let j = 0; j<data.data.length; j++){
            var coords = [];
            console.log(data.data[j]);
            for(let i= 0; i<4; i++){
                coords.push({lat:data.data[j].coordinates[i].lat, lng:data.data[j].coordinates[i].lng})
            }
            console.log(coords);
            var patchMarker = new google.maps.Polygon({
                paths: [coords],
                strokeColor: data.data[j].color_active ? data.data[j].color : '#000000',
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: data.data[j].color_active ? "#ffffff" : '#c0c7d1',
                fillOpacity: 0.35
            });
            patchMarker.setMap(map);
          
            addMarker({
                coord:{lat:data.data[j].coordinates[0].lat, lng:data.data[j].coordinates[0].lng},
                map: map,
                content: `<p>ContractorID:  ${data.data[j].user_id}</p>
                           <button class="btn btn-success" onclick="changeStatus('${data.data[j].patch_id}')">Disable</button>`
            });
        }  
    });

    function addMarker(props){
        var marker = new google.maps.Marker({
            position:props.coord,
            map: map,
        });

        var infoWindow = new google.maps.InfoWindow({
            content:props.content
        });

        marker.addListener('click', function(){
            infoWindow.open(map, marker);
        });

    }
}

function changeStatus(patchid){

    fetch('http://192.168.1.21:4444/api/parkings/'+patchid ,{
        method:"PATCH"
    })
        .then(res => res.json())
        .then(data => alert(data.message));

}