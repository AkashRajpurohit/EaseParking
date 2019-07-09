let _booking_data;
let _parking_data;

window.load = getBookings();

function getBookings() {
	fetch('http://192.168.1.21:4444/api/bookings/all')
		.then(res => res.json())
		.then(data => {
			_booking_data = data.data;
			getParkings();
		});
}

function getParkings() {
	fetch('http://192.168.1.21:4444/api/parkings/all')
		.then(res => res.json())
		.then(data => {
			_parking_data = data.data;
			startProcessing();
		});
}

function startProcessing() {
	// console.log(_booking_data);
	// console.log(_parking_data);

	let _data = [];

	_parking_data.forEach(parking => {
		_booking_data.forEach(booking => {
			if(booking.patch_id === parking.patch_id) {
				_data.push({
					book_count: parking.book_count,
					start_time: booking.start_time.split(':')[0],
					color: parking.color,
					start_date: booking.start_date
				})
			}
		});
	});

	let free = _data.filter(item => item.color === '#00ff00').length;
	let reserved = _data.filter(item => item.color === '#ffff00').length;
	let paid = _data.filter(item => item.color === '#ff0000').length;

	let date1_free = _data.filter(item => (item.start_date == '2018-09-29') && (item.color === '#00ff00'));
	let date1_reserved = _data.filter(item => (item.start_date == '2018-09-29') && (item.color === '#ffff00'));
	let date1_paid = _data.filter(item => (item.start_date == '2018-09-29') && (item.color === '#ff0000'));
	let date2_free = _data.filter(item => (item.start_date == '2018-09-30') && (item.color === '#00ff00'));
	let date2_reserved = _data.filter(item => (item.start_date == '2018-09-30') && (item.color === '#ffff00'));
	let date2_paid = _data.filter(item => (item.start_date == '2018-09-30') && (item.color === '#ff0000'));
	let data3_free = _data.filter(item => (item.start_date == '20a3-10-01') && (item.color === '#00ff00'));
	let date3_reserved = _data.filter(item => (item.start_date == '2018-10-01') && (item.color === '#ffff00'));
	let date3_paid = _data.filter(item => (item.start_date == '2013-10-01') && (item.color === '#ff0000'));
	let data4_free = _data.filter(item => (item.start_date == '20a3-10-02') && (item.color === '#00ff00'));
	let date4_reserved = _data.filter(item => (item.start_date == '2018-10-02') && (item.color === '#ffff00'));
	let date4_paid = _data.filter(item => (item.start_date == '2013-10-02') && (item.color === '#ff0000'));
	let data5_free = _data.filter(item => (item.start_date == '20a3-10-03') && (item.color === '#00ff00'));
	let date5_reserved = _data.filter(item => (item.start_date == '2018-10-03') && (item.color === '#ffff00'));
	let date5_paid = _data.filter(item => (item.start_date == '2013-10-03') && (item.color === '#ff0000'));
	let data6_free = _data.filter(item => (item.start_date == '20a3-10-04') && (item.color === '#00ff00'));
	let date6_reserved = _data.filter(item => (item.start_date == '2018-10-04') && (item.color === '#ffff00'));
	let date6_paid = _data.filter(item => (item.start_date == '2013-10-04') && (item.color === '#ff0000'));
	let data7_free = _data.filter(item => (item.start_date == '20a3-10-05') && (item.color === '#00ff00'));
	let date7_reserved = _data.filter(item => (item.start_date == '2018-10-05') && (item.color === '#ffff00'));
	let date7_paid = _data.filter(item => (item.start_date == '2013-10-05') && (item.color === '#ff0000'));

	let output = JSON.stringify(_data);
	// output+=`
	// 	<p>${JSON.stringify(date1_free)}</p>
	// 	<p>${JSON.stringify(date1_reserved)}</p>
	// 	<p>${JSON.stringify(date1_paid)}</p>

	// 	<p>${JSON.stringify(date2_free)}</p>
	// 	<p>${JSON.stringify(date2_reserved)}</p>
	// 	<p>${JSON.stringify(date2_paid)}</p>

	// 	<p>${JSON.stringify(date3_free)}</p>
	// 	<p>${JSON.stringify(date3_reserved)}</p>
	// 	<p>${JSON.stringify(date3_paid)}</p>

	// 	<p>${JSON.stringify(date4_free)}</p>
	// 	<p>${JSON.stringify(date4_reserved)}</p>
	// 	<p>${JSON.stringify(date4_paid)}</p>

	// 	<p>${JSON.stringify(date5_free)}</p>
	// 	<p>${JSON.stringify(date5_reserved)}</p>
	// 	<p>${JSON.stringify(date5_paid)}</p>

	// 	<p>${JSON.stringify(date6_free)}</p>
	// 	<p>${JSON.stringify(date6_reserved)}</p>
	// 	<p>${JSON.stringify(date6_paid)}</p>

	// 	<p>${JSON.stringify(date7_free)}</p>
	// 	<p>${JSON.stringify(date7_reserved)}</p>
	// 	<p>${JSON.stringify(date7_paid)}</p>
	// 		`;

	document.getElementById('bubbleData').innerHTML = output;
	console.log(data1_free, date1_paid);
	console.log(free);
	plotPieChart(free, reserved, paid);
	plotBubbleChart(date1_free, date1_reserved, date1_paid);
}

function plotPieChart(free, reserved, paid){
	var total = free+reserved+paid;
	var free = Math.floor((free/total)*100);
	var reserved = Math.floor((reserved/total)*100);
	var paid = Math.floor((paid/total)*100);
	console.log(free, reserved, paid);
	var ctx = document.getElementById('DonutChart').getContext('2d');

	var myPieChart = new Chart(ctx,{
		type: 'doughnut',
		data: {
			labels: ["Free", "Reserved", "Paid"],   
			datasets: [
			  {
				label: "Free",
				data: [free, reserved, paid],
				backgroundColor:[
					'rgba(0, 255, 0, 0.3)',
					'rgba(255,255,0,0.45)',
					'rgba(255,0,0,0.45)'
				],
				borderColor:'rgba(0, 0, 1, 0.2)',
				borderWidth: 2
			  }
			]
		}
	});
}

function plotBubbleChart(free, reserved, paid) {

	let free = free;
	let reserved = reserved;
	let paid = paid;

	let freeData = [];
	let resData = [];
	let paidData = [];

	for(let i = 0; i<free.length; i++){
		freeData.push({
			x:freeData.start_time,
			y:freeData.book_count,
			r:20
		});
	}
	for(let i = 0; i<reserved.length; i++){
		resData.push({
			x:resData.start_time,
			y:resData.book_count,
			r:30
		});}
		for(let i = 0; i<paid.length; i++){
		paidData.push({
			x:paidData.start_time,
			y:paidData.book_count,
			r:45
		});
	}

	var ctxR = document.getElementById("bubbleChart").getContext('2d');
	var myChart = new Chart(ctxR, {
    type: 'bubble',
    data: {
    	labels: ["Free", "Paid", "Reserved"],   
   		 datasets: [
        	{
            label: "Free",
            data: freeData,
            backgroundColor:'rgba(105, 0, 132, .2)',
            borderColor: 'rgba(200, 99, 132, .7)',
            pointStyle: 'circle',
            radius: 5
        },
        {
            label: "Reserved",
            data:resData,
            backgroundColor:'rgba(0, 250, 220, .2)',
            borderColor:'rgba(0, 213, 132, .7)',
            pointStyle: 'circle',
            radius: 5
        },
        {
            label: "Paid",
            data:paidData,
            backgroundColor:'rgba(255, 0, 0, .5)',
            borderColor:'rgba(255, 0, 0, .9)',
            pointStyle: 'circle',
            radius: 5
        }

    ]
    },
    options: {
        responsive: true,
        scales: {
        yAxes: [{
            ticks: {
                suggestedMin: 0,
                suggestedMax: 100
            }
        }],
        xAxes: [{
            ticks: {
                suggestedMin: 0,
                suggestedMax: 100
            }
        }]
    }
    }
});


}