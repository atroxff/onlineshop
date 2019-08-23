//表单校验并提交
function validateAndSubmit(){
    //校验信息置空
    document.getElementById("pname_tip").innerHTML="";
    document.getElementById("categoryid_tip").innerHTML="";
    document.getElementById("shop_price_tip").innerHTML="";
    document.getElementById("market_price_tip").innerHTML="";
    document.getElementById("is_hot_tip").innerHTML="";
    document.getElementById("pflag_tip").innerHTML="";
    //document.getElementById("pimage_pic_tip").innerHTML="";
    //校验非空 pname categoryid shop_price market_price is_hot pimage
    var pname=document.getElementById("pname").value;
    var categoryid=document.getElementById("categoryid").value;
    var shop_price=document.getElementById("shop_price").value;
    var market_price=document.getElementById("market_price").value;
    var pflag=document.getElementById("pflag").value;
    var is_hot=document.getElementById("is_hot").value;
    //var pimage=document.getElementById("pimage_pic").value;
    console.log(pname+" "+categoryid+" "+shop_price+" "+market_price+" "+is_hot+" ");
    if(pname==""){
        document.getElementById("pname_tip").innerHTML="商品名称不能为空";
    }
    if(categoryid==""){
        document.getElementById("categoryid_tip").innerHTML="商品分类不能为空";
    }
    if(shop_price==""){
        document.getElementById("shop_price_tip").innerHTML="商城价不能为空";
    }
    if(market_price==""){
        document.getElementById("market_price_tip").innerHTML="市场价不能为空";
    }
    if(is_hot==""){
        document.getElementById("is_hot_tip").innerHTML="热门标志不能为空";
    }
    if(pflag==""){
        document.getElementById("pflag_tip").innerHTML="下架标志不能为空";
    }
    // if(pimage==""){
    //     document.getElementById("pimage_pic_tip").innerHTML="商品图片不能为空";
    // }
    //修改商品时可以不选择图片  使用原图片
    if(pname==""||categoryid==""||shop_price==""||market_price==""||is_hot==""||pflag==""){
        console.log("校验失败");
        return;
    }

    //校验数据格式
    var reg=/^[-\+]?\d+(\.\d+)?$/;
    var flag= true;
    if(!reg.test(shop_price)){
        document.getElementById("shop_price_tip").innerHTML="价格格式不正确";
        flag=false;
    }
    if(!reg.test(market_price)){
        document.getElementById("market_price_tip").innerHTML="价格格式不正确";
        flag=false;
    }
    if(!flag){
        console.log("校验失败");
        return;
    }

    console.log("校验成功");

    //表单提交
    //document.getElementById("form1").submit();
    $("#form1").submit();
};