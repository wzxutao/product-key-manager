import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

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

export type KeyGenStats = {
    keyLength: number,
    totalKeyCount: number,
    usedKeyCount: number,
    blackListedKeyCount: number,
    remainingKeyCount: number
}

export const getKeyGenStatus = async (errorLogger?: ErrorLogger): Promise<KeyGenStats> => {
    try {
        const { data } = await axios.get(
            `${API_BASE}/key-gen-stats/get`,
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

export const getAdminExpiry = async (errorLogger?: ErrorLogger): Promise<number> => {
    try {
        const { data } = await axios.get(
            `${API_BASE}/admin-expiry/get`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const updateAdminExpiry = async (length: number, errorLogger?: ErrorLogger) => {
    try {
        await axios.put(
            `${API_BASE}/admin-expiry/update?length=${length}`,
            null,
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const getKeyGenBlackList = async (errorLogger?: ErrorLogger): Promise<string[]> => {
    try {
        const { data } = await axios.get<string[]>(
            `${API_BASE}/key-gen-blacklist/get`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const updateKeyGenBlackList = async (blackList: string[], errorLogger?: ErrorLogger) => {
    try {
        await axios.post(
            `${API_BASE}/key-gen-blacklist/update`,
            blackList,
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const getNormalAccounts = async (errorLogger?: ErrorLogger): Promise<string[]> => {
    try {
        const { data } = await axios.get<string[]>(
            `${API_BASE}/normal-accounts/get`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const upsertNormalAccount = async (
    username: string, password: string, errorLogger?: ErrorLogger) => {
    try {
        await axios.post(
            `${API_BASE}/normal-accounts/upsert`,
            {
                username,
                password
            },
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const deleteNormalAccount = async (
    username: string, errorLogger?: ErrorLogger) => {
    try {
        await axios.delete(
            `${API_BASE}/normal-accounts/delete?username=${username}`,
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
    }
}

export const verifyNormalAccount = async (
    username: string, password: string, errorLogger?: ErrorLogger): Promise<boolean> => {
    try {
        const { data } = await axios.post<boolean>(
            `${API_BASE}/normal-accounts/verify`,
            {
                username,
                password
            },
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
        return Promise.reject(err);
    }
}
