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
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        if (err?.response?.status === 400) {
            errorLogger?.('文件名不合法')
            throw err;
        }
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const getBackupFiles = async (errorLogger?: ErrorLogger): Promise<string[]> => {
    try {
        const { data } = await axios.get(
            `${API_BASE}/backup-files`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
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
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const updateMandatoryFields = async (fieldNames: string[], errorLogger?: ErrorLogger) => {
    try {
        await axios.put(
            `${API_BASE}/mandatory-fields/update`,
            fieldNames,
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const getKeyLength = async (errorLogger?: ErrorLogger): Promise<number> => {
    try {
        const { data } = await axios.get(
            `${API_BASE}/key-length/get`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const updateKeyLength = async (length: number, errorLogger?: ErrorLogger) => {
    try {
        await axios.put(
            `${API_BASE}/key-length/update?length=${length}`,
            null,
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}