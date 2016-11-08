function menuInHandler(ob){
    $(ob).children().filter('.m_sub_list').first().show();
}

function menuOutHandler(ob){
    $(ob).children().filter('.m_sub_list').first().hide()
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