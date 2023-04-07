/**
* adds an env tag
*/
function addEnvTag() {
    let itemIndex = $('#environments li').length;
    let text = $('#environment').val().trim();
    if(text.length > 0) {
        let element = $('<li class="list-inline-item"/>');
        let row = $('<div class="form-row ml-1"></div>');
        let hiddenElement = $('<input class="form-control-sm" type="text" data-test-id="tag-input" style="display: none;" name="environments[' +
                itemIndex + '].name" id="environments[' + itemIndex + '].name" value="' + text + '"' +
                'data-toggle="tooltip" trigger="manual" data-original-title="Environment Name cannot be blank"/>');
        let pill = $(`<h3><p class="font-weight-normal badge badge-light border border-secondary border-rounded">${text}<svg xmlns="http://www.w3.org/2000/svg"
                style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil ml-1" viewBox="0 0 20 20" onclick="displayEnvOptions(this)">
                <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                </svg>
                </p></h3>`);
        element.attr('name', text);
        row.append(pill);
        element.append(row);
        $('#environment').val('');
        $('#environmentRow ul').append(element);
    } else {
        let element = $("[name='environment']");
        element.tooltip('show');
        setTimeout(function(){
            element.tooltip('dispose');
        }, 2000);
    }
}

/**
* removes a tag
*/
function removeEnvTag(element, id) {
    if(id) {
        let input = $('<input style="display: none;" data-test-id="removed-tag-input" type="text" id="removedItems.environmentIds" name="removedItems.environmentIds" value="' + id + '" />');
        $(element).parents('ul').append(input);
        $(element).parents('li').hide();
    } else {
        const parent = $(element).parents('li');
        parent.children().remove();
        parent.hide();
    }
}