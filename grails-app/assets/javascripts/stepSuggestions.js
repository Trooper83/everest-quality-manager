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

        const i = document.getElementById('stepLinks').children.length;

        const ele = document.createElement('p');
        const text = document.createTextNode(stepName);
        ele.appendChild(text);

        const idInp = document.createElement('input');
        idInp.setAttribute('value', stepId);
        idInp.setAttribute('id', `links[${i}].id`);
        idInp.setAttribute('name', `links[${i}].id`);
        idInp.setAttribute('type', 'text');
        idInp.setAttribute('style', 'display:none;');

        const relationInp = document.createElement('input');
        relationInp.setAttribute('value', relation);
        relationInp.setAttribute('id', `links[${i}].relation`);
        relationInp.setAttribute('name', `links[${i}].relation`);
        relationInp.setAttribute('type', 'text');
        relationInp.setAttribute('style', 'display:none;');

        ele.appendChild(idInp);
        ele.appendChild(relationInp);

        const parent = document.getElementById('stepLinks');
        parent.appendChild(ele);

        searchInput.value = "";
        document.getElementById('relation').value = "";
    }
}

function removeLink() {
  //TODO: need to remove elements and hide the parent to keep the index correct
}

function showTooltip(id) {
    let element = $(`#${id}`);
    element.tooltip('show');
    setTimeout(function(){
        element.tooltip('dispose');
    }, 2000);
}

//https://www.w3schools.com/howto/howto_js_autocomplete.asp