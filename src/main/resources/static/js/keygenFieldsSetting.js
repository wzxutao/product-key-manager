// override
function showTodayIfChecked(){
    if($("#isDateNow").is(":checked")){
        var now = new Date().yymmdd()
        document.querySelector("#form-date").readOnly = true
        $("#form-date").val(now)
    }else{
//        document.querySelector("#form-date").readOnly = false
        $("#form-date").val("YYMMDD")
    }
}


function requestModify(){
    var arr = [];
    $(".required-field-name:not('#form-template-row .required-field-name')").toArray().forEach(e=>{
        arr.push(e.value)
    })

    $.ajax({
        type: "POST",
        url: "/admin/keygenFields/modify",
        data: JSON.stringify({mandatoryFieldNames: arr}),
        contentType: "application/json",
        success: function() {
            alert("成功");
            $("#keygen-new-tab").removeClass("d-none");
        },
        error: (xhr, status, err)=>{
            if(xhr.status === 401){
                alert("登陆已过期")
                location.href = "/auth"
                return;
            }
        }
    })


}