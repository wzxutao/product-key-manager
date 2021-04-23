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

$(function(){
    $(document).on('click', '.form-remove-row-button', function(e){
        e.preventDefault();
        removeFormRow(e.currentTarget);
    });
});