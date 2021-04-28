function newFormRow(){
    var newRow = ($("#form-template-row").clone())[0];
    newRow.removeAttribute("id");
    newRow.classList.remove("d-none");
    $("#form-template-row").before(newRow);
}

function findAncestor (el, sel) {
    while ((el = el.parentElement) && !((el.matches || el.matchesSelector).call(el,sel)));
    return el;
}

function removeFormRow(el){
    console.log(el);
    findAncestor(el, ".form-row-optional").remove()
}

function generateKey(){
    var json = {}

    isInvalid = false;

    $(".form-content:not('#form-template-row .form-content')").each((i, v)=>{
        var label = v.querySelector(".form-item-label input").value
        var content = v.querySelector(".form-item-content input").value
        if(content === ""){
            v.querySelector(".form-item-content input").classList.add("invalid-field")
            alert("内容不能为空")
            isInvalid = true;
            return false;
        }else{
            v.querySelector(".form-item-content input").classList.remove("invalid-field")
        }

        json[label] = content;
    })

    if(isInvalid) return;

    var date = $("#form-date").val()
    if(date.length !== 6){
        alert("日期必须是6位数字")
        return;
    }
    json["日期"]=date;

    $.ajax({
        type: "POST",
        url: "/new-key/submit",
        data: JSON.stringify(json),
        contentType: "application/json",
        success: (result) => {
            $("#key-display").text(result)
        },
        error: (xhr, textStatus, error)=>{
            console.log(xhr.status)
        }
    })
}

$(function(){
    $(document).on('click', '.form-remove-row-button', function(e){
        e.preventDefault();
        removeFormRow(e.currentTarget);
    });
});

Date.prototype.yymmdd = function() {
  var mm = this.getMonth() + 1; // getMonth() is zero-based
  var dd = this.getDate();

  return [this.getFullYear() % 100,
          (mm>9 ? '' : '0') + mm,
          (dd>9 ? '' : '0') + dd
         ].join('');
};

function showTodayIfChecked(){
    if($("#isDateNow").is(":checked")){
        var now = new Date().yymmdd()
        document.querySelector("#form-date").readOnly = true
        $("#form-date").val(now)
    }else{
        document.querySelector("#form-date").readOnly = false
        $("#form-date").val("YYMMDD")
    }
}

(()=>{
    showTodayIfChecked()
})()

$("#isDateNow").click(function() {
    showTodayIfChecked()
})

