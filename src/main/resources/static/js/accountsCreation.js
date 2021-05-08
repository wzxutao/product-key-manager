removePendingList = []

function removeExisting(el){
    console.log(el);
    var ans = findAncestor(el, ".form-row-mandatory")
    removePendingList.push(ans.getAttribute("account"))
    ans.remove()
}

function upload(){
    isInvalid = false;
    var json = {}

    $(".form-row-optional .form-content:not('#form-template-row .form-content')").each((i, v)=>{
        var username = requireNoneEmptyInput(v, ".form-item-label .usernameField")
        var password = requireNoneEmptyInput(v, ".form-item-content .passwordField")
        var passwordConfirm = requireNoneEmptyInput(v, ".form-item-content .passwordConfirmField")

        if(username === null || password === null || passwordConfirm === null){
            isInvalid = true
            return false;
        }else{
            if(password.value !== passwordConfirm.value){
                alert("密码不一致")
                setInvalid(passwordConfirm, true)
                isInvalid = true
                return false;
            }else{
                setInvalid(passwordConfirm, false)
                json[username.value] = password.value;
            }
        }
    })

    console.log(json)
    if(isInvalid) return;

    $.ajax({
        type: "POST",
        url: "/admin/accounts-add",
        data: JSON.stringify(json),
        contentType: "application/json",
        success: (result) => {
            requestDeleteExisting()
        },
        error: (xhr, status, err) => {
            if(xhr.status === 401){
                alert("登录已过期")
                location.href = "/auth"
                return;
            }else{
                alert(xhr.status)
            }
        }
    })

}

function requestDeleteExisting(){
    $.ajax({
        type: "POST",
        url: "/admin/accounts-remove",
        contentType: "application/json",
        data: JSON.stringify({
            list: removePendingList
        }),
        success: ()=> location.reload(),
        error: (xhr, status, err) => {
            if(xhr.status === 401){
                alert("登录已过期")
                location.href = "/auth"
                return;
            }else{
                alert(xhr.status)
            }
        }


    })
}

function setInvalid(el, isInvalid){
    if(isInvalid === true){
        el.classList.add("invalid-field")
    }else{
        el.classList.remove("invalid-field")
    }
}

// returns el if not empty, null otherwise
function requireNoneEmptyInput(topEl, selector){
    var el = topEl.querySelector(selector)
    if(el.value === ""){
        setInvalid(el, true)
        alert("内容不能为空")
        return null;
    }else{
        setInvalid(el, false)
        return el;
    }
}


$(function(){
    $(document).on('click', '.form-remove-existing-button', function(e){
        e.preventDefault();
        removeExisting(e.currentTarget);
    });
});