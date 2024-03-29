import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

const API_BASE = `${API_URL}/normal/listing/v2`;

export type StatusFilter = 'ALL' | 'NORMAL' | 'DELETED';


export async function getMyTodayRecords(statusFilter ?: StatusFilter, errorLogger?: ErrorLogger): Promise<RecordDto[]> {
    let statusQuery = '';
    if(statusFilter === 'NORMAL'){
        statusQuery = '?onlyNormal=true';
    }else if(statusFilter === 'DELETED'){
        statusQuery = '?onlyDeleted=true';
    }

    try {
        const { data } = await axios.get(
            `${API_BASE}/my-today-records${statusQuery}`,
            { withCredentials: true })
        return data;
    } catch (err) {
        handleAndThrowAuthError(err);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err)
    }

}

export async function batchDeleteMyTodayRecords(productKeys: string[], errorLogger?: ErrorLogger) {
    const url = new URL(`${API_BASE}/batch-delete`);
    const params = url.searchParams;
    productKeys.forEach((productKey) => params.append('productKey', productKey));

    try {
        await axios.delete(
            url.toString(),
            {withCredentials: true}
        )
    }catch(err){
        handleAndThrowAuthError(err);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err)
    }
}

export const updateRecord = async (record: RecordDto, errorLogger?: ErrorLogger) => {
    try {
        await axios.put(
            `${API_BASE}/update-record`,
            record,
            { withCredentials: true }
        )
    }catch(err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
        return Promise.reject(err);
    }
}