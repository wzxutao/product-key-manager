import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

const ROOT_OPERATOR = "ROOT";

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
            `${API_URL}/admin/listing/query-records`,
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
            `${API_URL}/admin/listing/mandatory-fields`,
            { withCredentials: true }
        )
        return data;
    } catch (err: any) {
        handleAndThrowAuthError(err, errorLogger);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err);
    }
}