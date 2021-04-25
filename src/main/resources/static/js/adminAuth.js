function requestLogin(){
    var account = $("#inputAccount").val()
    var password = $("#inputPassword").val()
//    $("#inputPassword").addClass("d-none")
//    $("#inputAccount").addClass("d-none")
    $("#requestedNotification").removeClass("d-none")

    $.ajax({
        type: "POST",
        url: "/auth/login",
        data: JSON.stringify({
            account: account,
            password: password
        }),
        contentType: "application/json",
        success: (res) => {
            deleteAllCookies()
            document.cookie = "auth=" + res;
            location.href = "/admin"
        },
        error: (xhr, status, err) => {
            deleteAllCookies()
//            $("#inputPassword").removeClass("d-none")
//            $("#inputAccount").removeClass("d-none")
            $("#requestedNotification").addClass("d-none")
            if(xhr.status === 401){
                alert("用户名或密码不正确")
            }else{
                alert(xhr.status)
            }
        },
        timeout: 120000
    })

}

352

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}