const searchElement = document.querySelector('#search');
const searchResults = document.querySelector("#search-results");

searchElement.addEventListener("change", displayMatchedResults);
searchElement.addEventListener("keyup", displayMatchedResults);

document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});

/**
* close any open result dropdown lists
*/
function closeAllLists(elmnt) {
    const parent = document.getElementById("search-results");
    if (elmnt != searchElement) {
        while (parent.hasChildNodes()) {
            parent.firstChild.remove()
        }
    }
}

const suggestions = [];

/**
* sets the endpoint
*/
function setEndpoint() {
    const url = window.location.href;
    const last = url.lastIndexOf('/');
    let sub = url.substring(0, last);
    return sub + '/search';
}

const endpoint = setEndpoint();

/**
* fetch results from the server and display them
*/
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
            searchResults.innerHTML = "";
        } else {
            searchResults.innerHTML = htmlToDisplay;
        }
        suggestions.length = 0;

        const searchItems = document.getElementsByClassName('search-item');
        for (const item of searchItems) {
            item.addEventListener("click", function(e) {
                searchElement.value = item.innerText;
                searchElement.setAttribute('data-id', item.getAttribute('data-id'));
            });
        }
    }
}

/**
* add a linked item to the dom
*/
function addLinkItem(element) {

    const relation = document.getElementById('relation').value;
    const searchInput = document.getElementById('search');
    const stepName = searchInput.value;
    const stepId = searchInput.getAttribute('data-id');

    if (relation.length == 0 && stepName.length == 0) {
        showTooltip('relation');
        showTooltip('search');
    } else if (relation.length == 0) {
        showTooltip('relation');
    } else if (stepName.length == 0) {
        showTooltip('search');
    } else if (!searchInput.getAttribute('data-id')) {
        if (document.getElementById('validate').children.length == 0) {
            const ele = document.createElement('div');
            ele.setAttribute('class', 'text-danger');
            ele.appendChild(document.createTextNode('A step from the Name field options must be selected'));
            document.getElementById('validate').appendChild(ele);
        }
    } else {

        const validationChildren = document.getElementById('validate').children;
        if (validationChildren.length > 0) {
            validationChildren[0].remove();
        }

        const i = document.getElementById('linkedSteps').children.length;

        const col = document.createElement('div');
        col.setAttribute('class', 'col');
        const card = document.createElement('div');
        card.setAttribute('class', 'card mb-3');
        col.appendChild(card);
        const body = document.createElement('div');
        body.setAttribute('class', 'card-body');
        card.appendChild(body);

        body.appendChild(createCardText('Name', stepName));
        body.appendChild(createCardText('Relation', relation));

        body.appendChild(createHiddenInput('linkedId', stepId, i));
        body.appendChild(createHiddenInput('relation', relation, i));

        const parent = document.getElementById('linkedSteps');
        parent.appendChild(col);

        searchInput.value = "";
        searchInput.removeAttribute('data-id');
        document.getElementById('relation').value = "";
    }
}

/**
* create the text for a linked item's card
*/
function createCardText(type, text) {
    const ele = document.createElement('p');
    ele.setAttribute('class', 'mb-2')
    const title = document.createElement('strong');
    title.appendChild(document.createTextNode(`${type}: `));
    ele.appendChild(title);
    ele.appendChild(document.createTextNode(text));
    return ele;
}

/**
* create a hidden input for a linked item
* this will be the data that is submitted via the form
*/
function createHiddenInput(type, value, index) {
    const inp = document.createElement('input');
    inp.setAttribute('value', value);
    inp.setAttribute('id', `links[${index}].${type}`);
    inp.setAttribute('name', `links[${index}].${type}`);
    inp.setAttribute('type', 'text');
    inp.setAttribute('style', 'display:none;');
    return inp;
}

function removeLink() {
  //TODO: need to remove elements and hide the parent to keep the index correct
}

/**
* show a tooltip and dispose after 2 seconds
*/
function showTooltip(id) {
    let element = $(`#${id}`);
    element.tooltip('show');
    setTimeout(function(){
        element.tooltip('dispose');
    }, 2000);
}

//https://www.w3schools.com/howto/howto_js_autocomplete.asp