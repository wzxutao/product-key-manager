import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"

const API_BASE = `${API_URL}/admin/man/v2`;

export const backup = async (fileName: string, errorLogger?: ErrorLogger) => {
    try {
        await axios.post(
            `${API_BASE}/backup?fileName=${encodeURIComponent(fileName)}`,
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

export const getBackupFiles = async (errorLogger?: ErrorLogger): Promise<string[]>=> {
    try {
        const {data} = await axios.get(
            `${API_BASE}/backup-files`,
            { withCredentials: true }
        )
        return data;
    }catch(err: any) { 
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const restore = async (fileName: string, errorLogger?: ErrorLogger) => {
    try {
        await axios.post(
            `${API_BASE}/restore?fileName=${encodeURIComponent(fileName)}`,
            null,
            { withCredentials: true }
        )
    }catch(err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}