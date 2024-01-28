import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

export const ROOT_CRITERION: Readonly<QueryRecordCriterion> = Object.freeze({
    children: [],
    operand1: '',
    operand2: '',
    operator: "ROOT"
});

export function isRootCriterion(criterion: QueryRecordCriterion): boolean {
    return criterion.operator === ROOT_CRITERION.operator;
}

export type QueryRecordCriterion = {
    children: QueryRecordCriterion[],
    operand1: string,
    operand2: string,
    operator: string
    helperText?: string
}

export type QueryRecordsRequest = {
    criterion?: QueryRecordCriterion
}


export const queryRecords = async (
    request: QueryRecordsRequest,
    errorLogger?: ErrorLogger
): Promise<RecordDto[]> => {
    try {
        const { data } = await axios.post(
            `${API_URL}/admin/listing/query-records`,
            request,
            { withCredentials: true }
        )

        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const getMandatoryFields = async (errorLogger?: ErrorLogger): Promise<string[]> => {
    try {
        const {data} = await axios.get(
            `${API_URL}/admin/listing/mandatory-fields`,
            {withCredentials: true}
        )
        return data;
    }catch(err: any){
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}