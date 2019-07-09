var ctxR = document.getElementById("myChart").getContext('2d');
var myRadarChart = new Chart(ctxR, {
    type: 'bubble',
    data: {
    labels: ["Free", "Paid", "Reserved"],   
    datasets: [
        {
            label: "My First dataset",
            data:[
            {
                x: 20,
                y: 45,
                r: 15
            },
            {
                x: 50,
                y: 85,
                r: 30
            },
            {
                x: 90,
                y: 25,
                r: 40
            }],
            backgroundColor:'rgba(105, 0, 132, .2)',
            borderColor: 'rgba(200, 99, 132, .7)',
            pointStyle: 'circle',
            radius: 5
        },
        {
            label: "My Second dataset",
            data:[
            {
                x: 80,
                y: 85,
                r: 20
            },
            {
                x: 10,
                y: 55,
                r: 10
            }],
            backgroundColor:'rgba(0, 250, 220, .2)',
            borderColor:'rgba(0, 213, 132, .7)',
            pointStyle: 'circle',
            radius: 5
        },
        {
            label: "My Third dataset",
            data:[
            {
                x: 30,
                y: 55,
                r: 40
            },
            {
                x: 60,
                y: 75,
                r: 50
            }],
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
