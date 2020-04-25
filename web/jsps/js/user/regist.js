$(function () {
    /*
    *得到所有的错误信息 然后遍历 调用一个方法来确定是否显示错误信息
    */
    $(".labelError").each(function () {
        showError($(this))//遍历每个元素 使用每个元素来条用showerror方法
    });
    /*
    *光标在注册按钮和离开按钮的效果
    * hover()函数  当指针停留在某个元素身上要执行两个函数
    */
    $("#submit").hover(
        function () {
            $("#submit").attr("src","/images/regist2.jpg");
        },
        function () {
            $("#submit").attr("src","/images/regist1.jpg");
        }
    );

    /*
    * 输入框得到的焦点隐藏信息
    * */
    $(".input").focus(function () {
       var lableId= $(this).attr("id")+"Error";
       $("#"+lableId).text("");//把lable的内容清空
        showError($("#"+lableId));//隐藏红色x
    });

    /*
    * 输入框失去焦点进行校验
    * */
    $(".input").blur(function () {
        var id=$(this).attr("id");
        var funName="validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";//拼接调用的方法名称
        eval(funName);  //把字符串当作JavaScript代码来执行
    });

    /*
        提交表单进行校验 id=registForm
    */
    $("#registForm").submit(function () {
        if (!validateLoginname()){
            return false;
        }
        if (!validateLoginpass()){
            return false;
        }
        if (!validateReloginpass()){
            return false;
        }
        if (!validateEmail()){
            return false;
        }
        if (!validateVerifyCode()){
            return false;
        }
        return true;
    });
})

//登录名校验方法
function validateLoginname() {
    var id="loginname";
    var values=$("#"+id).val();  //获取输入框内容
    if (!values){
        $("#"+id+"Error").text("用户名不能为空");
        showError($("#"+id+'Error'));
        return false;
    }
    if (values.length<3||values.length>20){
        $("#"+id+"Error").text("用户名长度为3-20位");
        showError($("#"+id+'Error'));  //显示那个红色错误提示 lable
        return false;
    }
    $.ajax({
        url:"/goods/userServlet",  //要请求的servlet
        data:{method:"ajaxValidateLoginname",loginname:values},  //给服务器的参数
        type:"POST",
        dataType:"json",
        async:false,  //是否请求异步，如果是异步，那么不会等服务器返回，我们这个函数就向下运行了
        cache:false,
        success:function (result) {
            if (!result){//如果校验失败
                $("#"+id+"Error").text("用户名已注册");
                showError($("#"+id+'Error'));
                return false;
            }
        }
    });
    return true;
}
//登录密码校验方法
function validateLoginpass() {
    var id="loginpass";
    var values=$("#"+id).val();
    if (!values){//密码为空
        $("#"+id+"Error").text("密码不能为空");
        showError($("#"+id+"Error"))
        return false;
    }
    if (values.length<3||values.length>20){
        $("#"+id+"Error").text("密码为3-20位");
        showError($("#"+id+"Error"))
        return false;
    }
    return true;
}
//确认密码校验
function validateReloginpass() {
    var id="reloginpass";
    var values=$("#"+id).val();
    var pass=$("#loginpass").val();
    if (!values){//密码为空
        $("#"+id+"Error").text("确认密码为3-20位");
        showError($("#"+id+"Error"));
        return false;
    }
    if (values!=pass){
        $("#"+id+"Error").text("两次密码不一致");
        showError($("#"+id+"Error"));
        return false;
    }
    return true;
}
//email校验
function validateEmail() {
    var id="email";
    var values=$("#"+id).val();
    if (!values){//密码为空
        $("#"+id+"Error").text("邮件不能为空");
        showError($("#"+id+"Error"))
        return false;
    }
    $.ajax({
        url:"/goods/userServlet",  //要请求的servlet
        data:{method:"ajaxValidateEmail",email:values},  //给服务器的参数
        type:"POST",
        dataType:"json",
        async:false,  //是否请求异步，如果是异步，那么不会等服务器返回，我们这个函数就向下运行了
        cache:false,
        success:function (result) {
            if (!result){//如果校验失败
                $("#"+id+"Error").text("邮箱已注册");
                showError($("#"+id+'Error'));
                return false;
            }
        }
    });
    return true;
}
//验证码校验
function validateVerifyCode() {
    var id="verifyCode";
    var values=$("#"+id).val();
    if (!values){//密码为空
        $("#"+id+"Error").text("验证码不能为空");
        showError($("#"+id+"Error"))
        return false;
    }
    return true;
}

//是否显示错误信息
function showError(ele) {
    var tex=ele.text();
    if (!tex){//没有内容
        ele.css("display","none"); //隐藏元素
    }else {
        ele.css("display","");//显示元素
    }
}
/*
* 更换一张验证码
* prop()　　在元素固有属性的时候使用
　attr()　　在自定义属性使用
  new Date().getTime() 避免浏览器缓存不更换验证码
* */
function change() {
    //$("#vCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime())
}


