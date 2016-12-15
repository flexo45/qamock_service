function menuInHandler(ob){
    $(ob).children().filter(".m_sub_list").first().show();
}

function menuOutHandler(ob){
    $(ob).children().filter(".m_sub_list").first().hide()
}

function loadJMSView(){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/tools/jms",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    });
}

function loadResourcesView(){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/dynamic",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    });
}

function openResource(resource_id){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/dynamic/" + resource_id,
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    });
}

function openCreateResource(){
    $.ajax({
            dataType: "xml",
            type: "GET",
            url: "/admin/dynamic/create",
            success: function(data){
                $("#admin_view").html(data.children[0].innerHTML);
            }
        });
}

function openAddResponse(resource_id){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/dynamic/" + resource_id + "/response/create",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    })
}

function openResourceEdit(resource_id){
    $.ajax({
            dataType: "xml",
            type: "GET",
            url: "/admin/dynamic/" + resource_id + "/edit",
            success: function(data){
                $("#admin_view").html(data.children[0].innerHTML);
            }
        });
}

function openResponseView(resource_id, response_id){
    $.ajax({
            dataType: "xml",
            type: "GET",
            url: "/admin/dynamic/" + resource_id + "/response/" + response_id,
            success: function(data){
                $("#admin_view").html(data.children[0].innerHTML);
            }
        });
}

function openResponseEdit(resource_id, response_id){
    $.ajax({
            dataType: "xml",
            type: "GET",
            url: "/admin/dynamic/" + resource_id + "/response/" + response_id + "/edit",
            success: function(data){
                $("#admin_view").html(data.children[0].innerHTML);
            }
        });
}

function createResource(){

    $.ajax({
        type: "POST",
        url: "/admin/dynamic/add",
        data: $("#add_resource").serialize(),
        dataType: "text",
        success: function(data){
            alert("Create successful");
            loadResourcesView();
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function createResponse(resource_id){

    $.ajax({
        type: "POST",
        url: "admin/dynamic/" + resource_id + "/response/add",
        data: $("#add_response").serialize(),
        dataType: "text",
        success: function(data){
            alert("Create successful");
            openResource(resource_id);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });

}

function updateResource(resource_id){
    $.ajax({
        type: "POST",
        url: "admin/dynamic/" + resource_id + "/update",
        data: $("#update_resource").serialize(),
        dataType: "text",
        success: function(data){
            alert("Update successful");
            openResource(resource_id);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function deleteResource(resource_id){

    var result = confirm("Are you sure you want to delete the resource id=" + resource_id + " ?")

    if(result){
        $.ajax({
            type: "POST",
            url: "admin/dynamic/" + resource_id + "/delete",
            success: function(data){
                alert("Delete successful");
                loadResourcesView();
            },
            error: function(data){
                alert("ERROR " + data.responseText);
            }
        });
    }
}

function deleteResponse(resource_id, response_id){

    var result = confirm("Are you sure you want to delete the response id=" + response_id + " ?")

    if(result){
        $.ajax({
            type: "POST",
            url: "admin/dynamic/" + resource_id + "/response/" + response_id + "/delete",
            success: function(data){
                alert("Delete successful");
                loadResourcesView();
            },
            error: function(data){
                alert("ERROR " + data.responseText);
            }
        });
    }
}

function updateResponse(resource_id, response_id){

    $.ajax({
        type: "POST",
        url: "admin/dynamic/" + resource_id + "/response/" + response_id + "/update",
        data: $("#update_response").serialize(),
        dataType: "text",
        success: function(data){
            alert("Update successful");
            openResponseView(resource_id, response_id);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function loadScriptsView(){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/scripts/",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function openCreateScript(){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/scripts/create",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function openScriptEdit(script_id){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/scripts/" + script_id + "/edit",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function openScript(script_id){
    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/scripts/" + script_id,
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function createScript(){
    $.ajax({
        type: "POST",
        url: "/admin/scripts/add",
        data: $("#add_script").serialize(),
        dataType: "text",
        success: function(data){
            alert("Create successful");
            loadScriptsView();
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function updateScript(script_id){
    $.ajax({
        type: "POST",
        url: "/admin/scripts/" + script_id + "/update",
        data: $("#update_script").serialize(),
        dataType: "text",
        success: function(data){
            alert("Update successful");
            openScript(script_id);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function loadLogsView(){

    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/logs?resource_id=-1&size=10",
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function applyFilter(){

    $.ajax({
        dataType: "xml",
        type: "GET",
        url: "/admin/logs?" + $("#get_logs").serialize(),
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        },
        error: function(data){
            alert("ERROR " + data.responseText);
        }
    });
}

function addHdr(){

    var row = "<tr><td><input type=\"text\" name=\"headers\" id=\"headers\" value=\"\"><span class=\"spn_btn\" onclick=\"delHdr(this)\">-</span></td></tr>";
    //var row = $("#hdr_tbl").children().first().clone();
    //row.children().first().children().first().val("");
    //row.children().first().append("<span class=\"spn_btn\" onclick=\"delHdr(this)\">-</span>")
    //row.appendTo("#hdr_tbl");
    $("#hdr_tbl").append(row);


}

function delHdr(object){
    $(object).parent().parent().remove();
}