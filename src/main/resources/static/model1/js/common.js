function logout(){
    if(confirm("您确认要退出么")){
        location.href = ContextPath + "/admin/logout.do";
    }
}