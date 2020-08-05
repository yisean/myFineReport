function ajaxLogin(username,password){
    jQuery.ajax({
        url:"http://localhost:8075/WebReport/ReportServer?op=fs_load&cmd=sso",//单点登录的报表服务器
        dataType:"jsonp",//跨域采用jsonp方式
        data:{"fr_username":username,"fr_password":password},
        jsonp:"callback",
        timeout:5000,//超时时间（单位：毫秒）
        success:function(data) {
            if (data.status === "success") {
                //登录成功
            } else if (data.status === "fail"){
                //登录失败（用户名或密码错误）
            }
        },
        error:function(){
            // 登录失败（超时或服务器其他错误）
        }
    });
}

