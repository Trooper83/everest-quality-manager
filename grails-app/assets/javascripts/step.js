// free form start
function getEntryRow() {
    const row = $('<div class="row align-items-center mt-3"/>');

    const index = $('#stepsTableContent div.row').length;
    const removed = $('#stepsTableContent input[data-test-id=step-removed-input]').length;
    const itemIndex = index + removed;

    const action = $('<div class="col"><textarea class="form-control" type="text" maxLength="500" name="steps[' + itemIndex + '].act" value="" id="steps[' + itemIndex + '].act"/></div>');
    const data = $('<div class="col"><textarea class="form-control" type="text" maxLength="500" name="steps[' + itemIndex + '].data" value="" id="steps[' + itemIndex + '].data"/></div>');
    const result = $('<div class="col"><textarea class="form-control" type="text" maxLength="500" name="steps[' + itemIndex + '].result" value="" id="steps[' + itemIndex + '].result"/></div>');
    const button = $('<div class="col-md-1"><input class="btn btn-link btn-sm" type="button" value="Remove" onclick="removeEntryRow(this)" /></div>');

    row.append(action, data, result, button);
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

const searchEndpoint = setEndpoint('search');
const relatedEndpoint = setEndpoint('related');
let fetchedSteps = [];

/**
* set event listeners on search field
*/
(() => {
  const searchElement = document.querySelector('#search');

  if (searchElement) {
    searchElement.addEventListener("change", displayMatchedResults);
    searchElement.addEventListener("keyup", displayMatchedResults);

    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
  }

  const rows = document.querySelectorAll('#builderSteps .row');
  // get suggested steps if there are steps
  if (rows.length > 0) {
     const row = rows[rows.length - 1];
     const stepId = row.querySelector('input[data-name=hiddenId]').value;
     displaySuggestedSteps(stepId);
  }
})();

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
    const url = window.location.href.replace(/testCase|bug/, 'stepTemplate');
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
    const end = type == 'search' ? '/search' : '/getRelatedTemplates';
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
        if (response.ok) {
            const results = await response.json();

            const searchTerm = this.value;
            const htmlToDisplay = results
                .map((step) => {
                    const start = step.name.toUpperCase().indexOf(searchTerm.toUpperCase());
                    const end = searchTerm.length;
                    const stepName = step.name.substring(0, start) + '<b>' + step.name.substring(start, start + end) + '</b>' +
                        step.name.substring(start + end);
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
        } else {
            const toast = document.getElementById('error-toast');
            const t = new bootstrap.Toast(toast);
            t.show();
        }
    }
}

/**
* fetch steps if not already in array
*/
async function fetchStep(id) {
    let s;
    if (fetchedSteps.some(e => e.template.id == id)) {
        s = fetchedSteps.find(e => e.template.id == id);
    } else {
        const url = `${relatedEndpoint}?templateId=${id}`;
        let encoded = encodeURI(url);
        const response = await fetch(encoded, {
            method: "GET",
        });
        if (response.ok) {
            s = await response.json();
            fetchedSteps.push(s);
        } else {
            s = null;
        }
    }
    return s;
}
/**
* adds suggested steps to the dom
*/
async function displayStepProperties(id) {

        const s = await fetchStep(id);
        if (s) {

            const existing = document.querySelectorAll('div#stepsTableContent > div.row').length;
            const displayed = document.querySelectorAll('#builderSteps > div').length;
            const index = existing + displayed;

            const actCol = document.createElement('div');
            actCol.setAttribute('class', 'col');
            const act = document.createElement('textArea');
            act.setAttribute('class', 'form-control');
            act.setAttribute('readonly', 'true');
            act.setAttribute('id', `steps[${index}].act`);
            act.setAttribute('name', `steps[${index}].act`);
            act.setAttribute('style', 'min-height:3.5em; max-height:7em;');
            act.setAttribute('type', 'text');
            const actText = s.template.act == null ? '' : s.template.act
            act.appendChild(document.createTextNode(actText));
            act.setAttribute('value', actText);
            actCol.appendChild(act);

            //create data element
            const dataCol = document.createElement('div');
            dataCol.setAttribute('class', 'col');
            const data = document.createElement('textArea');
            data.setAttribute('class', 'form-control');
            data.setAttribute('style', 'min-height:3.5em; max-height:7em;');
            data.setAttribute('id', `steps[${index}].data`);
            data.setAttribute('name', `steps[${index}].data`);
            data.setAttribute('type', 'text');
            dataCol.appendChild(data);

            const resCol = document.createElement('div');
            resCol.setAttribute('class', 'col');
            const res = document.createElement('textArea');
            res.setAttribute('class', 'form-control');
            res.setAttribute('readonly', 'true');
            res.setAttribute('id', `steps[${index}].result`);
            res.setAttribute('name', `steps[${index}].result`);
            res.setAttribute('style', 'min-height:3.5em; max-height:7em;');
            res.setAttribute('type', 'text');
            const resText = s.template.result == null ? '' : s.template.result
            res.appendChild(document.createTextNode(resText));
            resCol.appendChild(res);

            const hiddenTemplate = document.createElement('input');
            hiddenTemplate.setAttribute('style', 'display:none;');
            hiddenTemplate.setAttribute('id', `steps[${index}].template`);
            hiddenTemplate.setAttribute('name', `steps[${index}].template`);
            hiddenTemplate.setAttribute('type', 'text');
            hiddenTemplate.setAttribute('data-name', 'hiddenId');
            hiddenTemplate.setAttribute('value', s.template.id);

            //create hidden builder step boolean
            const hiddenBoolean = document.createElement('input');
            hiddenBoolean.setAttribute('style', 'display:none;');
            hiddenBoolean.setAttribute('id', `steps[${index}].isBuilderStep`);
            hiddenBoolean.setAttribute('name', `steps[${index}].isBuilderStep`);
            hiddenBoolean.setAttribute('type', 'text');
            hiddenBoolean.setAttribute('data-name', 'hiddenBoolean');
            hiddenBoolean.setAttribute('value', 'true');

            //create remove link
            const removeDiv = document.createElement('div');
            const removeInp = document.createElement('input');
            removeInp.setAttribute('class', 'btn btn-link btn-sm');
            removeInp.setAttribute('type', 'button');
            removeInp.setAttribute('value', 'Remove');
            removeInp.setAttribute('onclick', 'removeBuilderRow(this);');
            removeDiv.appendChild(removeInp);

            //create row and append elements
            const row = document.createElement('div');
            row.setAttribute('class', 'row align-items-center mt-3');
            row.appendChild(actCol);
            row.appendChild(dataCol);
            row.appendChild(resCol);
            row.appendChild(removeDiv);
            row.appendChild(hiddenTemplate);
            row.appendChild(hiddenBoolean);

            //remove link
            if (document.querySelectorAll('#builderSteps .row').length > 0) {
                const rows = document.querySelectorAll('#builderSteps .row');
                const row = rows[rows.length - 1];
                const link = row.querySelector('input[value=Remove]');
                link.setAttribute('style', 'display:none;');
            }

            //append row to dom
            const parent = document.getElementById('builderSteps');
            parent.appendChild(row);

            document.getElementById('search').value = "";

            await displaySuggestedSteps(id);

        } else {
            const toast = document.getElementById('error-toast');
            const t = new bootstrap.Toast(toast);
            t.show();
        }
}

/**
* displays the step that suggested steps are displayed for
*/
function setParentName(name) {
    //remove previous step name if present and display new step name
    const parentName = document.getElementById('currentStep');
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

        if (s) {
            setParentName(s.template.name);
            if (s.relatedStepTemplates.length == 0) {
                const emptyDiv = document.createElement('p');
                emptyDiv.setAttribute('class', 'mt-3')
                emptyDiv.setAttribute('id', 'noStepsFound');
                emptyDiv.appendChild(document.createTextNode('No related steps found'));
                parent.appendChild(emptyDiv);
            }

            //add all related steps to dom
            s.relatedStepTemplates.forEach(template => {
                const col = document.createElement('div');
                col.setAttribute('class', 'col mt-3');
                col.setAttribute('onclick', `displayStepProperties(${template.id});`);
                const card = document.createElement('div');
                card.setAttribute('class', 'card mb-2 suggested');
                card.setAttribute('style', 'cursor:pointer');
                col.appendChild(card);
                const body = document.createElement('div');
                body.setAttribute('class', 'card-body');
                card.appendChild(body);
                const p = document.createElement('p');
                p.appendChild(document.createTextNode(template.name));
                body.appendChild(p);
                parent.appendChild(col);
            });
        }
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
       //un-hide remove link and update suggested steps
       if (document.querySelectorAll('#builderSteps .row').length > 0) {
           const rows = document.querySelectorAll('#builderSteps .row');
           const row = rows[rows.length - 1];
           row.querySelector('input[value=Remove]').removeAttribute('style');
           const stepId = row.querySelector('input[data-name=hiddenId]').value;
           displaySuggestedSteps(stepId);
       } else {
           resetForm();
       }
    } else {
       element.closest('div.row').remove();
       //un-hide remove link and update suggested steps
       if (document.querySelectorAll('#builderSteps .row').length > 0) {
           const rows = document.querySelectorAll('#builderSteps .row');
           const row = rows[rows.length - 1];
           row.querySelector('input[value=Remove]').removeAttribute('style');
           const stepId = row.querySelector('input[data-name=hiddenId]').value;
           displaySuggestedSteps(stepId);
       } else {
           resetForm();
       }
    }
}

/**
* resets step builder modal form
*/
function resetForm() {
    if (document.getElementById('builderSteps').hasChildNodes()) {
        const rows = document.querySelectorAll('#builderSteps .row');
        for (let row of rows) {
            row.remove();
        }
    }
    if (document.getElementById('suggestedSteps')) {
        const suggested = document.getElementById('suggestedSteps');
        while (suggested.firstChild) {
            suggested.removeChild(suggested.firstChild);
        }
    }
    if (document.getElementById('currentStep').firstChild) {
        document.getElementById('currentStep').firstChild.remove();
    }
}

/**
* appends steps from modal to steps table
*/
function appendBuilderSteps() {
    const eles = document.querySelectorAll('#builderSteps > div.row');
    if (eles.length > 0) {
        const links = document.querySelectorAll('#stepsTableContent input[value=Remove]');
        links.forEach(link => {
            link.setAttribute('style', 'display:none;');
        });
        const parent = document.querySelector('#stepsTableContent');
        eles.forEach(ele => {
            let e = ele.querySelector('input[value=Remove]');
            e.parentElement.setAttribute('class', 'col-md-1');
            e.setAttribute('onclick', 'removeEntryRow(this);')
            parent.append(ele);
        });
    }
}
//end builder