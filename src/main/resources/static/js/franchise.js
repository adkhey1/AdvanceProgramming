


var franchiseList;

getFranchiseData()

function getFranchiseData(){
    $.ajax({
        'async': "true", 'type': "GET",
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
            console.log(franchiseList)
        }
    });

}

console.log(franchiseList)

document.getElementById("data").innerText = franchiseList