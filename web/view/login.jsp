<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>用户登录</title>
    <link rel="shortcut icon" type="image/ico" href="<%=basePath %>assets/images/favicon.ico">
    <link href="<%=basePath %>assets/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=basePath %>assets/content/login.css" rel="stylesheet">
    <link href="<%=basePath %>assets/content/drag.css" rel="stylesheet">
</head>

<body>
<div class="loginheader">
    <p><span class="systemtitle"><span class="bluetxt">酒店屏</span>&nbsp;管理中心</span> <span
            class="systeminfo">欢迎光临本系统!</span></p>
    <span class="datetxt">今天是：<span class="datetime">
       <%
           java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy年MM月dd日");
           java.util.Date currentTime = new java.util.Date();
           String time = simpleDateFormat.format(currentTime).toString();
           out.println(time);
       %>
		</span></span>
</div>
<div class="loginwrapper">
    <div class="logincontent">
        <div class="loginbox">
            <div class="inputbox">
                <div class="form-login">
                    <form action="<%=basePath%>admin/login" method="POST">
                        <fieldset>
                            <label>用户登录</label>
                            <div class="errorbox">${message}</div>
                            <div class="inputwrapper">
                                <i class="icon glyphicon glyphicon-user"></i>
                                <input type="text" name="username" placeholder="请输入用户名"/>
                            </div>
                            <div class="inputwrapper">
                                <i class="icon glyphicon glyphicon-lock"></i>
                                <input type="password" name="password" placeholder="请输入用户密码"/>
                            </div>
                            <div class="loginbtnwrapper">
                                <button type="submit" class="loginbtn" id="btn-login">登 录</button>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="loginfooter">
    <p>Copyright @ 2016 成都翼联科技有限公司</p>
</div>

<div class="modal" id="modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div>
                <img src="<%=basePath%>/assets/images/admin/loader.gif">
            </div>
            <br/>
            努力加载中...
        </div>
    </div>
</div>

<script src="<%=basePath %>assets/scripts/jquery.min.js"></script>
<script src="<%=basePath %>assets/scripts/jquery.drag.js"></script>
<script src="<%=basePath %>assets/bootstrap3/js/bootstrap.min.js"></script>
<script>
    $(function () {
        $('.inputwrapper input').focus(function () {
            $(this).parent(".inputwrapper").addClass("inputfocus");
        }).blur(function () {
            $(this).parent(".inputwrapper").removeClass("inputfocus");
        });

        $('#btn-login').click(function () {
            $('#modal').modal();

            var params = $("#form-login").serializeJson();
            var basePath = "<%=basePath %>";

            $.post(basePath + 'admin/login/doLogin', JSON.stringify(params), function (data) {
                if (data.result == true) {
                    window.location.href = basePath + 'admin/main';
                } else {
                    $('#modal').modal('hide');
                    $('.errorbox').html(data.message);
                    $('.errorbox').css('border', '1px solid #fc9daf');
                }
            })
        })

        $(document).keyup(function (event) {
            if (event.keyCode == 13) {
                $("#btn-login").trigger("click");
            }
        });

        $('#txt-validate-code').val('');
        $('#drag').slideToUnlock({
            onSuccess: function () {
                var code = getCookie('code');
                $('#txt-validate-code').val(code);
                delCookie('code');
            }
        });
    });

    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            return unescape(arr[2]);
        }
        return null;
    }

    function delCookie(name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);

        var cval = getCookie(name);
        if (cval != null)
            document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }

    $.ajaxSetup({
        cache: false,
        contentType: 'application/json;charset=utf-8',
    });

    $.fn.serializeJson = function () {
        var serializeObj = {};
        $(this.serializeArray()).each(function () {
            if (this.value.length > 0)
                serializeObj[this.name] = this.value;
        });
        return serializeObj;
    };
</script>

</body>

</html>
