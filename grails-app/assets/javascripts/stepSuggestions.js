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

function setEndpoint() {
    const url = window.location.href;
    const last = url.lastIndexOf('/');
    let sub = url.substring(0, last);
    return sub + '/getSteps';
}

const endpoint = setEndpoint();

function displayMatchedResults() {

    /*if (!window.location.href.endsWith('getSteps')) {
        setEndpoint();
    }*/
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

    if (relation.length == 0 && stepName.length == 0) {
        showTooltip('relation');
        showTooltip('linkedSteps');
    } else if (relation.length == 0) {
        showTooltip('relation');
    } else if (stepName.length == 0) {
        showTooltip('linkedSteps');
    } else {
        const parent = document.getElementById('stepLinks');
        const inp = document.createElement('input');
        const text = document.createTextNode(stepName);
        inp.appendChild(text);
        inp.setAttribute('data-id', stepId);
        inp.setAttribute('value', stepId);
        inp.setAttribute('id', 'link[0]'); //TODO: get this value dynamically and set input to hidden
        inp.setAttribute('name', 'link[0]');
        inp.setAttribute('type', 'text');
        parent.appendChild(inp);
        searchInput.value = "";
        document.getElementById('relation').value = "";
    }
}

function showTooltip(id) {
    let element = $(`#${id}`);
    element.tooltip('show');
    setTimeout(function(){
        element.tooltip('dispose');
    }, 2000);
}

//https://www.w3schools.com/howto/howto_js_autocomplete.asp