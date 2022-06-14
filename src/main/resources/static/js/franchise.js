


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
$.when(getFranchiseData()).done(function() {
    console.log(franchiseList)

    //document.getElementById("data").innerHTML = franchiseList.bestCity[0].counter
    //document.getElementById("data").innerHTML = franchiseList.countFranchise[0].name1




    getTop10Keys()
    getTop10Data()
    // BAr Chart for Top 10

    barChart10()







});


function getTop10Keys(){
    var tempArray = [];
    for (let i = 0; i < 10; i++) {
        tempArray.push(franchiseList.countFranchise[i].name1)
    }

    return tempArray
}

function getTop10Data(){
    var tempArray = [];
    for (let i = 0; i < 10; i++) {
        tempArray.push(franchiseList.countFranchise[i].counter)
    }

    return tempArray
}



function barChart10(){

    var config = {
        type: "bar",
        data: {

            labels: getTop10Keys(),
            datasets: [{
                label: 'TOP 10',
                data: getTop10Data(),
                options: {
                    // This chart will not respond to mousemove, etc
                    events: ['click']
                },
                backgroundColor: [getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(),getRandomColor(),getRandomColor()],
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

    function clickHandler(click){
        //console.log("click")
        const points = myChart1.getElementsAtEventForMode(click,'nearest',{
            intersect: true},true);
        //console.log(points)
        //console.log(points[0].index)
        //console.log(franchiseList.countFranchise[points[0].index].name1)

        getMoreInfo(franchiseList.countFranchise[points[0].index].name1)

    }

    ctx.onclick = clickHandler;

}


function getMoreInfo(restaurant){
    var tempList1 = []
    var tempList2 = []

    for (let i = 0; i < 10; i++) {
        if(restaurant===franchiseList.eachAverage[i].name1){
            //console.log(franchiseList.eachAverage[i].counter)   // Die folge von top Franchise und review count ist unterschiedlich deswegen rausfinden welches restaurant angedrückt wurde und dieses finden
            document.getElementById('inputEachScore').innerHTML ="average Score: "+ franchiseList.eachAverage[i].counter
            document.getElementById('inputBestReviewCount').innerHTML ="Anzahl der besten Reviews: "+ franchiseList.countBestReview

            for (let j = 0; j < 10; j++) {
                if(restaurant===franchiseList.countBestReview[j].franchise1){
                    //console.log(franchiseList.countBestReview[j].liste[0].counter)
                    document.getElementById('inputBestReviewCount').innerHTML ="Anzahl 5-Sterne Bewertung: "+ franchiseList.countBestReview[j].liste[0].counter
                    document.getElementById('inputWorstReviewCount').innerHTML ="Anzahl 0-Sterne Bewertung: "+ franchiseList.countWorstReview[j].liste[0].counter
                }
            }
            //console.log(franchiseList.countBestReview[i].liste[0].counter) //

            let addTemp = ""
            for (let j = 0; j < 10; j++) {
                if (restaurant===franchiseList.countBestReview[j].franchise1){
                    for (let k = 0; k < franchiseList.countCategories[j].liste.length; k++) {
                        addTemp+=franchiseList.countCategories[j].liste[k].name1
                        addTemp+=", "
                    }
                    console.log(franchiseList.countCategories[j].liste.length)
                }




            }
            addTemp=addTemp.slice(0, -1)
            addTemp=addTemp.slice(0, -1)
            document.getElementById('inputCategories').innerHTML ="Kategorien: "+ addTemp

        }


    }
    //console.log(franchiseList.bestCity[0].liste[0].name1)

    //document.getElementById('1').innerHTML = franchiseList.bestCity[0].liste[0].name1
    for (let x = 0; x <10 ; x++) {
        //console.log(franchiseList.bestCity[x].franchise1)
        if(restaurant===franchiseList.bestCity[x].franchise1){
            for (let i = 0; i < 5; i++) {
                document.getElementById((i+1).toString()).innerHTML =franchiseList.bestCity[x].liste[i].name1
                document.getElementById((i+1).toString()+(i+1).toString()).innerHTML =franchiseList.bestCity[x].liste[i].counter
            }

        }
    }

    for (let x = 0; x <10 ; x++) {
        //console.log(franchiseList.bestCity[x].franchise1)
        if(restaurant===franchiseList.worstCity[x].franchise1){
            for (let i = 0; i < 5; i++) {
                document.getElementById((9).toString()+(i+1).toString()).innerHTML =franchiseList.worstCity[x].liste[i].name1
                document.getElementById((9).toString()+(i+1).toString()+(i+1).toString()).innerHTML =franchiseList.worstCity[x].liste[i].counter
            }

        }
    }


    var tempArrKeys = []
    var tempArrValues = []
    for (let x = 0; x <10 ; x++) {
        //console.log(franchiseList.bestCity[x].franchise1)
        if(restaurant===franchiseList.storesInCity[x].franchise1){
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

    console.log(tempArrKeys)
    exampleChart2('clickChart',tempArrValues,tempArrKeys)

    //console.log(franchiseList.eachAverage[number].name1)
    //console.log(franchiseList.eachAverage[number].counter)
}


var myChart2 = null;

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


function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}



