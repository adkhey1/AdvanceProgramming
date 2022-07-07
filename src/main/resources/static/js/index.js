let states = null;
var categories = null;
let attributes = null;
let currCoice1 = 0;
let currCoice2 = 0;
let currCoice3 = 0;

var filteredJsonData;


function searchText() {
    console.log("erfolg funktion ausgeloest")

    var search = {
        stars: document.getElementById("stars").value,
        name: document.getElementById("name").value,
        state: document.getElementById("state").value,
        city: document.getElementById("City").value,
        plz: document.getElementById("PLZ").value,
        category: document.getElementById("category").value,
        attribute: document.getElementById("attribute").value

    }

    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        url: "/restaurant/filtered/",
        data: JSON.stringify(search), // Note it is important
        success: function (result) {
            filteredJsonData = result
            console.log(filteredJsonData)
            initMapAfterFilter()
        }
    });


}


function initMapAfterFilter() {
    let centerLat = 0.0;
    let centerLong = 0.0;
    let coordiCounter = 0;


    if(filteredJsonData.length === 0){
        console.log("asdasd")
        document.getElementById('noResult').style.display = 'inline';
        document.getElementById('noResult').innerHTML="No result matching your criteria :("
    } else {
        document.getElementById('noResult').style.display = 'none';
        console.log("else schleife")


        for (let i = 0; i < filteredJsonData.length; i++) {

            centerLat += filteredJsonData[i].latitude
            centerLong += filteredJsonData[i].longitude
            console.log(centerLat)
            console.log(filteredJsonData[i].latitude)
            console.log(i)
            coordiCounter++;
        }

        centerLat = centerLat / coordiCounter;
        centerLong = centerLong / coordiCounter;


        const map = new google.maps.Map(document.getElementById("map"), {
            //zoom: 4, center: {lat: 40.560109, lng: -100.573589}
            zoom: 4, center: {lat: centerLat, lng: centerLong}

        })


        function addMarker(latLongPos, businessID, name) {
            const marker = new google.maps.Marker({
                position: latLongPos,

                map: map


            });

            const detailWindow = new google.maps.InfoWindow({
                content: name
            });

            marker.addListener("click", () => {
                detailWindow.open(map, marker);


                getInfoOnClick()

                function getInfoOnClick() {
                    $.ajax({
                        'async': "true",
                        'type': "POST",
                        'global': false,
                        'url': "/map/viewMarker/", //'contentType': "text",
                        //'data':businessID.toString(),
                        'contentType': "application/json; charset=utf-8",
                        'data': JSON.stringify({business_id: businessID}), //dataType: "json",
                        'success': function (data) {
                            //console.log("test")
                            //console.log(data)
                            json_return_marker = data
                            console.log(businessID)
                            sideView()
                        }
                    });
                }

            })

        }

        var myLatlng;

        for (let i = 0; i < json_data_LatLongArray.length; i++) {
            myLatlng = new google.maps.LatLng(filteredJsonData[i].latitude, filteredJsonData[i].longitude);
            addMarker(myLatlng, filteredJsonData[i].business_id, filteredJsonData[i].name)


        }
    }
}


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


// TODO Code mit Variabel übergabe Problem END


//TODO END


var json_return_marker;

//let map = google.maps.Map;
//let marker = google.maps.Marker = [];

// map
function initMap() {

    let centerLat = 0.0;
    let centerLong = 0.0;
    let coordiCounter = 0;

    for (let i = 0; i < json_data_LatLongArray.length; i++) {
        centerLat += json_data_LatLongArray[i].latitude
        centerLong += json_data_LatLongArray[i].longitude

        coordiCounter++;
    }

    centerLat = centerLat / coordiCounter;
    centerLong = centerLong / coordiCounter;


    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4, center: {lat: centerLat, lng: centerLong}
    })


    function addMarker(latLongPos, businessID, name) {
        const marker = new google.maps.Marker({
            position: latLongPos,

            map: map


        });

        const detailWindow = new google.maps.InfoWindow({
            content: name
        });

        marker.addListener("click", () => {
            detailWindow.open(map, marker);


            getInfoOnClick()
            let run = false

            function getInfoOnClick() {
                $.ajax({
                    'async': "true", 'type': "POST", 'global': false, 'url': "/map/viewMarker/", //'contentType': "text",
                    //'data':businessID.toString(),
                    'contentType': "application/json; charset=utf-8", 'data': JSON.stringify({business_id: businessID}), //dataType: "json",
                    'success': function (data) {
                        //console.log("test")
                        //console.log(data)
                        json_return_marker = data
                        sideView()
                    }
                });
            }

            //console.log(json_return_marker)

            /*
            setTimeout(function () {
                console.log(json_return_marker)


            }, 10000);



             */


        })

    }


    //add all marker
    var myLatlng;

    for (let i = 0; i < json_data_LatLongArray.length; i++) {
        myLatlng = new google.maps.LatLng(json_data_LatLongArray[i].latitude, json_data_LatLongArray[i].longitude);
        addMarker(myLatlng, json_data_LatLongArray[i].business_id, json_data_LatLongArray[i].name)


    }

    function deleteMarkers() {
        console.log("deletet")
        json_data_LatLongArray = []
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
        'async': false, 'type': "POST", 'global': false, 'url': "/map/restaurants/", 'success': function (data) {
            json_data_LatLongArray = data;
        }
    });
}

loadMapMarkers();

function drawDetails(arrElement) {

    let div = document.createElement('div');
    let lName = document.createElement('label');
    lName.innerHTML = "Name of Business:"
    let pName = document.createElement('p');
    pName.innerHTML = "<b>" + arrElement.name + "</b>";
    let lAddress = document.createElement('label');
    lAddress.innerHTML = "Address of Business:"
    let pAdress = document.createElement('p');
    pAdress.innerHTML = arrElement.address;
    let lCity = document.createElement('label');
    lCity.innerHTML = "City:"
    let pCity = document.createElement('p');
    pCity.innerHTML = arrElement.postal_code;
    let lState = document.createElement('label');
    lState.innerHTML = "State:"
    let pState = document.createElement('p');
    pState.innerHTML = arrElement.state;

    let lStars = document.createElement('label');
    lStars.innerHTML = "Rating:"
    let pStars = document.createElement('p');
    pStars.innerHTML = arrElement.stars;


    let lHours = document.createElement('label');
    lHours.innerHTML = "Open hours:"

    let times = arrElement.hours;
    for (let i = 0; i < times.length; i++) {
        times = times.replace("'", '"');
    }
    times = JSON.parse(times);

    function replaceNull(input) {
        let output;
        if (input == null) {
            output = "closed"
        } else {
            output = input
        }
        return output;
    }

    let mond, tue, wed, thur, fri, sat, sun;
    //mond = times.Monday;
    mond = replaceNull(times.Monday);
    tue = replaceNull(times.Tuesday);
    wed = replaceNull(times.Wednesday);
    thur = replaceNull(times.Thursday);
    fri = replaceNull(times.Friday);
    sat = replaceNull(times.Saturday);
    sun = replaceNull(times.Sunday);

    let divTable = document.createElement("div");

    let tableTemplate = "<table class='table table-bordered table-sm'>\n" +
        "                        <thead class='table-dark'>\n" +
        "                        <tr>\n" +
        "                            <th>Monday</th>\n" +
        "                            <th>Tuesday</th>\n" +
        "                            <th>Wednesday</th>\n" +
        "                            <th>Thursday</th>\n" +
        "                            <th>Friday</th>\n" +
        "                            <th>Saturday</th>\n" +
        "                            <th>Sunday</th>\n" +
        "                        </tr>\n" +
        "                        </thead>\n" +
        "                        <tbody>\n" +
        "                        <tr>\n" +
        "                            <td name=\"monday\">" + mond + "</td>\n" +
        "                            <td name=\"tuesday\">" + tue + "</td>\n" +
        "                            <td name=\"wednesday\">" + wed + "</td>\n" +
        "                            <td name=\"thursday\">" + thur + "</td>\n" +
        "                            <td name=\"friday\">" + fri + "</td>\n" +
        "                            <td name=\"saturday\">" + sat + "</td>\n" +
        "                            <td name=\"sunday\">" + sun + "</td>\n" +
        "                        </tr>\n" +
        "                        </tbody>\n" +
        "                    </table>"

    if (arrElement.hours == 0) {
        let p = document.createElement("p").innerHTML = "no information provided";
        divTable.innerHTML = p;

    } else {
        divTable.innerHTML = tableTemplate;
    }

    div.appendChild(lName);
    div.appendChild(pName);
    div.appendChild(lAddress)
    div.appendChild(pAdress);
    div.appendChild(lCity);
    div.appendChild(pCity);
    div.appendChild(lState);
    div.appendChild(pState);
    div.appendChild(lHours);
    div.appendChild(divTable);
    //div.appendChild(pHours); //_---------------------------------------
    div.appendChild(lStars);
    div.appendChild(pStars);

    return div;
}


var json_return_markerArrTemp = [] //


//konzept für 3 Fenster
function sideView() {

    //fill the Array
    if (json_return_markerArrTemp.length < 3) {
        json_return_markerArrTemp.unshift(json_return_marker)
    } else {
        json_return_markerArrTemp.pop()
        json_return_markerArrTemp.unshift(json_return_marker)
    }

    //Evtl löschen


    let result = drawDetails(json_return_markerArrTemp[0]);

    // const child = document.getElementById('sideWindow1inner').childNodes;
    // document.getElementById('sideWindow1inner').removeChild(child[0]);
    document.getElementById('sideWindow1inner').innerHTML=""
    document.getElementById('sideWindow1inner').appendChild(result);
    //document.getElementById('sideWindow1inner').innerHTML = result
    //document.getElementById('sideWindow1inner').innerText = JSON.stringify(json_return_markerArrTemp[0]);

    //transform to hashmap and get keys and values
    var hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countPostalcode))
    var values = Array.from(hsMap.values());
    var keys = Array.from(hsMap.keys());
    //console.log(chart);

    exampleChart1('chartWindow1', values, keys);
    comparisonChartWithCategory(json_return_markerArrTemp, "test", 'lineComp1', 0)
    //comparisonChart(json_return_markerArrTemp)
    show1()

    //Window2
    if (json_return_markerArrTemp[1] != null) {
        comparisonChartWithCategory(json_return_markerArrTemp, "test", 'lineComp2', 1)
        let result1 = drawDetails(json_return_markerArrTemp[1]);
        //const child1 = document.getElementById('sideWindow2inner').childNodes;
        //document.getElementById('sideWindow2inner').removeChild(child1[0]);
        document.getElementById('sideWindow2inner').innerHTML=""
        document.getElementById('sideWindow2inner').appendChild(result1);
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());

        exampleChart2('chartWindow2', values, keys);
        show2()
    }

    //window3
    if (json_return_markerArrTemp[2] != null) {
        comparisonChartWithCategory(json_return_markerArrTemp, "test", 'lineComp3', 2)
        let result2 = drawDetails(json_return_markerArrTemp[2]);
        //const child2 = document.getElementById('sideWindow3inner').childNodes;
        //document.getElementById('sideWindow3inner').removeChild(child2[0]);
        document.getElementById('sideWindow3inner').innerHTML=""
        document.getElementById('sideWindow3inner').appendChild(result2);
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


function updateChartbyDropdown1() {
    var hsMap;
    var values;
    var keys;


    var valueDropdown = document.getElementById('AreaDropDown1')
    console.log(valueDropdown.value)

    //document.getElementById('dropMenu1').style.display = 'none'; //TODO Dropdown Menu Hidden Standartmäßig außer es wird auf einen marker gedrückt


    //TODO: Values an die Methode Übergeben
    if (valueDropdown.value == 1.1) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice1 = 0
        document.getElementById('map1').style.display = 'none';
        exampleChart1('chartWindow1', values, keys)
    }
    if (valueDropdown.value == 1.2) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countCity))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice1 = 1
        document.getElementById('map1').style.display = 'none';
        exampleChart1('chartWindow1', values, keys)
    }
    if (valueDropdown.value == 1.3) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[0].countState))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice1 = 2
        document.getElementById('map1').style.display = 'none';
        exampleChart1('chartWindow1', values, keys)
    }

}

function updateChartbyDropdown2() {

    var hsMap;
    var values;
    var keys;

    var valueDropdown = document.getElementById('AreaDropDown2')
    console.log(valueDropdown.value)

    if (valueDropdown.value == 2.1) {
        console.log("!!!!!")
        console.log(json_return_markerArrTemp[1])
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice2 = 0
        document.getElementById('map2').style.display = 'none';
        exampleChart2('chartWindow2', values, keys)
    }
    if (valueDropdown.value == 2.2) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countCity))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice2 = 1
        document.getElementById('map2').style.display = 'none';
        exampleChart2('chartWindow2', values, keys)
    }
    if (valueDropdown.value == 2.3) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[1].countState))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice2 = 2
        document.getElementById('map2').style.display = 'none';
        exampleChart2('chartWindow2', values, keys)

    }

}

function updateChartbyDropdown3() {

    var hsMap;
    var values;
    var keys;

    var valueDropdown = document.getElementById('AreaDropDown3')
    console.log(valueDropdown.value)

    if (valueDropdown.value == 3.1) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countPostalcode))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice3 = 0
        document.getElementById('map3').style.display = 'none';
        exampleChart3('chartWindow3', values, keys)
    }
    if (valueDropdown.value == 3.2) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countCity))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice3 = 1
        document.getElementById('map3').style.display = 'none';
        exampleChart3('chartWindow3', values, keys)
    }
    if (valueDropdown.value == 3.3) {
        hsMap = new Map(Object.entries(json_return_markerArrTemp[2].countState))
        values = Array.from(hsMap.values());
        keys = Array.from(hsMap.keys());
        currCoice3 = 2
        document.getElementById('map3').style.display = 'none';
        exampleChart3('chartWindow3', values, keys)
    }

}

function exampleChart1(div, values, keys) {


    var config = {
        type: "bar", data: {

            labels: keys, datasets: [{
                data: values,
                options: {
                    events: ['click']
                },
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3'],
                datalabels: {
                    color: 'BB86FC', anchor: 'end', align: 'top'
                }
            }]
        }, plugins: [ChartDataLabels], options: {


            responsive: true, interaction: {
                mode: 'index', intersect: false,
            },

            plugins: {
                legend: {
                    display: false
                }, title: {
                    display: true, text: 'Categories in a certain area'
                },
            }
        },


    }

    if (myChart1 != null) {
        myChart1.destroy();
    }
    const ctx = document.getElementById(div)
    myChart1 = new Chart(ctx, config,);

    function clickHandler(click) {
        const points = myChart1.getElementsAtEventForMode(click, 'nearest', {
            intersect: true
        }, true);

        console.log(points)
        console.log(points[0].index)
        console.log(keys[points[0].index])
        initializeMap1(json_return_markerArrTemp[0].business_id, keys[points[0].index], currCoice1, 'map1')


        //console.log(keys[points[0].index])
        //console.log(json_return_markerArrTemp[0].business_id)
        console.log(currCoice1)
    }

    ctx.onclick = clickHandler;

    //document.getElementById('main_page1').hidden = false;
}


function exampleChart2(div, values, keys) {


    var config = {
        type: "bar", data: {

            labels: keys, datasets: [{
                label: 'count',
                data: values,
                options: {
                    events: ['click']
                },
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3'],
                datalabels: {
                    color: 'BB86FC', anchor: 'end', align: 'top'
                }
            }]
        }, plugins: [ChartDataLabels], options: {


            responsive: true, interaction: {
                mode: 'index', intersect: false,
            },

            plugins: {
                legend: {
                    display: false
                }, title: {
                    display: true, text: 'Categories in a certain area'
                },
            }
        },


    }

    if (myChart2 != null) {
        myChart2.destroy();
    }
    const ctx = document.getElementById(div)
    myChart2 = new Chart(document.getElementById(div), config, ctx);

    function clickHandler(click) {
        const points = myChart2.getElementsAtEventForMode(click, 'nearest', {
            intersect: true
        }, true);

        console.log(points)
        console.log(points[0].index)
        console.log(keys[points[0].index])
        initializeMap2(json_return_markerArrTemp[1].business_id, keys[points[0].index], currCoice2, 'map2')


        //console.log(keys[points[0].index])
        //console.log(json_return_markerArrTemp[0].business_id)
        console.log(currCoice2)

    }

    ctx.onclick = clickHandler;

    //document.getElementById('main_page2').hidden = false;
}

function exampleChart3(div, values, keys) {

    var config = {
        type: "bar", data: {

            labels: keys, datasets: [{
                label: 'count',
                data: values,
                options: {
                    events: ['click']
                },
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3'],
                datalabels: {
                    color: 'BB86FC', anchor: 'end', align: 'top'
                }
            }]
        }, plugins: [ChartDataLabels], options: {


            responsive: true, interaction: {
                mode: 'index', intersect: false,
            },

            plugins: {
                legend: {
                    display: false
                }, title: {
                    display: true, text: 'Categories in a certain area'
                },
            }
        },


    }


    if (myChart3 != null) {
        myChart3.destroy();
    }
    const ctx = document.getElementById(div)
    myChart3 = new Chart(document.getElementById(div), config, ctx);

    function clickHandler(click) {
        const points = myChart3.getElementsAtEventForMode(click, 'nearest', {
            intersect: true
        }, true);

        console.log(points)
        console.log(points[0].index)
        console.log(keys[points[0].index])
        initializeMap3(json_return_markerArrTemp[2].business_id, keys[points[0].index], currCoice3, 'map3')

        console.log(currCoice2)
    }

    ctx.onclick = clickHandler;

    //document.getElementById('main_page3').hidden = false;
}


var compChart = null;
var compChart2 = null;
var compChart3 = null;

function comparisonChart(dataArr) {


    console.log(compChart)

    if (compChart != null) {
        compChart.destroy();
    }


    const labels = ['spring', 'summer', 'fall', 'winter'

    ];

    const data = {
        labels: labels,


        datasets: [

            {
                label: 'Dataset 1', backgroundColor: '#3700B3', borderColor: '#3700B3', //data: [20,30,60,10],
                data: [dataArr[0].spring, dataArr[0].summer, dataArr[0].fall, dataArr[0].winter] //0 durch die jeweiliges side view window nummer erstzen

            },], scales: {
            y: {
                suggestedMin: 0,
                suggestedMax: 5
            }
        }, plugins: [ChartDataLabels]
    };


    const config = {
        type: 'line', data: data, options: {}
    };

    compChart = new Chart(document.getElementById('lineComp1'), config);

}

function loadClickedCategory() {
    // TODO AJAX CALL und neues diagramm erstellen das beim click geladen wird
}

function comparisonChartWithCategory(dataArr, label, div, position) {


    const labels = ['spring', 'summer', 'fall', 'winter'];

    const data = {
        labels: labels,


        datasets: [

            {
                label: 'Score by stars', backgroundColor: '#BB86FC', borderColor: '#BB86FC', //data: [20,30,60,10],
                data: [dataArr[position].spring, dataArr[position].summer, dataArr[position].fall, dataArr[position].winter]

            }, {
                label: 'Score by Sentiment Analysis',
                backgroundColor: '#6200EE',
                borderColor: '#6200EE', //data: [20,30,60,10],
                data: [dataArr[position].sentSpring, dataArr[position].sentSummer, dataArr[position].sentFall, dataArr[position].sentWinter]


            }], plugins: [ChartDataLabels]
    };
    const config = {
        type: 'line', data: data, options: {


            responsive: true, interaction: {
                mode: 'index', intersect: false,
            },

            plugins: {
                title: {
                    display: true, text: 'Comparison between Stars and Sentiment Analysis - Normalized(0-1)'
                },
            }
        },

    };

    if (position == 0) {
        if (compChart != null) {
            compChart.destroy();
        }
        compChart = new Chart(document.getElementById(div), config);
    }
    if (position == 1) {
        if (compChart2 != null) {
            compChart2.destroy();
        }
        compChart2 = new Chart(document.getElementById(div), config);
    }
    if (position == 2) {
        if (compChart3 != null) {
            compChart3.destroy();
        }
        compChart3 = new Chart(document.getElementById(div), config);
    }


}


//var map1;


document.getElementById('map1').style.display = 'none';
document.getElementById('map2').style.display = 'none';
document.getElementById('map3').style.display = 'none';

//TODO ---- div --- windownumber---
function initializeMap1(business_id, categorie, choice, div) {

    var latLongMap;

    getLatLongAjax()

    $.when(getLatLongAjax()).done(function () {
        var myLatlngSideView
        console.log(latLongMap)

        let centerLat = 0.0;
        let centerLong = 0.0;
        let coordiCounter = 0;

        for (let i = 0; i < latLongMap.output.length; i++) {
            centerLat += latLongMap.output[i].latitude1
            centerLong += latLongMap.output[i].longitude1

            coordiCounter++;
        }

        centerLat = centerLat / coordiCounter;
        centerLong = centerLong / coordiCounter;

        var myOptions = {
            //zoom: 4, center: new google.maps.LatLng(40.560109, -100.573589), mapTypeId: google.maps.MapTypeId.ROADMAP
            zoom: 10, center: new google.maps.LatLng(centerLat, centerLong), mapTypeId: google.maps.MapTypeId.ROADMAP
        }


        var map1 = new google.maps.Map(document.getElementById(div), myOptions);


        for (let i = 0; i < latLongMap.output.length; i++) {
            myLatlngSideView = new google.maps.LatLng(latLongMap.output[i].latitude1, latLongMap.output[i].longitude1);
            addMarkerSideViewMap(myLatlngSideView)
            //console.log(myLatlngSideView)
        }

        function addMarkerSideViewMap(latLongPos) {
            const marker = new google.maps.Marker({
                position: latLongPos, map: map1
            });

        }


        document.getElementById(div).style.display = 'block';

    });

    async function getLatLongAjax() {

        await $.ajax({
            'async': "false",
            'type': "POST",
            'global': false,
            'url': "/categorieMap", //'contentType': "text",
            //data:'business_id'+ business_id+'categorie'+categorie+'choice'+choice,
            'data': JSON.stringify({"business_id": business_id, "categorie": categorie, "choice": choice}),
            'contentType': "application/json; charset=utf-8", //'data': JSON.stringify({business_id: businessID}),
            //dataType: "json",
            'success': function (data) {
                //console.log("test")
                //console.log(data)
                latLongMap = data


            }
        });
    }

}

function initializeMap2(business_id, categorie, choice, div) {

    var latLongMap;

    getLatLongAjax()

    $.when(getLatLongAjax()).done(function () {
        var myLatlngSideView
        console.log(latLongMap)

        let centerLat = 0.0;
        let centerLong = 0.0;
        let coordiCounter = 0;

        for (let i = 0; i < latLongMap.output.length; i++) {
            centerLat += latLongMap.output[i].latitude1
            centerLong += latLongMap.output[i].longitude1

            coordiCounter++;
        }

        centerLat = centerLat / coordiCounter;
        centerLong = centerLong / coordiCounter;

        var myOptions = {
            //zoom: 4, center: new google.maps.LatLng(40.560109, -100.573589), mapTypeId: google.maps.MapTypeId.ROADMAP
            zoom: 10, center: new google.maps.LatLng(centerLat, centerLong), mapTypeId: google.maps.MapTypeId.ROADMAP
        }


        var map1 = new google.maps.Map(document.getElementById(div), myOptions);


        for (let i = 0; i < latLongMap.output.length; i++) {
            myLatlngSideView = new google.maps.LatLng(latLongMap.output[i].latitude1, latLongMap.output[i].longitude1);
            addMarkerSideViewMap(myLatlngSideView)
            //console.log(myLatlngSideView)
        }

        function addMarkerSideViewMap(latLongPos) {
            const marker = new google.maps.Marker({
                position: latLongPos, map: map1
            });

        }


        document.getElementById(div).style.display = 'block';

    });

    async function getLatLongAjax() {

        await $.ajax({
            'async': "false",
            'type': "POST",
            'global': false,
            'url': "/categorieMap", //'contentType': "text",
            //data:'business_id'+ business_id+'categorie'+categorie+'choice'+choice,
            'data': JSON.stringify({"business_id": business_id, "categorie": categorie, "choice": choice}),
            'contentType': "application/json; charset=utf-8", //'data': JSON.stringify({business_id: businessID}),
            //dataType: "json",
            'success': function (data) {
                //console.log("test")
                //console.log(data)
                latLongMap = data


            }
        });
    }

}

function initializeMap3(business_id, categorie, choice, div) {

    var latLongMap;

    getLatLongAjax()


    $.when(getLatLongAjax()).done(function () {
        var myLatlngSideView
        console.log(latLongMap)

        let centerLat = 0.0;
        let centerLong = 0.0;
        let coordiCounter = 0;

        for (let i = 0; i < latLongMap.output.length; i++) {
            centerLat += latLongMap.output[i].latitude1
            centerLong += latLongMap.output[i].longitude1

            coordiCounter++;
        }

        centerLat = centerLat / coordiCounter;
        centerLong = centerLong / coordiCounter;

        var myOptions = {
            //zoom: 4, center: new google.maps.LatLng(40.560109, -100.573589), mapTypeId: google.maps.MapTypeId.ROADMAP
            zoom: 10, center: new google.maps.LatLng(centerLat, centerLong), mapTypeId: google.maps.MapTypeId.ROADMAP
        }


        var map1 = new google.maps.Map(document.getElementById(div), myOptions);


        for (let i = 0; i < latLongMap.output.length; i++) {
            myLatlngSideView = new google.maps.LatLng(latLongMap.output[i].latitude1, latLongMap.output[i].longitude1);
            addMarkerSideViewMap(myLatlngSideView)
            //console.log(myLatlngSideView)
        }

        function addMarkerSideViewMap(latLongPos) {
            const marker = new google.maps.Marker({
                position: latLongPos, map: map1
            });

        }


        document.getElementById(div).style.display = 'block';

    });

    async function getLatLongAjax() {

        await $.ajax({
            'async': "false",
            'type': "POST",
            'global': false,
            'url': "/categorieMap", //'contentType': "text",
            //data:'business_id'+ business_id+'categorie'+categorie+'choice'+choice,
            'data': JSON.stringify({"business_id": business_id, "categorie": categorie, "choice": choice}),
            'contentType': "application/json; charset=utf-8", //'data': JSON.stringify({business_id: businessID}),
            //dataType: "json",
            'success': function (data) {
                //console.log("test")
                //console.log(data)
                latLongMap = data
            }
        });
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