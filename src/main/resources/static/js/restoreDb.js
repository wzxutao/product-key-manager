function requestRestore(fileName){
    $.ajax({
        type: "POST",
        url: "/admin/restore" + `?fileName=${fileName}`,
        success: ()=>{
            alert(`请求还原 ${fileName} ，服务器即将关闭，请手动重启`)
            location.href = "/admin"
        },
        error: (xhr, status, err) => {
            if(xhr.status === 401){
                alert("登陆已过期")
                location.href = "/admin"
            }else{
                alert(xhr.status)
            }
        }
    })
}

$(document).ready(
    document.querySelectorAll(".backup-file").forEach(
    e=>{e.href=`javascript:requestRestore("${e.innerText}")`})
)