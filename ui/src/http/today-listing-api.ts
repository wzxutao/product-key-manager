import axios from "axios"
import { API_URL, ErrorLogger, handleAndThrowAuthError, logAndRethrowOtherError } from "./base-api"
import { RecordDto } from "./dto/record-dto";

export async function getMyTodayRecords(errorLogger?: ErrorLogger): Promise<RecordDto[]> {
    try {
        const { data } = await axios.get(
            `${API_URL}/normal/listing/my-today-records`,
            { withCredentials: true })
        return data;
    } catch (err) {
        handleAndThrowAuthError(err);
        logAndRethrowOtherError(err, errorLogger);
        return Promise.reject(err)
    }

}