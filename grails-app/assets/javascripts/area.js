
function addArea() {
    let itemIndex = $('#areas li').length;
    let text = $('#area').val();
    if(text) {
        let element = $('<li/>');
        let hiddenElement = $('<input hidden name="areas[' + itemIndex + '].name" id="areas[' + itemIndex + '].name" value="' + text + '">');
        let button = $('<button onclick="removeAreaElement(this)">x</button>');
        let span = $('<span>' + text + '</span>')
        element.append(button, span, hiddenElement);
        $('#area').val('');
        $('#areas ul').append(element);
    }
}

function removeAreaElement(button) {
    $(button).parent().remove();
}