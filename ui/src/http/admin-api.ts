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

export const getBackupFiles = async (errorLogger?: (msg: string) => void): Promise<string[]>=> {
    try {
        const {data} = await axios.get(
            `${API_URL}/admin/backup-files`,
            { withCredentials: true }
        )
        return data;
    }catch(err: any) { 
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        throw err;
    }
}

export const restore = async (fileName: string, errorLogger?: (msg: string) => void) => {
    try {
        await axios.post(
            `${API_URL}/admin/restore?fileName=${encodeURIComponent(fileName)}`,
            null,
            { withCredentials: true }
        )
    }catch(err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}