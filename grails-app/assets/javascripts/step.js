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

/**
* set event listeners on search field
*/
(() => {
  const searchElement = document.querySelector('#search');

  searchElement.addEventListener("change", displayMatchedResults);
  searchElement.addEventListener("keyup", displayMatchedResults);

  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
})();

const searchEndpoint = setEndpoint('search');
const relatedEndpoint = setEndpoint('related');
let fetchedSteps = [];

/**
* close any open result dropdown lists
*/
function closeAllLists(ele) {
    const parent = document.getElementById("search-results");
    const searchElement = document.querySelector('#search');
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
    const end = type == 'search' ? '/search' : '/getRelatedSteps';
    return sub + end;
}

/**
* fetch results from the server and display them
* 'this' refers to search input here
*/
async function displayMatchedResults() {

    const url = `${searchEndpoint}?q=${this.value}`;
    let encoded = encodeURI(url);

    if(this.value.length > 2) {
        const response = await fetch(encoded, {
            method: "GET",
        });
        const results = await response.json();

        const searchTerm = this.value;
        const htmlToDisplay = results
            .map((step) => {
                const regex = RegExp(searchTerm, "gi");
                const stepName = step.name.replace(regex, `<strong>${this.value}</strong>`);
                const html = `<li class='search-results-menu-item'
                            onclick='displayStepProperties(${step.id});'>
                        <span class='name'>${stepName}</span></li>`;
                return html;
            }).join("");

        const searchResults = document.querySelector("#search-results");
        if (searchTerm === "") {
            searchResults.innerHTML = "";
        } else {
            searchResults.innerHTML = htmlToDisplay;
        }
    }
}

/**
* fetch steps if not already in array
*/
async function fetchStep(id) {
    let s;
    if (fetchedSteps.some(e => e.step.id == id)) {
        s = fetchedSteps.find(e => e.step.id == id);
    } else {
        const url = `${relatedEndpoint}?stepId=${id}`;
        let encoded = encodeURI(url);
        const response = await fetch(encoded, {
            method: "GET",
        });
        s = await response.json();
        fetchedSteps.push(s);
    }
    return s;
}
/**
* adds suggested steps to the dom
*/
async function displayStepProperties(id) {

        //create act element
        const actCol = document.createElement('div');
        actCol.setAttribute('class', 'col-5');
        const actCard = document.createElement('div');
        actCard.setAttribute('class', 'card mb-2');
        actCol.appendChild(actCard);
        const actBody = document.createElement('div');
        actBody.setAttribute('class', 'card-body');
        actCard.appendChild(actBody);

        const s = await fetchStep(id);
        actBody.appendChild(document.createTextNode(s.step.act));

        //create result element
        const resCol = document.createElement('div');
        resCol.setAttribute('class', 'col-5');
        const resCard = document.createElement('div');
        resCard.setAttribute('class', 'card mb-2');
        resCol.appendChild(resCard);
        const resBody = document.createElement('div');
        resBody.setAttribute('class', 'card-body');
        resCard.appendChild(resBody);
        resBody.appendChild(document.createTextNode(s.step.result));

        //create hidden input
        const index = document.querySelector('#builderSteps').childElementCount;
        const hidden = document.createElement('input');
        hidden.setAttribute('style', 'display:none;');
        hidden.setAttribute('id', `steps[${index}].id`);
        hidden.setAttribute('name', `steps[${index}].id`);
        hidden.setAttribute('type', 'text');
        hidden.setAttribute('value', s.step.id);

        //create remove link
        const removeDiv = document.createElement('div');
        removeDiv.setAttribute('class', 'col-1');
        const removeInp = document.createElement('input');
        removeInp.setAttribute('class', 'btn btn-link btn-sm');
        removeInp.setAttribute('type', 'button');
        removeInp.setAttribute('value', 'Remove');
        removeInp.setAttribute('onclick', 'removeBuilderRow(this);');
        removeDiv.appendChild(removeInp);

        //create row and append elements
        const row = document.createElement('div');
        row.setAttribute('class', 'row align-items-center');
        row.appendChild(actCol);
        row.appendChild(resCol);
        row.appendChild(removeDiv);
        row.appendChild(hidden);

        //remove link
        if (document.querySelector('#builderSteps input[value=Remove]')) {
            const link = document.getElementById('builderSteps').lastChild.querySelector('input[value=Remove]');
            link.setAttribute('style', 'display:none;');
        }

        //append row to dom
        const parent = document.getElementById('builderSteps');
        parent.appendChild(row);

        document.getElementById('search').value = "";

        await displaySuggestedSteps(id);
}

/**
* displays the step that suggested steps are displayed for
*/
function setParentName(name) {
    //remove previous step name if present and display new step name
    const parentName = document.getElementById('suggestedName');
    if(parentName.firstChild) {
        parentName.firstChild.remove();
    }
    const prevStepName = document.createElement('p');
    prevStepName.appendChild(document.createTextNode(name));
    parentName.appendChild(prevStepName);
}

/**
* adds suggested steps to the dom
*/
async function displaySuggestedSteps(id) {

        //remove any displayed suggested steps
        const parent = document.getElementById('suggestedSteps');
        while (parent.firstChild) {
          parent.removeChild(parent.firstChild);
        }

        const s = await fetchStep(id);

        setParentName(s.step.name);

        if (s.relatedSteps.length == 0) {
            const emptyDiv = document.createElement('p');
            emptyDiv.appendChild(document.createTextNode('No related steps found'));
            parent.appendChild(emptyDiv);
        }
        //add all related steps to dom
        s.relatedSteps.forEach(step => {
            const col = document.createElement('div');
            col.setAttribute('class', 'col');
            col.setAttribute('onclick', `displayStepProperties(${step.id});`);
            const card = document.createElement('div');
            card.setAttribute('class', 'card mb-2');
            col.appendChild(card);
            const body = document.createElement('div');
            body.setAttribute('class', 'card-body');
            card.appendChild(body);
            body.appendChild(document.createTextNode(step.name));
            parent.appendChild(col);
        });
}

/**
* removes a row from builder steps
*/
function removeBuilderRow(element, id) {
    if(id) {
       const input = document.createElement('input');
       input.setAttribute('style', 'display:none;');
       input.setAttribute('data-test-id', 'step-removed-input');
       input.setAttribute('type', 'text');
       input.setAttribute('id', 'removedItems.stepIds');
       input.setAttribute('name', 'removedItems.stepIds');
       input.setAttribute('value', id);
       element.closest('#builderSteps').appendChild(input);
       element.closest('div.row').remove();
    } else {
       element.closest('div.row').remove();
    }
    //un-hide remove link and update suggested steps
    if (document.querySelector('#builderSteps input[value=Remove]')) {
        const link = document.getElementById('builderSteps').lastChild.querySelector('input[value=Remove]');
        link.removeAttribute('style');
        const previous = document.getElementById('builderSteps').lastChild.lastChild;
        const stepId = previous.value;
        displaySuggestedSteps(stepId);
    } else {
        resetForm('builder');
    }
}

/**
* resets step forms to avoid submitting builder and free-form step data
*/
function resetForm(type) {
    if(type == 'free') {
        if (document.getElementById('stepsTableContent').hasChildNodes()) {
            const steps = document.getElementById('stepsTableContent');
            while (steps.firstChild) {
                 steps.removeChild(steps.firstChild);
            }
        }
    } else {
        if (document.getElementById('builderSteps').hasChildNodes()) {
            const steps = document.getElementById('builderSteps');
            while (steps.firstChild) {
                steps.removeChild(steps.firstChild);
            }
        }
        if (document.getElementById('suggestedSteps')) {

            const suggested = document.getElementById('suggestedSteps');
            while (suggested.firstChild) {
                suggested.removeChild(suggested.firstChild);
            }
        }
        if (document.getElementById('suggestedName').firstChild) {
            document.getElementById('suggestedName').firstChild.remove();
        }
    }
}
//end builder