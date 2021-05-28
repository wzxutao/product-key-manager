function checkKey(){
    var key = $("#key-input").val();
    $.ajax({
        type: "POST",
        url: "/check-key/info",
        data: JSON.stringify({key: $("#key-input").val()}),
        contentType: "application/json",
        success: (res) => {
            $("#invalid-key-message")[0].classList.add("d-none")
            $(".form-content:not('#form-template-row .form-content')").remove()
            for (const [key, value] of Object.entries(JSON.parse(res))) {
                newFormRow(key, value)
            }
        },
        error: (xhr, status, err) => {
            if(xhr.status === 400 || xhr.status === 404){
                $(".form-content:not('#form-template-row .form-content')").remove()
                $("#invalid-key-message")[0].classList.remove("d-none")
            }else if(xhr.status === 403){
                alert('登录已过期')
                location.href = "/auth"
                return;
            }else{
                alert(xhr.status)
            }

        },

    })

}

function newFormRow(key, val){
    var newRow = ($("#form-template-row").clone())[0];
    newRow.removeAttribute("id");
    newRow.classList.remove("d-none");
    newRow.querySelector(".form-item-label input").value = key
    newRow.querySelector(".form-item-content input").value = val
    $("#form-template-row").before(newRow);
}