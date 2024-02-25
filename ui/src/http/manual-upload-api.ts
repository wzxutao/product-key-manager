import axios from "axios";

import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"

const API_BASE = `${API_URL}/admin/manual-upload-record/v2`;

export const CONFLICT_RECORD_ERROR = new Error("CONFLICT_RECORD");

export const uploadRecord = async (record: {
    productKey: string,
    data: Record<string, string>
}, errorLogger: ErrorLogger) => {
    try {
         await axios.post(
            `${API_BASE}/upload`,
            record,
            { withCredentials: true }
        )
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        if(err.response.status === 409) {
            throw CONFLICT_RECORD_ERROR;
        }
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}