

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


    function addWindow() {

    }

    addMarker({lat: 50.1109, lng: 8.6821});
    addMarker({lat: 52.5200, lng: 13.4050});
    addMarker({lat: 51.5072, lng: 0.1276});


}
window.initMap = initMap;

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