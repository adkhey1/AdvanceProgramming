


var franchiseList = null;

getFranchiseData()

async function getFranchiseData() {


    await $.ajax({
        'async': "true",
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
            console.log(data)
            franchiseList = data


        }
    });
}

$.when(getFranchiseData()).done(function() {
    console.log(franchiseList)
    console.log('All 3 ajax request complete.');
    document.getElementById("data").innerHTML = franchiseList.bestCity[0].counter
    document.getElementById("data").innerHTML = franchiseList.countFranchise[0].name1
    console.log(franchiseList.bestCity[0])



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
   console.log(tempArray)
    return tempArray
}

function getTop10Data(){
    var tempArray = [];
    for (let i = 0; i < 10; i++) {
        tempArray.push(franchiseList.countFranchise[i].counter)
    }
    console.log(tempArray)
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
        plugins: [ChartDataLabels]


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
        console.log(points)
        console.log(points[0].index)
        console.log(franchiseList.countFranchise[points[0].index].name1)

        getMoreInfo(franchiseList.countFranchise[points[0].index].name1)

    }

    ctx.onclick = clickHandler;

}


function getMoreInfo(restaurant){
    for (let i = 0; i < 10; i++) {
        if(restaurant===franchiseList.eachAverage[i].name1){
            console.log(franchiseList.eachAverage[i].counter)   // Die folge von top Franchise und review count ist unterschiedlich deswegen rausfinden welches restaurant angedrückt wurde und dieses finden
            document.getElementById('inputEachScore').innerHTML ="average Score: "+ franchiseList.eachAverage[i].counter
        }
    }

    //console.log(franchiseList.eachAverage[number].name1)
    //console.log(franchiseList.eachAverage[number].counter)
}


function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}



