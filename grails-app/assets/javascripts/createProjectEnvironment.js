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
        let pill = $(`<h3><p class="badge badge-secondary">${text}
                <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayEnvOptions(this)">
                <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                </svg>
                </p></h3>`);
        element.attr('name', text);
        element.append(pill, hiddenElement);
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
* displays edit and delete buttons
*/
function displayEnvOptions(element) {
    let visGroup = $('div.btn-group:visible');
    if(visGroup.length > 0) {
        visGroup.remove();
    }
    let buttonGroup = $(`<div class="btn-group btn-group-sm" role="group" aria-label="edit buttons">
        <button class="btn btn-link" data-test-id="remove-tag-button" type="button" onclick="removeEnvTag(this)">remove</button>
        <button class="btn btn-link" data-test-id="edit-tag-button" type="button" onclick="editEnvTag(this)">edit</button>
        </div>`);
    $(element).parents('li').append(buttonGroup);

    // adds a click handler to cancel edit if clicked outside input
   $(document).on('click', function(event) {
      let ele = $('.btn-group');
      if($(event.target).is('p svg')) return;
      if(ele.length < 1) return;
      if(!ele.is(event.target)) {
          ele.remove();
          $(document).off('div[role=main] form');
      }
   });
}

/**
* displays edit and delete buttons
*/
function displayEnvOptions(element) {
    let visGroup = $('div.btn-group:visible');
    if(visGroup.length > 0) {
        visGroup.remove();
    }
    let buttonGroup = $('<div class="btn-group btn-group-sm" role="group" aria-label="edit buttons">' +
            '<button class="btn btn-link" data-test-id="remove-tag-button" type="button" onclick="removeEnvTag(this)">remove</button>' +
            '<button class="btn btn-link" data-test-id="edit-tag-button" type="button" onclick="editAreaTag(this)">edit</button>' +
            '</div>');
    $(element).parents('li').append(buttonGroup);

    // adds a click handler to cancel edit if clicked outside input
   $(document).on('click', function(event) {
      let ele = $('.btn-group');
      if($(event.target).is('p svg')) return;
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
function editEnvTag(button) {
    let visInput = $('input[data-test-id=tag-input]:visible');
    if(visInput.length > 0) { // cancel other edit if already visible
        let cancel = visInput.siblings('[data-test-id=cancel-tag-button]');
        cancelTag(cancel);
    }
    let ele = $(button).parents("li");
    let input = ele.children('input[type="text"]');
    input.show();
    input.focus();
    ele.children('h3').remove();
    ele.children('.btn-group').remove();
    ele.append('<input type="button" data-test-id="save-tag-button" value="Save" onclick="saveEnvTag(this)"/>');
    ele.append('<input type="button" data-test-id="cancel-tag-button" value="Cancel" onclick="cancelEnvTag(this)"/>');

    // adds a click handler to cancel edit if clicked outside input
    $(document).on('click', function(event) {
        let ele = $('input[data-test-id=tag-input]:visible');
        if($(event.target).is('button')) return;
        if(ele.length < 1) return;
        if(!ele.is(event.target)) {
            let text = ele.parent().attr('name');
            ele.val(text);
            let pill = $(`<h3><p class="badge badge-secondary">${text}
                        <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayEnvOptions(this)">
                        <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                        </svg>
                    </p></h3>`);
            ele.parent().append(pill);
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
function removeEnvTag(element) {
    $(element).parents('li').remove();
}

/**
* cancels a tag
*/
function cancelEnvTag(element) {
    let ele = $(element);
    let input = ele.siblings('input[type="text"]');
    let text = ele.parent().attr('name');
    input.val(text);
    let pill = $(`<h3><p class="badge badge-secondary">${text}
                <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayEnvOptions(this)">
                <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                </svg>
            </p></h3>`);
    ele.parent().append(pill);
    ele.siblings('input[data-test-id="save-tag-button"]').remove();
    ele.remove();
    input.hide();
}

/**
* saves a tag
*/
function saveEnvTag(element) {
    let ele = $(element);
    let input = ele.siblings('input[type="text"]');
    let text = input.val();
    if(text) {
        input.attr('value', text);
        ele.parent().attr('name', text);
        let pill = $(`<h3><p class="badge badge-secondary">${text}
                        <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayEnvOptions(this)">
                        <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                        </svg>
                    </p></h3>`);
        ele.parent().append(pill);
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