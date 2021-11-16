/**
* adds an area tag
*/
function addAreaTag() {
    let itemIndex = $('#areas li').length;
    let text = $('#area').val();
    if(text) {
        let element = $('<li/>');
        let hiddenElement = $('<input type="text" data-test-id="tag-input" style="display: none;" name="areas[' +
                itemIndex + '].name" id="areas[' + itemIndex + '].name" value="' + text + '"' +
                'data-toggle="tooltip" trigger="manual" title="Area Name cannot be blank"/>');
        let removeButton = $('<input data-test-id="remove-tag-button" type="button" value="x" onclick="removeAreaTag(this)"/>');
        let editButton = $('<input data-test-id="edit-tag-button" type="button" value="y" onclick="editTag(this)"/>');
        let span = $('<span>' + text + '</span>')
        element.attr('name', text);
        element.append(removeButton, editButton, span, hiddenElement);
        $('#area').val('');
        $('#areas ul').append(element);
    } else {
        let element = $("[name='area']");
        element.tooltip('show');
        setTimeout(function(){
            element.tooltip('dispose');
        }, 2000);
    }
}

/**
* adds an env tag
*/
function addEnvironmentTag() {
    let itemIndex = $('#environments li').length;
    let text = $('#environment').val();
    if(text) {
        let element = $('<li/>');
        let hiddenElement = $('<input type="text" data-test-id="tag-input" style="display: none;" name="environments[' +
                itemIndex + '].name" id="environments[' + itemIndex + '].name" value="' + text + '"' +
                'data-toggle="tooltip" trigger="manual" title="Environment Name cannot be blank"/>');
        let removeButton = $('<input data-test-id="remove-tag-button" type="button" value="x" onclick="removeEnvironmentTag(this)"/>');
        let editButton = $('<input data-test-id="edit-tag-button" type="button" value="y" onclick="editTag(this)"/>');
        let span = $('<span>' + text + '</span>')
        element.attr('name', text);
        element.append(removeButton, editButton, span, hiddenElement);
        $('#environment').val('');
        $('#environments ul').append(element);
    } else {
        let element = $("[name='environment']");
        element.tooltip('show');
        setTimeout(function(){
            element.tooltip('dispose');
        }, 2000);
    }
}

/**
* enables the edit tag elements
*/
function editTag(button) {
    let ele = $(button);
    ele.siblings('input[type="text"]').show();
    ele.siblings('span').remove();
    ele.parent().append('<input type="button" data-test-id="save-tag-button" value="Save" onclick="saveTag(this)"/>')
    ele.remove();
}

/**
* removes an area tag
*/
function removeAreaTag(element, id) {
    if(id) {
        let input = $('<input style="display: none;" data-test-id="removed-tag-input" type="text" id="removedItems.areaIds" name="removedItems.areaIds" value="' + id + '" />');
        $(element).parent().parent().append(input);
        $(element).parent().remove();
    } else {
        $(element).parent().remove();
    }
}

/**
* removes an env tag
*/
function removeEnvironmentTag(element, id) {
    if(id) {
        let input = $('<input style="display: none;" data-test-id="removed-tag-input" type="text" id="removedItems.environmentIds" name="removedItems.environmentIds" value="' + id + '" />');
        $(element).parent().parent().append(input);
        $(element).parent().remove();
    } else {
        $(element).parent().remove();
    }
}

/**
* saves a tag
*/
function saveTag(element) {
    let input = $(element).siblings('input[type="text"]');
    let text = input.val();
    if(text) {
        let ele = $(element);
        let input = ele.siblings('input[type="text"]');
        let text = input.val();
        ele.parent().append('<input data-test-id="edit-tag-button" type="button" value="y" onclick="editTag(this)"/>');
        ele.parent().append('<span>' + text + '</span>');
        ele.parent().attr('name', text);
        ele.remove();
        input.hide();
    } else {
        input.tooltip('show');
        setTimeout(function(){
           input.tooltip('dispose');
        }, 2000);
    }
}