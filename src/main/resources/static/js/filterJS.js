function rescticteAlphabet(e) {


    var x = e.which || e.keycode;
    if ((x >= 48 && x <= 57))
        return true;
    else
        return false;
}

/*
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
            loadFiltered(result)
            // do what ever you want with data
        }
    });
}



 */
function display() {
    console.log(states);
}


async function fillDropdown() {
    console.log("Check1");

    let select = document.querySelector('select');
    let fruits = states;
    let options = fruits.map(fruit => `<option value=${fruit.toLowerCase()}>${fruit}</option>`).join('\n');
    let optionNone = document.createElement('option');
    optionNone.innerHTML = "all";
    optionNone.value = "";
    document.getElementById("state").innerHTML = options;
    document.getElementById("state").appendChild(optionNone)
}





