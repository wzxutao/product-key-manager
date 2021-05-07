function requestBackup(){
    var fileName = $("#backup-name").val()
    if(fileName === ""){
        $("#backup-name").addClass("invalid-field")
        alert("请输入备份文件名")
        return
    }

    $("#backup-name").removeClass("invalid-field")

    $.ajax({
        type: "POST",
        url: "/admin/backup" + `?fileName=${fileName}`,
        success: ()=>{
            alert(`${fileName} 成功备份`)
            $("#backup-name").val("")
        },
        error: (xhr, status, err) => {
            if(xhr.status === 422){
                alert("备份文件名不合法")
            }
            else if(xhr.status === 401){
                alert("登陆已过期")
                location.href = "/auth"
                return;
            }
            else{
                alert(xhr.status)
            }
        }

    })
}

function requestChangeKeyLength(){
    var length = $("#key-length").val()
    $.ajax({
        type: "POST",
        url: "/admin/key-length" + `?length=${length}`,
        success: ()=>location.reload(),
        error: (xhr, status, err) => {
            if(xhr.status === 400){
                alert("无效长度")
            }else{
                alert(xhr.status)
            }
        }
    })
}

function checkUpdate(){
    $.ajax({
        type: "POST",
        url: "/admin/update",
        success: ()=>alert('有更新可用，更新即将开始，服务器将会重启'),
        error: (xhr, status, err) => {
            if(xhr.status === 404){
                alert('已是最新版本')
            }else if(xhr.status === 429){
                alert("在查了在查了，不过也许您的服务器连不上github")
            }else{
                alert('更新失败，大概率是网络问题')
            }
        }
    })
}