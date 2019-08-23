

//删除商品
function deleteProduct(pid) {
    console.log("删除商品：" + pid);
    if (confirm("您确认要删除该商品么？（删除后商品所有信息都将消失）")) {
        location.href=ContextPath+"/admin/product/delete.do?pid="+pid;
    }
};

//查找商品
function searchProduct(){
    var key = document.getElementById("searchKey").value;
    var type=document.getElementById("searchType").value;//搜索类型 0按编号 1按名称 2按分类
    var pagenumStr = "";
    var pagesizeStr = "";

    //校验
    if(key==""){
        return;
    }
    console.log("searchKey:"+key+"searchtype:"+type);
    location.href=ContextPath+"/admin/product.do?key="+key+"&type="+type+"&pagenumStr="+pagenumStr+"&pagesizeStr="+pagesizeStr;
};
//上一页
function pageUp() {
    var key = document.getElementById("searchKey").value;
    var type=document.getElementById("searchType").value;//搜索类型 0按编号 1按名称 2按分类
    var pagenumStr =document.getElementById("pagenumStr").value;
    pagenumStr=parseInt(pagenumStr)-1<1?1:parseInt(pagenumStr)-1;
    var pagesizeStr = "";
    //console.log("searchKey:"+key+"searchtype:"+type+"pagenumStr:"+pagenumStr);
    if(key==""){
        location.href=ContextPath+"/admin/product.do?pagenumStr="+pagenumStr+"&pagesizeStr="+pagesizeStr;
    }else{
        location.href=ContextPath+"/admin/product.do?key="+key+"&type="+type+"&pagenumStr="+pagenumStr+"&pagesizeStr="+pagesizeStr;
    }
};
//下一页
function pageDown(totalpage) {
    var key = document.getElementById("searchKey").value;
    var type=document.getElementById("searchType").value;//搜索类型 0按编号 1按名称 2按分类
    var pagenumStr =document.getElementById("pagenumStr").value;
    pagenumStr=parseInt(pagenumStr)+1>totalpage?totalpage:parseInt(pagenumStr)+1;
    var pagesizeStr = "";
    //console.log("searchKey:"+key+"searchtype:"+type+"pagenumStr:"+pagenumStr);
    if(key==""){
        location.href=ContextPath+"/admin/product.do?pagenumStr="+pagenumStr+"&pagesizeStr="+pagesizeStr;
    }else{
        location.href=ContextPath+"/admin/product.do?key="+key+"&type="+type+"&pagenumStr="+pagenumStr+"&pagesizeStr="+pagesizeStr;
    }
};

function toPageNum(totalpage) {
    //var key = document.getElementById("searchKey").value;
    //var type=document.getElementById("searchType").value;//搜索类型 0按编号 1按名称 2按分类
    var pagenumStr =document.getElementById("pagenumStr").value;
    pagenumStr=parseInt(pagenumStr);
    if(pagenumStr>totalpage||pagenumStr<1){
        return;
    }
    var pagesizeStr = "";

    //console.log("searchKey:"+key+"searchtype:"+type+"pagenumStr:"+pagenumStr);
    location.href=ContextPath+"/admin/product.do?pagenumStr="+pagenumStr+"&pagesizeStr="+pagesizeStr;
};