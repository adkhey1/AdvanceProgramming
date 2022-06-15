function rescticteAlphabet(e) {


    var x = e.which || e.keycode;
    if ((x >= 48 && x <= 57))
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
        url: "http://localhost:8080/restaurant/filtered/",
        data: JSON.stringify(search), // Note it is important
        success: function (result) {
            // do what ever you want with data
        }
    });
}


function display() {
    console.log(states);
}




async function fillDropdown() {
    console.log("Check1");

    //let btnPopulate = document.querySelector('button');
    let select = document.querySelector('select');
    //let fruits = ['Banana', 'Grapes', 'Kiwi', 'Mango', 'Orange'];
    let fruits = states;

    let options = fruits.map(fruit => `<option value=${fruit.toLowerCase()}>${fruit}</option>`).join('\n');
    document.getElementById("state").innerHTML = options;
}





