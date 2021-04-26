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
            else{
                alert(xhr.status)
            }
        }

    })
}