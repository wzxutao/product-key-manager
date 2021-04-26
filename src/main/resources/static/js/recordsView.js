function setToday(el, fromStart){
    $.ajax({
        type: "GET",
        url: "/util/datetime-today" + `?fromStart=${fromStart}`,
        success: (res) => el.value = res,
        error: (xhr, status, err) => console.log(xhr.status)
    })
}

function filter(){
    var from = $("#from-time").val()
    var to = $("#to-time").val()
    $.ajax({
        type: "POST",
        url: "/admin/records",
        data: JSON.stringify({
            fromTime: from,
            toTime: to
        }),
        contentType: "application/json",
        dataType: "html",
        success: (html) => {
            document.open();
            document.write(html);
            document.close();
        },
        error: (xhr, status, err) => {
            if(xhr.status === 400){
                alert("日期格式不正确")
            }else{
                alert(xhr.status)
            }
        }
    })
}