function getEntryRow() {
    let row = $('<tr />');

    let itemIndex = $('#stepsTableContent input.iHidden').length;

    let hidden = $('<td><input type="hidden" name="stepsIndex[' + itemIndex + ']" class="iHidden" value="" id="steps[' + itemIndex + ']"/></td>');
    let action = $('<td><input type="text" name="steps[' + itemIndex + '].action" value="" id="steps[' + itemIndex + '].action"/></td>');
    let result = $('<td><input type="text" name="steps[' + itemIndex + '].result" value="" id="steps[' + itemIndex + '].result"/></td>');
    let button = $('<td><input type="button" value="Remove" onclick="removeEntryRow(this)" /></td>');

    row.append(hidden);
    row.append(action);
    row.append(result);
    row.append(button);
    return row;
}

function addEntryRow() {
    let row = getEntryRow();
    $('#stepsTableContent').append(row);
}

function removeEntryRow(button) {
    $(button).parent().parent().remove();
}