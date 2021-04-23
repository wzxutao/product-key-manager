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
    $(".form-content:not('#form-template-row .form-content')").each((i, v)=>{
        var label = v.querySelector(".form-item-label input").value
        var content = v.querySelector(".form-item-content input").value
        json[label] = content;
    })

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