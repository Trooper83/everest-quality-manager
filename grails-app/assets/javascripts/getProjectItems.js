/**
* gets the current selected project and
* retrieves it's corresponding items
*/
function getProjectItems() {
    const projectId = $("#project").children("option:selected").val();
    if(projectId != '') {
        $.ajax({
            url: '/project/getProjectItems',
            type: 'GET',
            data: {id:projectId},
            dataType: 'json',
            success:function(response) {
               populateAreas(response.areas);
               populateEnvironments(response.environments);
            }
        });
    } else {
        $("#area").empty();
        $("#area").append("<option value=''>Select an Area...</option>");
        $("#area").prop("disabled", true);

        $("#environments").empty();
        $("#environments").append("<option value=''>--No Environment--</option>");
        $("#environments").prop("disabled", true);
    }
}

function populateAreas(areas) {
    const len = areas.length;
    $("#area").empty();
    $("#area").append("<option value=''>Select an Area...</option>");
    for(let i = 0; i < len; i++){
       let id = areas[i]['id'];
       let name = areas[i]['name'];
       $("#area").append("<option value='"+id+"'>"+name+"</option>");
    }
    $("#area").prop("disabled", false);
}

function populateEnvironments(environments) {
    const len = environments.length;
    $("#environments").empty();
    $("#environments").append("<option value=''>--No Environment--</option>");
    for(let i = 0; i < len; i++){
       let id = environments[i]['id'];
       let name = environments[i]['name'];
       $("#environments").append("<option value='"+id+"'>"+name+"</option>");
    }
    $("#environments").prop("disabled", false);
}