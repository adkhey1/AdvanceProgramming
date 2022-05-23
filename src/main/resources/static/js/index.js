var json_return_marker;




// map
function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: {lat: 40.560109, lng: -100.573589}
    })


    function addMarker(latLongPos, businessID) {
        const marker = new google.maps.Marker({
            position: latLongPos,

            map: map


        });

        const detailWindow = new google.maps.InfoWindow({
            content: businessID
        });

        marker.addListener("click", () => {
            detailWindow.open(map, marker);
            $.ajax({
                'async': "true",
                'type': "POST",
                'global': false,
                'url': "/map/viewMarker/",
                //'contentType': "text",
                //'data':businessID.toString(),
                'contentType': "application/json; charset=utf-8",
                'data': JSON.stringify({business_id: businessID}),
                //dataType: "json",
                'success': function (data) {
                    //console.log("test")
                    //console.log(data)
                    json_return_marker = data
                    console.log(businessID)
                }
            });
            setTimeout(function (){

                sideView();

            }, 5000);

        })

    }

    //add all marker
    var myLatlng;
    for (let i = 0; i < json_data_LatLongArray.length; i++) {
        myLatlng = new google.maps.LatLng(json_data_LatLongArray[i].latitude, json_data_LatLongArray[i].longitude);
        addMarker(myLatlng, json_data_LatLongArray[i].business_id)

    }

    //var myLatlng = new google.maps.LatLng(json_data_LatLongArray[1].latitude, json_data_LatLongArray[1].latitude);
    //addMarker(myLatlng);
    //addMarker({lat: 52.5200, lng: 13.4050});
    //addMarker({lat: 51.5072, lng: 0.1276});
    //addMarker({lat: 50.16026, lng: 8.52174});


}

window.initMap = initMap;


//get Markers
var json_data_LatLongArray;


//ajax call um marker zu laden
function loadMapMarkers() {


    $.ajax({
        'async': false,
        'type': "POST",
        'global': false,
        'url': "/restaurant/filtered/",
        'success': function (data) {
            json_data_LatLongArray = data;
        }
    });


}

loadMapMarkers();






var json_return_markerArrTemp = []

//konzept für 3 Fenster
function sideView() {

    //fill the Array
    if(json_return_markerArrTemp.length<3) {
        json_return_markerArrTemp.unshift(json_return_marker)
    }else {
        json_return_markerArrTemp.pop()
        json_return_markerArrTemp.unshift(json_return_marker)
    }

    console.log(json_return_markerArrTemp[0])
    //Window1
    document.getElementById('sideWindow1inner').innerText = JSON.stringify(json_return_markerArrTemp[0]);

    //transform to hashmap and get keys and values
    var hsMap = new Map( Object.entries(json_return_markerArrTemp[0].countCategorie) )
    var values = Array.from(hsMap.values());
    var keys = Array.from(hsMap.keys());
    console.log(values) //33,33,2
    console.log(keys) //chinese etc.
    //console.log(chart);

    exampleChart1('chartWindow1',values,keys);

    //Window2
    if(json_return_markerArrTemp[1]!=null){
        document.getElementById('sideWindow2inner').innerText = JSON.stringify(json_return_markerArrTemp[1]);
         hsMap = new Map( Object.entries(json_return_markerArrTemp[1].countCategorie) )
         values = Array.from(hsMap.values());
         keys = Array.from(hsMap.keys());

        exampleChart2('chartWindow2',values,keys);
    }

    //window3
    if(json_return_markerArrTemp[2]!=null){
        document.getElementById('sideWindow3inner').innerText = JSON.stringify(json_return_markerArrTemp[2]);
        hsMap = new Map( Object.entries(json_return_markerArrTemp[2].countCategorie) )
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());

        exampleChart3('chartWindow3',values,keys);
    }
    console.log(json_return_markerArrTemp)



}

let myChart1 = null;
let myChart2 = null;
let myChart3 = null;


function exampleChart1(div,values,keys){


    var config={
        type:"bar",
        data:{

            labels:keys,
            datasets:[{
                label: 'count',
                data:values,
                backgroundColor: [getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor()],
                datalabels:{
                    color: 'blue',
                    anchor: 'end',
                    align: 'top'
                }
            }]},
    plugins:[ChartDataLabels]


    }

    if(myChart1!=null){
        myChart1.destroy();
    }
    myChart1=new Chart(
        document.getElementById(div),
        config,
    );




}

function exampleChart2(div,values,keys){


    var config={
        type:"bar",
        data:{

            labels:keys,
            datasets:[{
                label: 'count',
                data:values,
                backgroundColor: [getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor()],
                datalabels:{
                    color: 'blue',
                    anchor: 'end',
                    align: 'top'
                }
            }]},
        plugins:[ChartDataLabels]


    }

    if(myChart2!=null){
        myChart2.destroy();
    }
    myChart2=new Chart(
        document.getElementById(div),
        config
    );




}

function exampleChart3(div,values,keys) {

    var config={
        type:"bar",
        data:{

            labels:keys,
            datasets:[{
                label: 'count',
                data:values,
                backgroundColor: [getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor(),getRandomColor()],
                datalabels:{
                    color: 'blue',
                    anchor: 'end',
                    align: 'top'
                }
            }]},
        plugins:[ChartDataLabels]


    }


    if (myChart3 != null) {
        myChart3.destroy();
    }
    myChart3 = new Chart(
        document.getElementById(div),
        config
    );

}


comparisonChart()


function comparisonChart(){



    const labels = [
        'Long',
        'Lat',

    ];

    const data = {
        labels: labels,
        datasets: [{
            label: '1',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [20,30],
        },
            {
                label: '2',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: [0,50],
            },
            {
                label: '3',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: [30,50],
            }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {}
    };

    const compChart = new Chart(
        document.getElementById('testChart'),
        config
    );

}





/*
barChartcountCategorie()

function barChartcountCategorie(){

    var config={
        type:"bar",
        data:{
            labels:["test1","test2","test3"],
            datasets:[{
                label: "testing",
                data:[10,20,30],
                backgroundColor: [getRandomColor(),getRandomColor(),getRandomColor()]



            }]},




    }

    const barChart=new Chart(
        document.getElementById("testBar"),
        config);
}

 */

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

///////////////////////////////////////////////////////

//TODO Gracjan Filter
// document.getElementById("map")

var $items = $('#firstName, #lastName,#phoneNumber,#address ')
var obj = {}
$items.each(function () {
    obj[this.id] = $(this).val();
})

var json = JSON.stringify(obj);


/*
// Initialize and add the map
function initMap() {

    const uluru = { lat: 34.4266787, lng: -119.7111968 };
    // The map, centered at Uluru
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: uluru,
    });
    //marker
    const marker = new google.maps.Marker({
        position: uluru,
        map: map,
    });
    const detailWindow = new google.maps.InfoWindow({
        content: "Test"
    });

    marker.addListener("click",()=>{
        detailWindow.open(map, marker);
    })
}



window.initMap = initMap;

*/