import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"


export const getMandatoryFields = async (errorLogger?: ErrorLogger): Promise<string[]> => {
    try {
        const { data } = await axios.get(
            `${API_URL}/keygen/v2/mandatory-fields`,
            { withCredentials: true }
        )
        return data;
    }catch(err: any){
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export type genKeyRequest = {
    count: number;
    data: Record<string, string>;
}

export const genKeys = async (request: genKeyRequest, errorLogger?: ErrorLogger): Promise<string[]> => {
    try {
        const { data } = await axios.post(
            `${API_URL}/keygen/v2/new-key`,
            request,
            { withCredentials: true }
        )
        return data;
    }catch(err: any){
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}