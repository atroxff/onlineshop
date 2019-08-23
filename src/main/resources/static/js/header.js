//header.jsp加载完毕后 去服务器端获得所有的category数据
function getCategoryList(){
    //alert(ContextPath);
    var content = "";
    $.ajax({
        type:'post',
        async:false,
        url:ContextPath+'/items/ajax/getCategoryList.do',
        data:{},
        dataType: 'json',
        success:function(data){
            console.log(data)
            for(var i=0;i<data.length;i++){
                var category  = data[i];

                content+="<li class='nav-item'><a class='nav-link' href='"+ContextPath+"/items/list.do?cid="+category.cid+"'>"+category.cname+"</a></li>";
            }
            // //将拼接好的li放置到ul中    要在html元素加载后再进行拼接
            $("#categoryUl").html(content);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            alert("获取菜单目录失败!");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });
};

function getCart(){
    var user= document.getElementById("user");
    if(user != null ){
        location.href=ContextPath+"/cart/cartlist.do";
    }else{
        var r=confirm("请登录后再访问！")
        if(r==true){
            location.href=ContextPath;
        }
    }
};

function getOrders(){
    var user= document.getElementById("user");
    if(user != null ){
        location.href=ContextPath+"/cart/myOrders.do";
    }else{
        var r=confirm("请登录后再访问！")
        if(r==true){
            location.href=ContextPath;
        }
    }
};

function logout(){
    var user= document.getElementById("user");
    if(user != null ){
        var r=confirm("您确定要退出么？")
        if(r==true) {
            location.href = ContextPath + "/user/logout.do";
        }
    }else{
        location.href="#";
    }
};

function ajaxtest(){
    var me = this, data = me.data;
    $.ajax({
        type:'post',
        async:false,
        url:ContextPath+'/test/ajax.do',
        data:{},
        dataType: 'json',
        success:function(req){
            var msg = req.msg || [];

            console.log(msg);
            //me.$update();  //regular.min.js

        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            alert("失败!");
            // alert(XMLHttpRequest.status);
            // alert(XMLHttpRequest.readyState);
            // alert(textStatus);
        }
    });
};
