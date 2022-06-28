(function () {
    "use strict";
    let inputField = document.getElementById('category');
    let ulField = document.getElementById('suggestionsKa');
    inputField.addEventListener('input', changeAutoComplete);
    ulField.addEventListener('click', selectItem);

    function changeAutoComplete({target}) {
        let data = target.value;
        ulField.innerHTML = ``;
        if (data.length) {
            let autoCompleteValues = autoComplete(data);
            autoCompleteValues.forEach(value => {
                addItem(value);
            });
        }
    }

    function autoComplete(inputValue) {
        let destination = categories;
        let test = destination.filter(

            (value) => value.toLowerCase().includes(inputValue.toLowerCase())

        );

        const slicedArray = test.slice(0, 5);
         return slicedArray;
    }

    function addItem(value) {
        ulField.innerHTML = ulField.innerHTML + `<li>${value}</li>`;
    }

    function selectItem({target}) {
        if (target.tagName === 'LI') {
            inputField.value = target.textContent;
            ulField.innerHTML = ``;
        }
    }
})();


(function () {
    "use strict";
    let inputField = document.getElementById('attribute');
    let ulField = document.getElementById('suggestionsAt');
    inputField.addEventListener('input', changeAutoComplete);
    ulField.addEventListener('click', selectItem);

    function changeAutoComplete({target}) {
        let data = target.value;
        ulField.innerHTML = ``;
        if (data.length) {
            let autoCompleteValues = autoComplete(data);
            autoCompleteValues.forEach(value => {
                addItem(value);
            });
        }
    }

    function autoComplete(inputValue) {
        // let destination = ["Italy", "Spain", "Portugal", "Brazil"];
        let destination = attributes;
        let test = destination.filter(
            (value) => value.toLowerCase().includes(inputValue.toLowerCase())
        );


        const slicedArray = test.slice(0, 5);
        return slicedArray;
    }

    function addItem(value) {
        ulField.innerHTML = ulField.innerHTML + `<li>${value}</li>`;
    }

    function selectItem({target}) {
        if (target.tagName === 'LI') {
            inputField.value = target.textContent;
            ulField.innerHTML = ``;
        }
    }
})();



