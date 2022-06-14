// TODO Patryk Filter START

let states = null;
var categories = null;
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
    fillDropdown()
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

loadStates()
loadAttributes()
loadCategories()

// TODO Code mit Variabel übergabe Problem
function display() {
    console.log(categories);
    console.log(states);
}


// TODO Code mit Variabel übergabe Problem END


//TODO END




var json_return_marker;

//let map = google.maps.Map;
//let marker = google.maps.Marker = [];

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

            }, 10000);


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

    function deleteMarkers(){
        console.log("deletet")
        json_data_LatLongArray=[]
        console.log(json_data_LatLongArray)
        pushMarker()
    }


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


    console.log(json_return_markerArrTemp[0])
    //Window1
    document.getElementById('sideWindow1inner').innerText = JSON.stringify(json_return_markerArrTemp[0]);

    //transform to hashmap and get keys and values
    var hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countPostalcode))
    var values = Array.from(hsMap.values());
    var keys = Array.from(hsMap.keys());
    console.log(values) //33,33,2
    console.log(keys) //chinese etc.
    //console.log(chart);

    exampleChart1('chartWindow1', values, keys);
    comparisonChartWithCategory(json_return_markerArrTemp,"test",'lineComp1',0)
    //comparisonChart(json_return_markerArrTemp)
    show1()

    //Window2
    if (json_return_markerArrTemp[1] != null) {
        comparisonChartWithCategory(json_return_markerArrTemp,"test",'lineComp2',1)
        document.getElementById('sideWindow2inner').innerText = JSON.stringify(json_return_markerArrTemp[1]);
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());

        exampleChart2('chartWindow2', values, keys);
        show2()
    }

    //window3
    if (json_return_markerArrTemp[2] != null) {
        comparisonChartWithCategory(json_return_markerArrTemp,"test",'lineComp3',2)
        document.getElementById('sideWindow3inner').innerText = JSON.stringify(json_return_markerArrTemp[2]);
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());

        exampleChart3('chartWindow3', values, keys);
        show3()
    }
    console.log(json_return_markerArrTemp)

}

let myChart1 = null;
let myChart2 = null;
let myChart3 = null;

document.getElementById('dropMenu1').style.display = 'none';
document.getElementById('dropMenu2').style.display = 'none';
document.getElementById('dropMenu3').style.display = 'none';

function show1() {
    document.getElementById('dropMenu1').style.display = 'inline';
}
function show2() {
    document.getElementById('dropMenu2').style.display = 'inline';
}
function show3() {
    document.getElementById('dropMenu3').style.display = 'inline';
}

//TODO Chart für jeden case, vom Value rausfinden welches Chart// Values aus der Hashmap Rausfiltern
function updateChartbyDropdown1(){
    var hsMap;
    var values;
    var keys;


    var valueDropdown = document.getElementById('AreaDropDown1')
    console.log(valueDropdown.value)

    //document.getElementById('dropMenu1').style.display = 'none'; //TODO Dropdown Menu Hidden Standartmäßig außer es wird auf einen marker gedrückt



    //TODO: Values an die Methode Übergeben
    if(valueDropdown.value==1.1){
         hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countPostalcode))
         values = Array.from(hsMap.values());
         keys = Array.from(hsMap.keys());
        exampleChart1('chartWindow1',values,keys)
    }
    if(valueDropdown.value==1.2){
         hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countCity))
         values = Array.from(hsMap.values());
         keys = Array.from(hsMap.keys());
        exampleChart1('chartWindow1',values,keys)
    }
    if(valueDropdown.value==1.3){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countState))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart1('chartWindow1',values,keys)
    }

}

function updateChartbyDropdown2(){

    var hsMap;
    var values;
    var keys;

    var valueDropdown = document.getElementById('AreaDropDown2')
    console.log(valueDropdown.value)

    if(valueDropdown.value==2.1){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart2('chartWindow2',values,keys)
    }
    if(valueDropdown.value==2.2){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countCity))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart2('chartWindow2',values,keys)
    }
    if(valueDropdown.value==2.3){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countState))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart2('chartWindow2',values,keys)

    }

}

function updateChartbyDropdown3(){

    var hsMap;
    var values;
    var keys;

    var valueDropdown = document.getElementById('AreaDropDown3')
    console.log(valueDropdown.value)

    if(valueDropdown.value==3.1){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart3('chartWindow3',values,keys)
    }
    if(valueDropdown.value==3.2){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countCity))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart3('chartWindow3',values,keys)
    }
    if(valueDropdown.value==3.3){
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countState))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        exampleChart3('chartWindow3',values,keys)
    }

}

function exampleChart1(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
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
        plugins: [ChartDataLabels],
        options: {


            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false,
            },

            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: 'Categories in a certain area'
                },
            }
        },


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
        plugins: [ChartDataLabels],
        options: {


            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false,
            },

            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: 'Categories in a certain area'
                },
            }
        },


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
        plugins: [ChartDataLabels],
        options: {


            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false,
            },

            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: 'Categories in a certain area'
                },
            }
        },


    }


    if (myChart3 != null) {
        myChart3.destroy();
    }
    myChart3 = new Chart(
        document.getElementById(div),
        config
    );

}







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

var compChart = null;
var compChart2 = null;
var compChart3 = null;

function comparisonChart(dataArr) {


    console.log(compChart)

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

function comparisonChartWithCategory(dataArr,label,div,position) {



/*
    if (compChart != null) {
        compChart.destroy();
    }


    if (compChart2 != null) {
        compChart2.destroy();
    }

    if (compChart3 != null) {
        compChart3.destroy();
    }


 */







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
                label: 'Score by stars',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                //data: [20,30,60,10],
                data: [dataArr[position].spring, dataArr[position].summer, dataArr[position].fall, dataArr[position].winter]

            },
            {
                label: 'Score by Sentiment Analysis',
                backgroundColor: 'rgb(54, 162, 235)',
                borderColor: 'rgb(54, 162, 235)',
                //data: [20,30,60,10],
                data: [dataArr[position].sentSpring, dataArr[position].sentSummer, dataArr[position].sentFall, dataArr[position].sentWinter]


            }
        ],
        plugins: [ChartDataLabels]
    };
    const config = {
        type: 'line',
        data: data,
        options: {


        responsive: true,
            interaction: {
        mode: 'index',
            intersect: false,
    },

            plugins: {
                title: {
                    display: true,
                    text: 'Comparison between Stars and Sentiment Analysis - Normalized(0-1)'
                },
            }
        },

    };

    if(position==0){
        if (compChart != null) {
            compChart.destroy();
        }
        compChart = new Chart(
            document.getElementById(div),
            config
        );
    }
    if(position==1){
        if (compChart2 != null) {
            compChart2.destroy();
        }
        compChart2 = new Chart(
            document.getElementById(div),
            config
        );
    }
    if(position==2){
        if (compChart3 != null) {
            compChart3.destroy();
        }
        compChart3 = new Chart(
            document.getElementById(div),
            config
        );
    }


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



