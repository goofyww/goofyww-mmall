$(function(){
    //聚焦失焦input
    $('input').eq(0).focus(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("支持中文，字母，数字，'-'，'_'的多种组合");
        }
    })

    $('input').eq(0).blur(function(){
        $(this).parent().next("div").text("");
        $.ajax({
            type: 'POST',
            url: '${base}/user/check_valid.do',
            data:{
               'str':$(".username").val(),
              'type':'username'
            },
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            cache: true,
            success:function(json){
                alert(json);
            }
        });
    })

    $('input').eq(1).focus(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("建议使用字母、数字和符号两种以上的组合，6-20个字符");
        }
    })
    $('input').eq(2).focus(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("请再次输入密码");
        }
    })
    $('input').eq(3).focus(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("验证完后，你可以使用该手机登陆和找回密码");
        }
    })
    $('input').eq(4).focus(function(){
        if($(this).val().length==0){
            $(this).parent().next().next("div").text("看不清？点击图片更换验证码");
        }
    })
    //input各种判断
    //用户名：
    $('input').eq(0).blur(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("");
            $(this).parent().next("div").css("color",'#ccc');
        }else if($(this).val().length>0 && $(this).val().length<4){
            $(this).parent().next("div").text("长度只能在4-20个字符之间");
            $(this).parent().next("div").css("color",'red');
        }else if($(this).val().length>=4&& !isNaN($(this).val())){
            $(this).parent().next("div").text("用户名不能为纯数字");
            $(this).parent().next("div").css("color",'red');
        }else{
            for(var m=0;m<stuList.length;m++){
                if($(this).val()==stuList[m].name){
                    $(this).parent().next("div").text("该用户名已被注册");
                    $(this).parent().next("div").css("color",'red');
                    return;
                }
            }
            $(this).parent().next("div").text("");
        }
    })
    //密码
    $('input').eq(1).blur(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("");
            $(this).parent().next("div").css("color",'#ccc');
        }else if($(this).val().length>0 && $(this).val().length<6){
            $(this).parent().next("div").text("长度只能在6-20个字符之间");
            $(this).parent().next("div").css("color",'red');
        }else{
            $(this).parent().next("div").text("");
        }
    })
//	确认密码
    $('input').eq(2).blur(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("");
            $(this).parent().next("div").css("color",'#ccc');
        }else if($(this).val()!=$('input').eq(1).val()){
            $(this).parent().next("div").text("两次密码不匹配");
            $(this).parent().next("div").css("color",'red');
        }else{
            $(this).parent().next("div").text("");
        }
    })
//	手机号
    $('input').eq(3).blur(function(){
        if($(this).val().length==0){
            $(this).parent().next("div").text("");
            $(this).parent().next("div").css("color",'#ccc');
        }else if($(this).val().substr(0,3)!=138&&$(this).val().substr(0,3)!=189&&$(this).val().substr(0,3)!=139&&$(this).val().substr(0,3)!=158&&$(this).val().substr(0,3)!=188&&$(this).val().substr(0,3)!=157||$(this).val().length!=11){
            $(this).parent().next("div").text("手机号格式不正确");
            $(this).parent().next("div").css("color",'red');
        }else{
            $(this).parent().next("div").text("");
        }
    })
// 	验证码
//	 验证码刷新
    function code(){
        var str="qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPLKJHGFDSAZXCVBNM";
        var str1=0;
        for(var i=0; i<4;i++){
            str1+=str.charAt(Math.floor(Math.random()*62))
        }
        str1=str1.substring(1)
        $("#code").text(str1);
    }
    code();
    $("#code").click(code);
//	验证码验证
    $('input').eq(4).blur(function(){
        if($(this).val().length==0){
            $(this).parent().next().next("div").text("");
            $(this).parent().next().next("div").css("color",'#ccc');
        }else if($(this).val().toUpperCase()!=$("#code").text().toUpperCase()){
            $(this).parent().next().next("div").text("验证码不正确");
            $(this).parent().next().next("div").css("color",'red');
        }else{
            $(this).parent().next().next("div").text("");
        }
    })

})