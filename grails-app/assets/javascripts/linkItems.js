
(() => {
  const searchElement = document.querySelector('#search');

  searchElement.addEventListener("change", displayMatchedResults);
  searchElement.addEventListener("keyup", displayMatchedResults);

  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
})();

const suggestions = [];
const endpoint = setEndpoint();

/**
* close any open result dropdown lists
*/
function closeAllLists(elmnt) {
    const parent = document.getElementById("search-results");
    const searchElement = document.querySelector('#search');
    if (elmnt != searchElement) {
        while (parent.hasChildNodes()) {
            parent.firstChild.remove()
        }
    }
}

/**
* sets the endpoint
*/
function setEndpoint() {
    const url = window.location.href;
    let sub;
    if(url.endsWith('create') || url.endsWith('save')) {
        const last = url.lastIndexOf('/');
        sub = url.substring(0, last);
    } else if (url.includes('update')) {
        const last = url.lastIndexOf('/update');
        sub = url.substring(0, last);
    } else {
        const last = url.lastIndexOf('/edit');
        sub = url.substring(0, last);
    }
    return sub + '/search';
}

/**
* fetch results from the server and display them
*/
function displayMatchedResults() {

    const searchResults = document.querySelector("#search-results");
    const searchElement = document.querySelector('#search');

    const url = `${endpoint}?q=${this.value}`;
    let encoded = encodeURI(url);

    if(this.value.length > 2) {
        fetch(encoded, {
            method: "GET",
        })
        .then((response) => {
            if (!response.ok) {
              throw new Error('Server returned ' + response.status);
            }
            return response;
        })
        .then((blobdata) => blobdata.json())
        .then((data) => suggestions.push(...data))
        .then(() => {
            const searchTerm = this.value;
            const htmlToDisplay = suggestions
                .map((step) => {
                    const regex = RegExp(searchTerm, "gi");
                    const stepName = step.name.replace(regex, `<strong>${this.value}</strong>`);
                    const html = `<li class='search-results-menu-item' data-id='${step.id}'><span class='name'>${stepName}</span></li>`;
                    return html;
                }).join("");

            if (searchTerm === "") {
                searchResults.innerHTML = "";
            } else {
                searchResults.innerHTML = htmlToDisplay;
            }
            suggestions.length = 0;

            const searchItems = document.getElementsByClassName('search-results-menu-item');
            for (const item of searchItems) {
                item.addEventListener("click", function(e) {
                    searchElement.value = item.innerText;
                    searchElement.setAttribute('data-id', item.getAttribute('data-id'));
                });
            }
        })
        .catch(error => {
          console.error('There was a problem with the Fetch operation:', error);
          const toast = document.getElementById('error-toast');
          const t = new bootstrap.Toast(toast);
          t.show();
        });
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

        const i = document.getElementById('parents').children.length + document.getElementById('siblings').children.length +
            document.getElementById('children').children.length;

        const col = document.createElement('div');
        col.setAttribute('class', 'col');
        const card = document.createElement('div');
        card.setAttribute('class', 'card mb-2');
        col.appendChild(card);
        const body = document.createElement('div');
        body.setAttribute('class', 'card-body');
        body.appendChild(buildCloseButton());
        card.appendChild(body);

        body.appendChild(createCardText('Name', stepName));

        body.appendChild(createHiddenInput('linkedId', stepId, i));
        body.appendChild(createHiddenInput('relation', relation, i));

        let eleId = '';
        if(relation == 'Is Child of') {
            eleId = 'parents';
        } else if (relation == 'Is Sibling of') {
            eleId = 'siblings';
        } else {
            eleId = 'children';
        }
        const parent = document.getElementById(eleId);
        parent.appendChild(col);

        searchInput.value = "";
        searchInput.removeAttribute('data-id');
        document.getElementById('relation').value = "";
    }
}

function buildCloseButton() {
    const iconSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    const iconPath = document.createElementNS('http://www.w3.org/2000/svg','path');

    iconSvg.setAttribute('onclick', 'removeLink(this)');
    iconSvg.setAttribute('style', 'cursor:pointer');
    iconSvg.setAttribute('width', '20');
    iconSvg.setAttribute('height', '20');
    iconSvg.setAttribute('fill', 'currentColor');
    iconSvg.setAttribute('class', 'bi bi-x position-absolute top-0 end-0 mt-1');
    iconSvg.setAttribute('viewBox', '0 0 20 20');
    iconPath.setAttribute('d', 'M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z');
    iconSvg.appendChild(iconPath);
    return iconSvg;
}

/**
* create the text for a linked item's card
*/
function createCardText(type, text) {
    const ele = document.createElement('p');
    ele.setAttribute('data-test-id', `linkedItem${type}`);
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

/**
* removes a linked step
*/
function removeLink(element, id) {
    if(id) {
        const input = document.createElement('input');
        input.setAttribute('style', 'display:none;');
        input.setAttribute('data-test-id', 'removed-tag-input');
        input.setAttribute('type', 'text');
        input.setAttribute('id', 'removedItems.linkIds');
        input.setAttribute('name', 'removedItems.linkIds');
        input.setAttribute('value', id);
        document.getElementById('links').appendChild(input);
        element.parentElement.parentElement.parentElement.remove();
    } else {
        element.parentElement.parentElement.parentElement.setAttribute('style', 'display:none;')
        element.parentElement.parentElement.remove();
    }
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