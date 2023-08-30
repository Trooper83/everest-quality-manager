const endpoint = window.location.href.replace('create', 'getSteps');;

const searchInput = document.querySelector('#linkedSteps');
const card = document.querySelector("#search-results");

searchInput.addEventListener("change", displayMatchedResults);
searchInput.addEventListener("keyup", displayMatchedResults);

document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});

function closeAllLists(elmnt) {
    var parent = document.getElementById("search-results");
    if (elmnt != searchInput) {
        while (parent.hasChildNodes()) {
            parent.firstChild.remove()
        }
    }
}

const suggestions = [];

function displayMatchedResults() {

    const url = `${endpoint}?q=${this.value}`;
    let encoded = encodeURI(url);

    if(this.value.length > 2) {
        fetch(encoded, {
            method: "GET",
        })
        .then((blobdata) => blobdata.json())
        .then((data) => suggestions.push(...data));

        const searchTerm = this.value;
        const htmlToDisplay = suggestions
            .map((step) => {
                const regex = RegExp(searchTerm, "gi");
                const stepName = step.name.replace(regex, `<strong>${this.value}</strong>`);
                const html = `<li class='search-item' data-id='${step.id}'><span class='name'>${stepName}</span></li>`;
                return html;
            }).join("");

        if (searchTerm === "") {
            card.innerHTML = "";
        } else {
            card.innerHTML = htmlToDisplay;
        }
        suggestions.length = 0;

        const searchItems = document.getElementsByClassName('search-item');
        for (const item of searchItems) {
            item.addEventListener("click", function(e) {
                searchInput.value = item.innerText;
                searchInput.setAttribute('data-id', item.getAttribute('data-id'));
            });
        }
    }
}

function addLink(element) {
    const relation = document.getElementById('relation').value;
    const stepName = document.getElementById('linkedSteps').value;
    const stepId = document.getElementById('linkedSteps').getAttribute('data-id');

    const parent = document.getElementById('stepLinks');
}

//https://www.w3schools.com/howto/howto_js_autocomplete.asp