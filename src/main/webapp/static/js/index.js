//页面初始化

var v_img_index=2;
setInterval(function(){
    if(v_img_index>3){
        v_img_index=1;
    }
    //获取img控件对象
    var v_img = document.getElementById("v_rollImg");
    //修改图片路径
    v_img.src="static/img/"+(v_img_index++)+".jpg";
},2000);