function setToday(el, fromStart){
    $.ajax({
        type: "GET",
        url: "/util/datetime-today" + `?fromStart=${fromStart}`,
        success: (res) => el.value = res,
        error: (xhr, status, err) => console.log(xhr.status)
    })
}