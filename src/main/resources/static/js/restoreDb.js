function requestRestore(fileName){
    $.ajax({
        type: "POST",
        url: "/admin/restore" + `?fileName=${fileName}`,
        success: ()=>{
            alert(`${fileName} 成功还原`)
            location.href = "/admin"
        },
        error: (xhr, status, err) => {
            alert(xhr.status)
        }
    })
}

$(document).ready(
    document.querySelectorAll(".backup-file").forEach(
    e=>{e.href=`javascript:requestRestore("${e.innerText}")`})
)