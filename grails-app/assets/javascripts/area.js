
function addArea() {
    let itemIndex = $('#input-areas li').length;
    let text = $('#areaInput').val();
    if(text) {
        let element = $('<li/>');
        let hiddenElement = $('<input hidden name="areas[' + itemIndex + '].name" id="areas[' + itemIndex + '].name" value="' + text + '">');
        let button = $('<button onclick="removeAreaElement(this)">x</button>');
        let span = $('<span>' + text + '</span>')
        element.append(button);
        element.append(span);
        $('#areaInput').val('');
        element.append(hiddenElement);
        $('#input-areas ul').append(element);
    }
}

function removeAreaElement(button) {
    $(button).parent().remove();
}