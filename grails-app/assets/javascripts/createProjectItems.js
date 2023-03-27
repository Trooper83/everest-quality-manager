/**
* create the pill element
*/
function getPill(text, type) {
    const onClick = type == 'area' ? "displayOptions(this,'area')" : "displayOptions(this,'environment')";
    return $(`<h3><p class="font-weight-normal badge badge-light border border-secondary border-rounded">${text}<svg xmlns="http://www.w3.org/2000/svg"
    style="cursor:pointer" width="12" height="12"
                        fill="currentColor" class="bi bi-pencil ml-1" viewBox="0 0 20 20" onclick=${onClick}>
                            <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                        </svg>
                   </p></h3>`);
}

/**
* create the button group
*/
function getButtonGroup(type) {
    return $(`<div class="btn-group btn-group-sm" role="group" aria-label="edit buttons">
        <button class="btn btn-link" data-test-id="remove-tag-button" type="button" onclick="removeTag(this,'${type}')">Remove</button>
        <button class="btn btn-link" data-test-id="edit-tag-button" type="button" onclick="editTag(this,'${type}')">Edit</button>
        </div>`);
}

/**
* adds a tag
*/
function addTag(type) {
    let itemIndex = $(`#${type}s li`).length;
    let text = $(`#${type}`).val();
    if(text.trim().length > 0) {
        let element = $('<li class="list-inline-item"/>');
        let row = $('<div class="form-row ml-1"></div>');
        let hiddenElement = $(`<input class="form-control-sm mt-3" type="text" data-test-id="tag-input" data-id="${type}"
            style="display: none;" name="${type}s[${itemIndex}].name" id="${type}s[${itemIndex}].name" value="${text}"
            data-toggle="tooltip" trigger="manual" data-original-title="Name cannot be blank"/>`);
        element.attr('name', text);
        row.append(getPill(text, type), hiddenElement);
        element.append(row);
        $(`#${type}`).val('');
        $(`#${type}s ul`).append(element);
    } else {
        let element = $(`[name=${type}]`);
        element.tooltip('show');
        setTimeout(function(){
            element.tooltip('dispose');
        }, 2000);
    }
}

/**
* displays edit and delete buttons
*/
function displayOptions(element, type) {
    let visGroup = $('div.btn-group:visible');
    if(visGroup.length > 0) {
        visGroup.remove();
    }
    $(element).parents('div.form-row').append(getButtonGroup(type));

    // adds a click handler to cancel edit if clicked outside input
      $(document).on('click', function(event) {
         let ele = $('div.btn-group:visible');
         if($(event.target).is('svg, path')) return;
         if(ele.length < 1) return;
         if(!ele.is(event.target)) {
             ele.remove();
             $(document).off('div[role=main] form');
         }
      });
}

/**
* enables the edit tag elements
*/
function editTag(button, type) {
    let visInput = $('input[data-test-id=tag-input]:visible');
    if(visInput.length > 0) { // cancel other edit if already visible
        let cancel = visInput.siblings('[data-test-id=cancel-tag-button]');
        cancelTag(cancel);
    }
    let ele = $(button).parents("div.form-row");
    let input = ele.children('input[type="text"]');
    input.show();
    input.focus();
    ele.children('h3').remove();
    ele.children('.btn-group').remove();
    ele.append(`<input class="btn btn-primary btn-sm border mt-3" type="button" data-test-id="save-tag-button" value="Save" onclick="saveTag(this, '${type}')"/>`);
    ele.append(`<input class="btn btn-light btn-sm border mt-3" type="button" data-test-id="cancel-tag-button" value="Cancel" onclick="cancelTag(this, '${type}')"/>`);

    // adds a click handler to cancel edit if clicked outside input
    $(document).on('click', function(event) {
        let ele = $('input[data-test-id=tag-input]:visible');
        if($(event.target).is('button')) return;
        if(ele.length < 1) return;
        if(!ele.is(event.target)) {
            let text = ele.parent().parent().attr('name');
            let entity = ele.attr('data-id');
            ele.val(text);
            ele.parent().append(getPill(text, entity));
            ele.siblings('input[data-test-id="save-tag-button"]').remove();
            ele.siblings('input[data-test-id="cancel-tag-button"]').remove();
            ele.hide();
            $(document).off('div[role=main] form');
        }
    });
}

/**
* removes a tag
*/
function removeTag(element, type) {
    const entity = `${type}s`;
    $(element).parents('li').remove();
    const items = document.querySelectorAll(`#${entity} input[data-test-id=tag-input]`);
    items.forEach((item, index) => {
        item.setAttribute('name', `${entity}[${index}].name`);
        item.setAttribute('id', `${entity}[${index}].id`);
    });
}

/**
* cancels a tag
*/
function cancelTag(element, type) {
    let ele = $(element);
    let input = ele.siblings('input[type="text"]');
    let text = ele.parent().parent().attr('name');
    input.val(text);
    ele.parent().append(getPill(text, type));
    ele.siblings('input[data-test-id="save-tag-button"]').remove();
    ele.remove();
    input.hide();
}

/**
* saves a tag
*/
function saveTag(element, type) {
    let ele = $(element);
    let input = ele.siblings('input[type="text"]');
    let text = input.val();
    if(text.trim().length > 0) {
        input.attr('value', text);
        ele.parent().parent().attr('name', text);
        ele.parent().append(getPill(text, type));
        ele.siblings('input[data-test-id="cancel-tag-button"]').remove();
        ele.remove();
        input.hide();
    } else {
        input.tooltip('show');
        setTimeout(function(){
           input.tooltip('dispose');
        }, 2000);
    }
}