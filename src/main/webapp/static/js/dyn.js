function menuInHandler(ob){
    $(ob).children().filter(".m_sub_list").first().show();
}

function menuOutHandler(ob){
    $(ob).children().filter(".m_sub_list").first().hide()
}

function loadJMSView(){
    $.ajax({
        dataType: 'xml',
        type: 'POST',
        url: '/admin/tools/jms',
        data: '',
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    });
}

function loadResourcesView(){
    $.ajax({
        dataType: 'xml',
        type: 'POST',
        url: '/admin/dynamic',
        date: '',
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    });
}

function openResource(resource_id){
    $.ajax({
        dataType: 'xml',
        type: 'POST',
        url: '/admin/dynamic/' + resource_id,
        date: '',
        success: function(data){
            $("#admin_view").html(data.children[0].innerHTML);
        }
    });
}

function openCreateResource(){
    $.ajax({
            dataType: 'xml',
            type: 'POST',
            url: '/admin/dynamic/create',
            date: '',
            success: function(data){
                $("#admin_view").html(data.children[0].innerHTML);
            }
        });
}

function s_click(object){
    var o = $(object);
    if(o.hasClass("selected")){
        o.removeClass("selected");
        o.children().first().removeAttr("checked");
    }
    else{
        o.addClass("selected");
        o.children().first().attr("checked", "checked");

    }
}

function s_show(object){
    var o = $(object);
    if(!o.hasClass("selected")){
        $("#strategy_selector").children().each(function(){
            if($(this).children().first().hasClass("selected")){
                $(this).children().first().removeClass("selected");
                $(this).children().first().next().removeAttr("checked", "checked");
                $(this).children().first().next().next().hide();
            }
        })
        o.addClass("selected");
        o.next().attr("checked", "checked");
        o.next().next().show();
    }
}

function createResource(){

    var fields = $(":input").serialize();

    $.ajax({
        dataType: 'json',
        type: 'POST',
        url: '/admin/dynamic/add',
        data: fields,
        success: function(data){
            loadResourcesView();
        }
    });
}