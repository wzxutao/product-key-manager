import axios from "axios"
import { API_URL, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"

export const backup = async (fileName: string, errorLogger?: (msg: string) => void) => {
    try {
        await axios.post(
            `${API_URL}/admin/backup?fileName=${encodeURIComponent(fileName)}`,
            null,
            { withCredentials: true }
        )
    }catch(err: any) {
        handleAndThrowAuthError(err, errorLogger);
        if(err?.response?.status === 400) {
            errorLogger?.('文件名不合法')
            throw err;
        }
        logAndRethrowOtherError(err, errorLogger)
    }
}   