var franchiseList = null;

getFranchiseData()

async function getFranchiseData() {


    await $.ajax({
        'async': "false",
        'type': "GET",
        'global': false,
        'url': "/analyze/franchise/",
        //'contentType': "text",
        //'data':businessID.toString(),
        'contentType': "application/json; charset=utf-8",
        //'data': JSON.stringify({business_id: businessID}),
        //dataType: "json",
        'success': function (data) {
            //console.log("test")
            //console.log(data)
            franchiseList = data


        }
    });
}


// Wartet auf die antwort der AJAX calls !!!
$.when(getFranchiseData()).done(function () {
    console.log(franchiseList)
    getTop10Keys()
    getTop10Data()
    // BAr Chart for Top 10

    barChart10()

});


function getTop10Keys() {
    var tempArray = [];
    for (let i = 0; i < 10; i++) {
        tempArray.push(franchiseList.countFranchise[i].name1)
    }

    return tempArray
}

function getTop10Data() {
    var tempArray = [];
    for (let i = 0; i < 10; i++) {
        tempArray.push(franchiseList.countFranchise[i].counter)
    }

    return tempArray
}


function barChart10() {

    let backgroundColor = ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3','#3700B3']

    var config = {
        type: "bar",
        data: {

            labels: getTop10Keys(),
            datasets: [{
                label: 'TOP 10',
                data: getTop10Data(),
                options: {
                    // This chart will not respond to mousemove, etc
                    events: ['click'],

                    onclick
                },
                backgroundColor: backgroundColor,
                borderColor: ["black", "black", "black", "black", "black", "black", "black", "black", "black", "black"],
                borderWidth: 1,
                datalabels: {
                    color: 'BB86FC',
                    anchor: 'end',
                    align: 'top'
                }
            }]
        },
        plugins: [ChartDataLabels],

        options: {
            onclick: function (e) {
                console.log(e);
                for (var i = 0; i < backgroundColor.length; i++) {
                    backgroundColor[i] = 'red';
                }
                backgroundColor[e[0]._index] = 'red';
                this.update();
            },
            scales: {
                yAxes: [{
                    ticks: {
                        fontColor: '#fff'
                    }
                }],
                xAxes: [{
                    ticks: {
                        fontColor: '#fff'
                    }
                }],
            },

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
                    text: 'Top 10 Franchise'
                },
            }
        },


    }

    const ctx = document.getElementById("barChart10")
    var myChart1 = new Chart(
        ctx,
        config,
    );

    function clickHandler(click) {
        //console.log("click")
        const color = ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3','#3700B3']

        myChart1.config.data.datasets[0].backgroundColor = color;
        const points = myChart1.getElementsAtEventForMode(click, 'nearest', {
            intersect: true
        }, true);
        //console.log(points)
        //console.log(points[0].index)

        if (points[0]) {
            myChart1.data.datasets[points[0].datasetIndex].backgroundColor[points[0].index] = '#BB86FC'
        }
        myChart1.update();
        //console.log(franchiseList.countFranchise[points[0].index].name1)

        getMoreInfo(franchiseList.countFranchise[points[0].index].name1)

    }

    ctx.onclick = clickHandler;

    document.getElementById('pac-man').hidden = true;

    document.getElementById('main-page').hidden = false;
}


function getMoreInfo(restaurant) {
    var tempList1 = []
    var tempList2 = []

    for (let i = 0; i < 10; i++) {
        if (restaurant === franchiseList.eachAverage[i].name1) {
            //console.log(franchiseList.eachAverage[i].counter)   // Die folge von top Franchise und review count ist unterschiedlich deswegen rausfinden welches restaurant angedrückt wurde und dieses finden

            let normalizedScore = franchiseList.eachAverage[i].counter.toFixed(4) / 5;
            normalizedScore = normalizedScore.toFixed(4);

            //document.getElementById('inputEachScore').value = franchiseList.eachAverage[i].counter.toFixed(4)
            document.getElementById('inputEachScore').value = normalizedScore;
            //document.getElementById('inputEachScoreLbl').innerText = "Average star Rating: " + franchiseList.eachAverage[i].counter.toFixed(4)
            document.getElementById('inputEachScoreLbl').innerText = "Average star Rating: " + normalizedScore;
            document.getElementById('inputEachScoreSent').value = franchiseList.avgSentiment[i].counter.toFixed(4)
            document.getElementById('inputEachScoreSentLbl').innerText = "Average sentient Rating: " + franchiseList.avgSentiment[i].counter.toFixed(4)

            document.getElementById('inputBestReviewCount').innerHTML = "Anzahl der besten Reviews: " + franchiseList.countBestReview
            document.getElementById('inputBestReviewCount2').innerHTML = "Anzahl der besten Reviews: " + franchiseList.countBestReview


            for (let j = 0; j < 10; j++) {
                if (restaurant === franchiseList.countBestReview[j].franchise1) {
                    //console.log(franchiseList.countBestReview[j].liste[0].counter)
                    document.getElementById('inputBestReviewCount').innerHTML = "Number of Businesses in Top five Cities: " + franchiseList.countBestReview[j].liste[0].name1
                    document.getElementById('inputBestReviewCount2').innerHTML = "Numbers of Reviews: " + franchiseList.countBestReview[j].liste[0].counter
                    document.getElementById('inputWorstReviewCount').innerHTML = "Number of Businesses in Worst five Cities: " + franchiseList.countWorstReview[j].liste[0].name1
                    document.getElementById('inputWorstReviewCount2').innerHTML = "Numbers of Reviews: " + franchiseList.countWorstReview[j].liste[0].counter
                }
            }
            //console.log(franchiseList.countBestReview[i].liste[0].counter) //


            //Todo Key = name / Value = number - for each categorie
            var tempArrKeysCategory = [];
            var tempArrValuesCategory = [];


            //let addTemp = ""
            for (let j = 0; j < 10; j++) {
                if (restaurant === franchiseList.countBestReview[j].franchise1) {
                    for (let k = 0; k < franchiseList.countCategories[j].liste.length; k++) {
                        //addTemp+=franchiseList.countCategories[j].liste[k].name1
                        //addTemp+=", "
                        tempArrKeysCategory.push(franchiseList.countCategories[j].liste[k].name1)
                        tempArrValuesCategory.push(franchiseList.countCategories[j].liste[k].counter)
                    }
                    //console.log(franchiseList.countCategories[j].liste.length)
                }


            }
            //addTemp=addTemp.slice(0, -1)
            //addTemp=addTemp.slice(0, -1)
            //document.getElementById('inputCategories').innerHTML ="Kategorien: "+ addTemp

        }


    }
    //console.log(franchiseList.bestCity[0].liste[0].name1)

    //document.getElementById('1').innerHTML = franchiseList.bestCity[0].liste[0].name1

    //Todo Key = name / Value = number - for 5 best cities
    var tempArrKeys2 = []
    var tempArrValues2 = []

    for (let x = 0; x < 10; x++) {
        //console.log(franchiseList.bestCity[x].franchise1)
        if (restaurant === franchiseList.bestCity[x].franchise1) {
            for (let i = 0; i < 5; i++) {
                tempArrKeys2.push(franchiseList.bestCity[x].liste[i].name1)
                tempArrValues2.push(franchiseList.bestCity[x].liste[i].counter)
                //document.getElementById((i+1).toString()).innerHTML =franchiseList.bestCity[x].liste[i].name1
                //document.getElementById((i+1).toString()+(i+1).toString()).innerHTML =franchiseList.bestCity[x].liste[i].counter
            }

        }
    }

    //Todo Key = name / Value = number - for 5 worst cities
    var tempArrKeys1 = []
    var tempArrValues1 = []

    for (let x = 0; x < 10; x++) {
        //console.log(franchiseList.bestCity[x].franchise1)
        if (restaurant === franchiseList.worstCity[x].franchise1) {
            for (let i = 0; i < 5; i++) {
                tempArrKeys1.push(franchiseList.worstCity[x].liste[i].name1)
                tempArrValues1.push(franchiseList.worstCity[x].liste[i].counter)
                //document.getElementById((9).toString()+(i+1).toString()).innerHTML =franchiseList.worstCity[x].liste[i].name1
                //document.getElementById((9).toString()+(i+1).toString()+(i+1).toString()).innerHTML =franchiseList.worstCity[x].liste[i].counter
            }

        }
    }
    console.log(tempArrKeys1)
    console.log(tempArrValues1)
    //exampleChart3(div, values, keys)


    //Todo Key = name / Value = number - for 10 cities with most restaurants
    var tempArrKeys = []
    var tempArrValues = []
    for (let x = 0; x < 10; x++) {
        //console.log(franchiseList.bestCity[x].franchise1)
        if (restaurant === franchiseList.storesInCity[x].franchise1) {
            for (let i = 0; i < 10; i++) {
                //console.log(franchiseList.storesInCity[x].liste[i].name1)
                //console.log(franchiseList.storesInCity[x].liste[i].counter)
                tempArrKeys.push(franchiseList.storesInCity[x].liste[i].name1)
                tempArrValues.push(franchiseList.storesInCity[x].liste[i].counter)
                /*
                document.getElementById((8).toString()+(i+1).toString()).innerHTML =franchiseList.storesInCity[x].liste[i].name1
                document.getElementById((8).toString()+(i+1).toString()+(i+1).toString()).innerHTML =franchiseList.storesInCity[x].liste[i].counter
                document.getElementById("80").innerHTML = franchiseList.storesInCity[x].liste[i+1].name1
                document.getElementById("800").innerHTML = franchiseList.storesInCity[x].liste[i+1].counter

                 */
            }


        }
    }

    //Todo Gives the charts a name
    console.log(tempArrKeys)
    exampleChart2('clickChart', tempArrValues, tempArrKeys) //10 cities with most restaurants
    exampleChart3('clickChart1', tempArrValues1, tempArrKeys1) //worst 5
    exampleChart4('clickChart2', tempArrValues2, tempArrKeys2) //best 5
    exampleChart5('catChart', tempArrValuesCategory, tempArrKeysCategory) //categories

    //console.log(franchiseList.eachAverage[number].name1)
    //console.log(franchiseList.eachAverage[number].counter)

    document.getElementById('sub-page').hidden = false;

}


var myChart2 = null;

//Todo design for 10 cities with most restaurants
function exampleChart2(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3','#3700B3'],
                borderColor: ["black", "black", "black", "black", "black", "black", "black", "black", "black", "black"],
                borderWidth: 1,
                datalabels: {
                    color: 'BB86FC',
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
                    text: 'Cities with the most stores'
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

var myChart3 = null;

//Todo 5 worst cities
function exampleChart3(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3','#3700B3'],
                borderColor: ["black", "black", "black", "black", "black", "black", "black", "black", "black", "black"],
                borderWidth: 1,
                datalabels: {
                    color: 'BB86FC',
                    anchor: 'end',
                    align: 'top'
                }
            }]
        },
        plugins: [ChartDataLabels],
        options: {

            scales: {
                y: {
                    suggestedMin: 0,
                    suggestedMax: 5
                }
            },
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
                    text: 'Worst rated cities'
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

var myChart4 = null;

//Todo 5 best rated cities
function exampleChart4(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3','#3700B3'],
                borderColor: ["black", "black", "black", "black", "black", "black", "black", "black", "black", "black"],
                borderWidth: 1,
                datalabels: {
                    color: 'BB86FC',
                    anchor: 'end',
                    align: 'top'
                }
            }]
        },
        plugins: [ChartDataLabels],
        options: {

            scales: {
                y: {
                    suggestedMin: 0,
                    suggestedMax: 5
                }
            },
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
                    text: 'Best rated cities'
                },
            }
        },

    }

    if (myChart4 != null) {
        myChart4.destroy();
    }
    myChart4 = new Chart(
        document.getElementById(div),
        config
    );


}


var myChart5 = null;

//Todo Categories
function exampleChart5(div, values, keys) {


    var config = {
        type: "bar",
        data: {

            labels: keys,
            datasets: [{
                label: 'count',
                data: values,
                backgroundColor: ['#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3', '#3700B3','#3700B3'],
                borderColor: ["black", "black", "black", "black", "black", "black", "black", "black", "black", "black"],
                borderWidth: 1,
                datalabels: {
                    color: 'BB86FC',
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
                    text: 'Categories counted'
                },
            }
        },


    }

    if (myChart5 != null) {
        myChart5.destroy();
    }
    myChart5 = new Chart(
        document.getElementById(div),
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



