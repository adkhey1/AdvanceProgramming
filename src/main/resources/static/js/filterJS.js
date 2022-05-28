
function rescticteAlphabet(e){


    var x = e.which || e.keycode;
    if((x>=48 && x<=57))
        return true;
    else
        return false;
}

function openForm() {
    document.getElementById("popupForm").style.display = "block";
}

function closeForm() {
    document.getElementById("popupForm").style.display = "none";
}

function searchText() {

    var search = {
        //vegan : document.getElementById('vegan').checked,
        star: document.getElementById("stars").value,
        name: document.getElementById("name").value,
        time: document.getElementById("time").value,
        day: document.getElementById("day").value,
        state: document.getElementById("State").value,
        city: document.getElementById("City").value,
        plz: document.getElementById("PLZ").value,
        kategorie: document.getElementById("kategorie").value,
        atribute: document.getElementById("atribute").value

    }

    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        url: "http://localhost:8080/restaurant/filtered/",
        data: JSON.stringify(search), // Note it is important
        success: function (result) {
            // do what ever you want with data
        }
    });
}

