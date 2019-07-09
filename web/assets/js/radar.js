
var ctxR = document.getElementById("radarChart").getContext('2d');
var myRadarChart = new Chart(ctxR, {
  type: 'radar',
  data: {
    labels: ["Free", "Paid", "Reserved"],   
    datasets: [
      {
        label: "No. of Cars",
        data: [65, 59, 90],
        backgroundColor:'rgba(105, 0, 132, .2)',
        borderColor:'rgba(200, 99, 132, .7)',
        borderWidth: 2
      },
      {
        label: "No. of Bikes",
        data: [28, 48, 40],
        backgroundColor:'rgba(0, 250, 220, .2)',
        borderColor:'rgba(0, 213, 132, .7)',
        borderWidth: 2
      }
    ]
  },
  options: {
    responsive: true,
    
  }
});