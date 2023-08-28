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