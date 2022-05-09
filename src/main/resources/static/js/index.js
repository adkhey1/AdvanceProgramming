

function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: {lat: 50.1109, lng: 8.6821}
    })


    function addMarker(positionLatLong) {
        const marker = new google.maps.Marker({
            position: positionLatLong,
            map: map


        });

        const detailWindow = new google.maps.InfoWindow({
            content: "Test"
        });

        marker.addListener("click",()=>{
            detailWindow.open(map, marker);
        })

    }


    addMarker({lat: 50.1109, lng: 8.6821});
    addMarker({lat: 52.5200, lng: 13.4050});
    addMarker({lat: 51.5072, lng: 0.1276});
    addMarker({lat: 50.16026, lng: 8.52174});


}
window.initMap = initMap;

var json_data = "test";
loadMapMarkers()

function loadMapMarkers() {



    return $.ajax({
        type: "GET",
        url: "/restaurant/filtered/",
        data: json_data,
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
}

document.getElementById("input").innerHTML = json_data;

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