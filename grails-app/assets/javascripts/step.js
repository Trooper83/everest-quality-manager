// free form start
function getEntryRow() {
    const row = $('<div class="row align-items-center mt-3"/>');

    const index = $('#stepsTableContent input.iHidden').length;
    const removed = $('#stepsTableContent input[data-test-id=step-removed-input]').length;
    const itemIndex = index + removed;

    const hidden = $('<input type="hidden" name="stepsIndex[' + itemIndex + ']" class="iHidden" value="" id="steps[' + itemIndex + ']"/>');
    const action = $('<div class="col-5"><textarea class="form-control" type="text" maxLength="500" name="steps[' + itemIndex + '].act" value="" id="steps[' + itemIndex + '].act"/></div>');
    const result = $('<div class="col-5"><textarea class="form-control" type="text" maxLength="500" name="steps[' + itemIndex + '].result" value="" id="steps[' + itemIndex + '].result"/></div>');
    const button = $('<div class="col-2"><input class="btn btn-link btn-sm" type="button" value="Remove" onclick="removeEntryRow(this)" /></div>');

    row.append(hidden, action, result, button);
    return row;
}

function addEntryRow() {
    $('#stepsTableContent div').find('input[value=Remove]').last().hide();
    let row = getEntryRow();
    $('#stepsTableContent').append(row);
    $('#stepsTableContent div').find('textarea[name$=act]').last().focus();
}

function removeEntryRow(element, id) {
    if(id) {
       let input = $('<input style="display: none;" data-test-id="step-removed-input" type="text" id="removedItems.stepIds" name="removedItems.stepIds" value="' + id + '" />');
       $(element).parent().parent().parent().append(input);
       $(element).parent().parent().remove();
    } else {
       $(element).parent().parent().remove();
    }
    $('#stepsTableContent div').find('input[value=Remove]').last().show();
}
//end free form

//start builder
const searchElement = document.querySelector('#search');

searchElement.addEventListener("change", displayMatchedResults);
searchElement.addEventListener("keyup", displayMatchedResults);

document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});

/**
* close any open result dropdown lists
*/
function closeAllLists(ele) {
    const parent = document.getElementById("search-results");
    if (ele != searchElement) {
        while (parent.hasChildNodes()) {
            parent.firstChild.remove()
        }
    }
    searchElement.value = "";
}

/**
* sets the endpoint
*/
function setEndpoint(type) {
    const url = window.location.href.replace(/testCase|bug/, 'step');
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
    const end = type == 'search' ? '/search' : '/getRelatedSteps'
    return sub + end;
}

const searchEndpoint = setEndpoint('search');
const relatedEndpoint = setEndpoint('related');
let suggestions = [];
let steps = [];
/**
* fetch results from the server and display them
*/
function displayMatchedResults() {

    const url = `${searchEndpoint}?q=${this.value}`;
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
                const html = `<li class='search-results-menu-item'
                            onclick='displaySuggestedSteps(this,${step.id}); displayStepProperties(this,${step.id});'>
                        <span class='name'>${stepName}</span></li>`;
                return html;
            }).join("");

        const searchResults = document.querySelector("#search-results");
        if (searchTerm === "") {
            searchResults.innerHTML = "";
        } else {
            searchResults.innerHTML = htmlToDisplay;
        }
        suggestions.length = 0;
    }
}

//TODO: issue is how to get the step action and result from the suggested step when the user uses the search function??
/**
* adds suggested steps to the dom
*/
function displayStepProperties(element, id) {

        const actCol = document.createElement('div');
        actCol.setAttribute('class', 'col-6');
        const actCard = document.createElement('div');
        actCard.setAttribute('class', 'card mb-2');
        actCol.appendChild(actCard);
        const actBody = document.createElement('div');
        actBody.setAttribute('class', 'card-body');
        actCard.appendChild(actBody);

        const step = steps.find(e => e.id == id);
        actBody.appendChild(document.createTextNode(step.act));

        const resCol = document.createElement('div');
        resCol.setAttribute('class', 'col-6');
        const resCard = document.createElement('div');
        resCard.setAttribute('class', 'card mb-2');
        resCol.appendChild(resCard);
        const resBody = document.createElement('div');
        resBody.setAttribute('class', 'card-body');
        resCard.appendChild(resBody);
        resBody.appendChild(document.createTextNode(step.result));

        const row = document.createElement('div');
        row.setAttribute('class', 'row');
        row.appendChild(actCol);
        row.appendChild(resCol);

        const parent = document.getElementById('builderSteps');
        parent.appendChild(row);

        document.getElementById('search').value = "";
}

/**
* adds suggested steps to the dom
*/
async function displaySuggestedSteps(element, id) {

        const parent = document.getElementById('suggestedSteps');
        while (parent.firstChild) {
          parent.removeChild(parent.firstChild);
        }

        const url = `${relatedEndpoint}?stepId=${id}`;
        let encoded = encodeURI(url);

        const response = await fetch(encoded, {
            method: "GET",
        });
        steps = await response.json();

        steps.forEach( step => {
            const col = document.createElement('div');
            col.setAttribute('class', 'col');
            col.setAttribute('onclick', `displayStepProperties(this, ${step.id}); displaySuggestedSteps(this, ${step.id});`);
            const card = document.createElement('div');
            card.setAttribute('class', 'card mb-2');
            col.appendChild(card);
            const body = document.createElement('div');
            body.setAttribute('class', 'card-body');
            card.appendChild(body);
            body.appendChild(document.createTextNode(step.name));
            parent.appendChild(col);
        });

        steps.length = 0;
}
//end builder