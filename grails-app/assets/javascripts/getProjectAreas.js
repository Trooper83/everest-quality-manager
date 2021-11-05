/**
* gets the current selected project and
* retrieves it's corresponding areas
*/
function getAreas() {
    const projectId = $("#project").children("option:selected").val();
    if(projectId != '') {
        $.ajax({
            url: '/project/getAreas',
            type: 'GET',
            data: {id:projectId},
            dataType: 'json',
            success:function(response){
                const len = response.length;
                $("#area").empty();
                $("#area").append("<option value=''>Select an Area...</option>");
                for(let i = 0; i < len; i++){
                    let id = response[i]['id'];
                    let name = response[i]['name'];
                    $("#area").append("<option value='"+id+"'>"+name+"</option>");
                }
                $("#area").prop("disabled", false);
            }
        });
    } else {
        $("#area").empty();
        $("#area").append("<option value=''>Select an Area...</option>");
        $("#area").prop("disabled", true);
    }
}