import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

export type QueryRecordCriterionOperator =
    'CREATED_MILLIS_BETWEEN' |
    'USERNAME_EQUALS' |
    'USERNAME_CONTAINS' |
    'STATUS_EQUALS' |
    'PAYLOAD_CONTAINS' |
    'FIELD_EQUALS';

export type QueryRecordCriterion = {
    child: QueryRecordCriterion,
    next: QueryRecordCriterion,
    connectedByAnd: boolean,
    operand1: string,
    operand2: string,
    operator: QueryRecordCriterionOperator
}

export type QueryRecordsRequest = {
    criterion: QueryRecordCriterion
}


export const queryRecords = async (
    request: QueryRecordsRequest,
    errorLogger: ErrorLogger
): Promise<RecordDto> => {
    try {
        const { data } = await axios.request(
            {
                method: 'GET',
                url: `${API_URL}/admin/listing/query-records`,
                withCredentials: true,
                data: request
            }
        )

        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}