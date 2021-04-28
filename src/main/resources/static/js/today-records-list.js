function requestDelete(key){
    $.ajax({
        type: "POST",
        url: "/today-records/delete" + `?key=${key}`,
        success: () => location.reload(),
        error: (xhr, status, err) => alert(xhr.status)
    })
}