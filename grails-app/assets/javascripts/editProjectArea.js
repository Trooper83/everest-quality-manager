/**
* adds an area tag
*/
function addAreaTag() {
    let itemIndex = $('#areas li').length;
    let text = $('#area').val().trim();
    if(text.length > 0) {
        let element = $('<li class="list-inline-item"/>');
        let row = $('<div class="form-row ml-1"></div>');
        let pill = $(`<p class="ml-1 badge text-bg-light border">${text}<svg xmlns="http://www.w3.org/2000/svg"
            style="cursor:pointer" width="16" height="16" fill="currentColor" class="bi bi-x" onclick="removeAreaTag(this)">
            <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
            </svg>
        </p>`);
        element.attr('name', text);
        row.append(pill);
        element.append(row);
        $('#area').val('');
        $('#areaRow ul').append(element);
    } else {
        let element = $("[name='area']");
        element.tooltip('show');
        setTimeout(function(){
            element.tooltip('dispose');
        }, 2000);
    }
}
/**
* removes an area tag
*/
function removeAreaTag(element, id) {
    if(id) {
        let input = $('<input style="display: none;" data-test-id="removed-tag-input" type="text" id="removedItems.areaIds" name="removedItems.areaIds" value="' + id + '" />');
        $(element).parents('ul').append(input);
        $(element).parents('li').hide();
    } else {
        const parent = $(element).parents('li');
        parent.children().remove();
        parent.hide();
    }
}