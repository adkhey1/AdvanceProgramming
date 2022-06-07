// TODO Patryk Filter START

let states = null;
let categories = null;
let attributes = null;

async function loadStates() {
    if (states == null) {
        const url = 'http://localhost:8080/list/states/';
        //sends request to server for states
        try {
            const responsePromise = await fetch(url, {
                method: "POST"
            })
            if (!responsePromise.ok) {
                throw new Error("Error! status: ${response.status}");
            }
            states = await responsePromise.json();
        } catch (err) {
            console.log(err);
        }
    }

    console.log(states);
}

async function loadCategories() {
    if (categories == null) {
        const url = 'http://localhost:8080/list/categories/';
        //sends request to server for categories
        try {
            const responsePromise = await fetch(url, {
                method: "POST"
            })
            if (!responsePromise.ok) {
                throw new Error("Error! status: ${response.status}");
            }
            categories = await responsePromise.json();
        } catch (err) {
            console.log(err);
        }
    }
}

async function loadAttributes() {
    if (attributes == null) {
        const url = 'http://localhost:8080/list/attributes/';
        //sends request to server for attributes
        try {
            const responsePromise = await fetch(url, {
                method: "POST"
            })
            if (!responsePromise.ok) {
                throw new Error("Error! status: ${response.status}");
            }
            attributes = await responsePromise.json();
        } catch (err) {
            console.log(err);
        }
    }
}




//TODO END


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
                'async': "true", 'type': "POST",
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
            setTimeout(function () {

                sideView();

            }, 25000);


        })

    }

    console.log(json_data_LatLongArray)

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

loadStates()
loadAttributes()
loadCategories()

window.initMap = initMap;


//get Markers
var json_data_LatLongArray;


//ajax call um marker zu laden
function loadMapMarkers() {


    $.ajax({
        'async': false,
        'type': "POST",
        'global': false,
        'url': "/100restaurants/",
        'success': function (data) {
            json_data_LatLongArray = data;
        }
    });


}

loadMapMarkers();

console.log(json_data_LatLongArray)


// Array mit 3 Placeholdern füllen damit es keine errors gibt evtl löschen
/*
var placeholder = {
    "spring": 0,
    "sommer": 0,
    "fall": 0,
    "winter": 0,
    "placholder": true
};

var json_return_markerArrTemp = []
json_return_markerArrTemp.unshift(placeholder);
json_return_markerArrTemp.unshift(placeholder);
json_return_markerArrTemp.unshift(placeholder);


 */

var json_return_markerArrTemp = [] //


console.log(json_return_markerArrTemp)

//konzept für 3 Fenster
function sideView() {

    //fill the Array
    if (json_return_markerArrTemp.length < 3) {
        json_return_markerArrTemp.unshift(json_return_marker)
    } else {
        json_return_markerArrTemp.pop()
        json_return_markerArrTemp.unshift(json_return_marker)
    }
    console.log(json_return_markerArrTemp)

    //Evtl löschen
    comparisonChart(json_return_markerArrTemp)

    console.log(json_return_markerArrTemp[0])
    //Window1
    document.getElementById('sideWindow1inner').innerText = JSON.stringify(json_return_markerArrTemp[0]);

    //transform to hashmap and get keys and values
    var hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countCategorie))
    var values = Array.from(hsMap.values());
    var keys = Array.from(hsMap.keys());
    console.log(values) //33,33,2
    console.log(keys) //chinese etc.
    //console.log(chart);

    exampleChart1('chartWindow1', values, keys);

    //Window2
    if (json_return_markerArrTemp[1] != null) {
        document.getElementById('sideWindow2inner').innerText = JSON.stringify(json_return_markerArrTemp[1]);
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countCategorie))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());

        exampleChart2('chartWindow2', values, keys);
    }

    //window3
    if (json_return_markerArrTemp[2] != null) {
        document.getElementById('sideWindow3inner').innerText = JSON.stringify(json_return_markerArrTemp[2]);
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countCategorie))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());

        exampleChart3('chartWindow3', values, keys);
    }
    console.log(json_return_markerArrTemp)

}

let myChart1 = null;
let myChart2 = null;
let myChart3 = null;


function exampleChart1(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                options: {
                    events: ['click']
                },
                backgroundColor: [getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor()],
                datalabels: {
                    color: 'blue',
                    anchor: 'end',
                    align: 'top'
                }
            }]
        },
        plugins: [ChartDataLabels]


    }

    if (myChart1 != null) {
        myChart1.destroy();
    }
    const ctx = document.getElementById(div)
    myChart1 = new Chart(
        ctx,
        config,
    );

    function clickHandler(click){
        const points = myChart1.getElementsAtEventForMode(click,'nearest',{
            intersect: true},true);
        console.log(points)
        console.log(points[0].index)
        console.log(keys[points[0].index])

    }
    ctx.onclick = clickHandler;
}

function exampleChart2(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                backgroundColor: [getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor()],
                datalabels: {
                    color: 'blue',
                    anchor: 'end',
                    align: 'top'
                }
            }]
        },
        plugins: [ChartDataLabels]


    }

    if (myChart2 != null) {
        myChart2.destroy();
    }
    myChart2 = new Chart(
        document.getElementById(div),
        config
    );


}

function exampleChart3(div, values, keys) {

    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                backgroundColor: [getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor()],
                datalabels: {
                    color: 'blue',
                    anchor: 'end',
                    align: 'top'
                }
            }]
        },
        plugins: [ChartDataLabels]


    }


    if (myChart3 != null) {
        myChart3.destroy();
    }
    myChart3 = new Chart(
        document.getElementById(div),
        config
    );

}


let compChart = null;


/*
function comparisonChart(dataArr) {

    let run1 = false
    let run2 = false
    let run3 = false

    if (dataArr[0].placeholder) {
        run1 = true
    }
    if (dataArr[1].placeholder) {
        run2 = true
    }
    if (dataArr[0].placeholder) {
        run3 = true
    }

    if (compChart != null) {
        compChart.destroy();
    }


    const labels = [
        'spring',
        'summer',
        'fall',
        'winter'

    ];

    const data = {
        labels: labels,


        datasets: [

            {
                label: 'Dataset 1',
                backgroundColor: getRandomColor(),
                borderColor: getRandomColor(),
                //data: [20,30,60,10],
                data: [dataArr[0].spring, dataArr[0].summer, dataArr[0].fall, dataArr[0].winter],
                hidden: run1
            },
            {
                label: 'Dataset 2',
                backgroundColor: getRandomColor(),
                borderColor: getRandomColor(),
                data: [dataArr[1].spring, dataArr[1].summer, dataArr[1].fall, dataArr[1].winter],
                hidden: run2
            },
            {
                label: 'Dataset 3',
                backgroundColor: getRandomColor(),
                borderColor: getRandomColor(),
                data: [dataArr[2].spring, dataArr[2].summer, dataArr[2].fall, dataArr[2].winter],
                hidden: run3
            }],
        plugins: [ChartDataLabels]
    };
    console.log(run1)
    console.log(run2)
    console.log(run3)

    const config = {
        type: 'line',
        data: data,
        options: {}
    };


    compChart = new Chart(
        document.getElementById('lineComp1'),
        config
    );

}

 */

function comparisonChart(dataArr) {


    if (compChart != null) {
        compChart.destroy();
    }


    const labels = [
        'spring',
        'summer',
        'fall',
        'winter'

    ];

    const data = {
        labels: labels,


        datasets: [

            {
                label: 'Dataset 1',
                backgroundColor: getRandomColor(),
                borderColor: getRandomColor(),
                //data: [20,30,60,10],
                data: [dataArr[0].spring, dataArr[0].summer, dataArr[0].fall, dataArr[0].winter] //0 durch die jeweiliges side view window nummer erstzen

            },
          ],
        plugins: [ChartDataLabels]
    };


    const config = {
        type: 'line',
        data: data,
        options: {}
    };

    compChart = new Chart(
        document.getElementById('lineComp1'),
        config
    );

}

function loadClickedCategory(){
    // TODO AJAX CALL und neues diagramm erstellen das beim click geladen wird
}

function comparisonChartWithCategory(dataArr,label,div) {




    if (compChart != null) {
        compChart.destroy();
    }
    const labels = [
        'spring',
        'summer',
        'fall',
        'winter'
    ];

    const data = {
        labels: labels,


        datasets: [

            {
                label: label,
                backgroundColor: getRandomColor(),
                borderColor: getRandomColor(),
                //data: [20,30,60,10],
                data: [dataArr[0].spring, dataArr[0].summer, dataArr[0].fall, dataArr[0].winter]

            },
        ],
        plugins: [ChartDataLabels]
    };
    const config = {
        type: 'line',
        data: data,
        options: {}
    };
    compChart = new Chart(
        document.getElementById('lineComp1'),
        config
    );

}


function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
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



