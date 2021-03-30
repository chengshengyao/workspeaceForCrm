<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath() + "/"; %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script>

        $(function () {
            if (window.top!==windbow){
                window.top.location=window.location
            }
            //页面加载完毕或刷新 清空文本输入框的内容
            $("#loginAct").val("")
            // 用户名文本框获取光标 进行输入
            $("#loginAct").focus()
            //  验证登录信息
            //	登录按钮绑定 1.单击事件 2.回车触发单击
            $("#submitBtn").click(function () {
                login()
            })
            $(window).keydown(function (event) {
                // enter key 的 keycode 等于 13
                if (event.keyCode == 13) {
                    login()
                }
            })
        })

        //自定义 方法 定义在$(function (){})的外面
        function login() {
            // 封装提取登录验证方法
            //验证账号 密码在数据库中取出的user对象信息中不为空
            // 将用户所输入的账号密码去除 头尾空格
            var loginAct = $.trim($("#loginAct").val());
            var loginPwd = $.trim($("#loginPwd").val());
            if (loginAct == "" || loginPwd == "") {
                $("#msg").html("账号密码不能为空");
                //终止用户登录操作
                return false;
            }

            //验证 账号密码不为空的情况下 在【后台】数据库中进行 数据比对 [异步请求 局部刷新]
            $.ajax({
                url:"/settings/user/login.do",
                data:{"loginAct":loginAct,"loginPwd":loginPwd},
                type:"post",
                dataType:"json",
                success:function (data) {
                    if (data.success){
                    //登录成功跳转工作台初始页
                        window.location.href="workbench/index.jsp";
                    }
                    else {
                    //登录失败 4种情况
                        $("#msg").html(data.msg)
                    }
                }
            })
        }
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;阿里巴巴</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" id="loginAct">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" id="loginPwd">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">

                    <span id="msg"></span>

                </div>
                <button type="submit" id="submitBtn" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>