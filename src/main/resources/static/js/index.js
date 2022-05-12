var json_return_marker;

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

            }, 4000);

        })

    }

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



console.log(json_return_marker)


var json_return_markerArrTemp = []

function sideView() {

    //fill the Array
    if(json_return_markerArrTemp.length<3) {
        json_return_markerArrTemp.unshift(json_return_marker)
    }else {
        json_return_markerArrTemp.pop()
        json_return_markerArrTemp.unshift(json_return_marker)
    }


    //Window1
    document.getElementById('sideWindow1inner').innerText = JSON.stringify(json_return_markerArrTemp[0]);
    exampleChart1('chartWindow1');
    //console.log(chart);

    //Window2
    if(json_return_markerArrTemp[1]!=null){
        document.getElementById('sideWindow2inner').innerText = JSON.stringify(json_return_markerArrTemp[1]);
        exampleChart2('chartWindow2');
    }

    //window3
    if(json_return_markerArrTemp[2]!=null){
        document.getElementById('sideWindow3inner').innerText = JSON.stringify(json_return_markerArrTemp[2]);
        exampleChart3('chartWindow3');
    }
    console.log(json_return_markerArrTemp)



}

let myChart1 = null;
let myChart2 = null;
let myChart3 = null;

function exampleChart1(div){


    const labels = [
        'Janua',
        'Februa',
        'March',
        'April',
        'May',
        'June',
    ];

    const data = {
        labels: labels,
        datasets: [{
            label: 'My First dataset',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [0, 10, 5, 2, 20, 30, 45],
        }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {}
    };

    if(myChart1!=null){
        myChart1.destroy();
    }
    myChart1=new Chart(
        document.getElementById(div),
        config
    );




}

function exampleChart2(div){


    const labels = [
        'Janua',
        'Februa',
        'March',
        'April',
        'May',
        'June',
    ];

    const data = {
        labels: labels,
        datasets: [{
            label: 'My First dataset',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [0, 10, 5, 2, 20, 30, 45],
        }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {}
    };

    if(myChart2!=null){
        myChart2.destroy();
    }
    myChart2=new Chart(
        document.getElementById(div),
        config
    );




}

function exampleChart3(div){


    const labels = [
        'Janua',
        'Februa',
        'March',
        'April',
        'May',
        'June',
    ];

    const data = {
        labels: labels,
        datasets: [{
            label: 'My First dataset',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [0, 10, 5, 2, 20, 30, 45],
        }]
    };

    const config = {
        type: 'line',
        data: data,
        options: {}
    };

    if(myChart3!=null){
        myChart3.destroy();
    }
    myChart3=new Chart(
        document.getElementById(div),
        config
    );




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