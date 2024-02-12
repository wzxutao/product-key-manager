import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

const ROOT_OPERATOR = "ROOT";

const API_BASE = `${API_URL}/admin/listing/v2`;

export const rootCriterion: () => QueryRecordCriterion = () => ({
    children: [],
    operand1: '',
    operand2: '',
    operator: ROOT_OPERATOR
});

export function isRootCriterion(criterion: QueryRecordCriterion): boolean {
    return criterion.operator === ROOT_OPERATOR;
}

export type QueryRecordCriterion = QueryRecordCriterionUIExtension & {
    children: QueryRecordCriterion[],
    operand1: string,
    operand2: string,
    operator: string
}


type QueryRecordCriterionUIExtension = {
    helperText?: string
    parent?: QueryRecordCriterion
}

export type QueryRecordsRequest = {
    criterion?: QueryRecordCriterion
}


function withoutExtensionFields(original?: QueryRecordCriterion): QueryRecordCriterion | undefined {
    if(original === undefined) { return undefined;}

    const curr = {...original, helperText: undefined, parent: undefined};
    curr.children = original.children.map(withoutExtensionFields).filter(c => c !== undefined) as QueryRecordCriterion[];
    return curr;
}

export const queryRecords = async (
    request: QueryRecordsRequest,
    errorLogger?: ErrorLogger
): Promise<RecordDto[]> => {
    try {
        // remove extension fields

        const { data } = await axios.post(
            `${API_BASE}/query-records`,
            {
                ...request,
                criterion: withoutExtensionFields(request.criterion)
            },
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
        const { data } = await axios.get(
            `${API_BASE}/mandatory-fields`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}

export const getByProductKey = async (productKey: string, errorLogger?: ErrorLogger): Promise<RecordDto | null> => {
    try {
        const { data } = await axios.get<RecordDto>(
            `${API_BASE}/get-by-product-key?productKey=${productKey}`,
            { withCredentials: true }
        )
        return data || null;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger)
        return Promise.reject(err);
    }
}