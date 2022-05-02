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
                'data-toggle="tooltip" trigger="manual" data-original-title="Area Name cannot be blank"/>');
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
                'data-toggle="tooltip" trigger="manual" data-original-title="Environment Name cannot be blank"/>');
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
    let visInput = $('input[data-test-id=tag-input]:visible');
    if(visInput.length > 0) { // cancel other edit if already visible
        let cancel = visInput.siblings('[data-test-id=cancel-tag-button]');
        cancelTag(cancel);
    }
    let ele = $(button);
    let input = ele.siblings('input[type="text"]');
    input.show();
    input.focus();
    ele.siblings('span').remove();
    ele.parent().append('<input type="button" data-test-id="save-tag-button" value="Save" onclick="saveTag(this)"/>');
    ele.parent().append('<input type="button" data-test-id="cancel-tag-button" value="Cancel" onclick="cancelTag(this)"/>');
    ele.remove();

    // adds a click handler to cancel edit if clicked outside input
    $(document).on('click', function(event) {
        let ele = $('input[data-test-id=tag-input]:visible');
        if($(event.target).is('input[data-test-id=edit-tag-button]')) return;
        if(ele.length < 1) return;
        if(!ele.is(event.target)) {
            let text = ele.parent().attr('name');
            ele.val(text);
            ele.parent().append('<input data-test-id="edit-tag-button" type="button" value="y" onclick="editTag(this)"/>');
            ele.parent().append('<span>' + text + '</span>');
            ele.siblings('input[data-test-id="save-tag-button"]').remove();
            ele.siblings('input[data-test-id="cancel-tag-button"]').remove();
            ele.hide();
            $(document).off('div[role=main] form');
        }
    });
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
* cancels a tag
*/
function cancelTag(element) {
    let input = $(element).siblings('input[type="text"]');
    let ele = $(element);
    let text = ele.parent().attr('name');
    input.val(text);
    ele.parent().append('<input data-test-id="edit-tag-button" type="button" value="y" onclick="editTag(this)"/>');
    ele.parent().append('<span>' + text + '</span>');
    ele.siblings('input[data-test-id="save-tag-button"]').remove();
    ele.remove();
    input.hide();
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
        input.attr('value', text);
        ele.parent().append('<input data-test-id="edit-tag-button" type="button" value="y" onclick="editTag(this)"/>');
        ele.parent().append('<span>' + text + '</span>');
        ele.parent().attr('name', text);
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