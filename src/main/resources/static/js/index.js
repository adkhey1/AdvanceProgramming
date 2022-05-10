

function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: {lat: 50.1109, lng: 8.6821}
    })


    function addMarker(latLongPos,businessID) {
        const marker = new google.maps.Marker({
            position: latLongPos,

            map: map


        });

        const detailWindow = new google.maps.InfoWindow({
            content: businessID
        });

        marker.addListener("click",()=>{
            detailWindow.open(map, marker);
            $.ajax({
                'async': false,
                'type': "POST",
                'global': false,
                'url': "/map/viewMarker/",
                //'contentType': "text",
                //'data':businessID.toString(),
                'contentType': "application/json; charset=utf-8",
                'data': JSON.stringify({business_id: businessID}),
                //dataType: "json",
                'success': function (data) {
                    console.log("test")
                    console.log(data)
                    console.log(businessID)
                }
            });
        })

    }
    var myLatlng=0;
    for (let i = 0; i < json_data_LatLongArray.length; i++) {
         myLatlng = new google.maps.LatLng(json_data_LatLongArray[i].latitude, json_data_LatLongArray[i].latitude);
        addMarker(myLatlng,json_data_LatLongArray[i].business_id)
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

    /*
    $.ajax({
        type: 'POST',
        url: "/restaurant/filtered/",
        //force to handle it as text
        dataType: "JSON",
        success: function(data) {

            //data downloaded so we call parseJSON function
            //and pass downloaded data

            //var json = $.parseJSON(data);
            //now json variable contains data in json format
            //let's display a few items
           console.log(data)
            //json_data_LatLongArray=data;
          // console.log(json_data_LatLongArray)
        }
    });
*/

}

loadMapMarkers();

console.log(json_data_LatLongArray)




/*
function prepJsonForMarker(){
    for (let i = 0; i < 20; i++) {
        console.log(json_data_LatLongArray[i].latitude)
        console.log(json_data_LatLongArray[i].longitude)
    }


}
prepJsonForMarker()

*/

//TODO Gracjan Filter
// document.getElementById("map")







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