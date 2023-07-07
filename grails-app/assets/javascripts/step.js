function getEntryRow() {
    const row = $('<tr />');

    const index = $('#stepsTableContent input.iHidden').length;
    const removed = $('#stepsTableContent input[data-test-id=step-removed-input]').length;
    const itemIndex = index + removed;

    const hidden = $('<td><input type="hidden" name="stepsIndex[' + itemIndex + ']" class="iHidden" value="" id="steps[' + itemIndex + ']"/></td>');
    const action = $('<td><textarea class="form-control" type="text" maxLenght="500" name="steps[' + itemIndex + '].action" value="" id="steps[' + itemIndex + '].action"/></td>');
    const result = $('<td><textarea class="form-control" type="text" maxLenght="500" name="steps[' + itemIndex + '].result" value="" id="steps[' + itemIndex + '].result"/></td>');
    const button = $('<td><input class="btn btn-link btn-sm" type="button" value="Remove" onclick="removeEntryRow(this)" /></td>');

    row.append(hidden, action, result, button);
    return row;
}

function addEntryRow() {
    let button = $('tr input[type=button]').last().hide();
    let row = getEntryRow();
    $('#stepsTableContent').append(row);
    $('tr').last().find('textarea[name$=action]').focus();
}

function removeEntryRow(element, id) {
    if(id) {
       let input = $('<input style="display: none;" data-test-id="step-removed-input" type="text" id="removedItems.stepIds" name="removedItems.stepIds" value="' + id + '" />');
       $(element).parent().parent().parent().append(input);
       $(element).parent().parent().remove();
    } else {
       $(element).parent().parent().remove();
    }
    let button = $('tr input[type=button]').last().show();
}