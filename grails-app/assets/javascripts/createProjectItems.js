/**
* create the pill element
*/
function getPill(text, type) {
    const onClick = type == 'area' ? "removeTag(this,'area')" : "removeTag(this,'environment')";
    return $(`<p class="ml-1 badge text-bg-light border">${text}<svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer"
                    width="16" height="16" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16" onclick=${onClick}>
                    <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                    </svg>
              </p>`);
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
        row.append(getPill(text, type));
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