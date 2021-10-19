/**
* adds an area tag
*/
function addArea() {
    let itemIndex = $('#areas li').length;
    let text = $('#area').val();
    if(text) {
        let element = $('<li/>');
        let hiddenElement = $('<input type="text" data-test-id="create-area-tag-input" style="display: none;" name="areas[' +
                itemIndex + '].name" id="areas[' + itemIndex + '].name" value="' + text + '"/>');
        let removeButton = $('<input data-test-id="create-remove-area-button" type="button" value="x" onclick="removeAreaElement(this)"/>');
        let editButton = $('<input data-test-id="create-edit-area-button" type="button" value="y" onclick="editAreaElement(this)"/>');
        let span = $('<span>' + text + '</span>')
        element.attr('name', text);
        element.append(removeButton, editButton, span, hiddenElement);
        $('#area').val('');
        $('#areas ul').append(element);
    }
}

/**
* enables the edit area tag elements
*/
function editAreaElement(button) {
    let ele = $(button);
    ele.siblings('input[type="text"]').show();
    ele.siblings('span').remove();
    ele.parent().append('<input type="button" data-test-id="create-area-save-button" value="Save" onclick="saveAreaElement(this)"/>')
    ele.remove();
}

/**
* removes an area tag
*/
function removeAreaElement(button) {
    $(button).parent().remove();
}

/**
* saves an area tag edit
*/
function saveAreaElement(button) {
    let ele = $(button);
    let input = ele.siblings('input[type="text"]');
    let text = input.val();
    ele.parent().append('<input data-test-id="create-edit-area-button" type="button" value="y" onclick="editAreaElement(this)"/>');
    ele.parent().append('<span>' + text + '</span>');
    ele.parent().attr('name', text);
    ele.remove();
    input.hide();
}