import { API_URL } from "../common/constants";

export { API_URL };

export const AUTH_ERROR = new Error('401')

export type ErrorLogger = (msg: string) => void;

let lastAlertTime = 0;

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}


export const handleAndThrowAuthError = (err: any, errorLogger?: ErrorLogger) => {
    if ([401, 403].includes(err?.response?.status)) {
        errorLogger ? errorLogger("登录已到期") : alert("登录已到期")
        deleteAllCookies();
        if (lastAlertTime < Date.now() - 10_000) {
            alert("登录已到期");
            lastAlertTime = Date.now();
        }
        window.location.href = '/';
        throw AUTH_ERROR;
    }
}

export const logAndRethrowOtherError = (err: any, errorLogger?: ErrorLogger): void => {
    console.error(err);
    let response = ''
    try {
        response = '\n' + JSON.stringify(err?.response?.data, null, 2)
    } catch (_) { }

    const msg = '操作失败：' + (err?.response?.status ?? '未知错误') + response;
    errorLogger ? errorLogger(msg) : alert(msg)
    throw err;
}

