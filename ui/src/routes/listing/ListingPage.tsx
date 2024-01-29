import React from 'react'
import "./ListingPage.less"
import { Stack } from '@mui/material';
import ListingResultTable from './ListingResultTable';
import ListingFilterAccordion from './ListingFilterAccordion';
import { RecordDto } from "../../http/dto/record-dto";
import { queryRecords } from '../../http/listing-api';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';

export default function ListingPage() {

    const [data, setData] = React.useState<RecordDto[] | null>(null);
    const [alertMsg, handleAlert] = useAlert();



    React.useEffect(() => {
        queryRecords({}, handleAlert).then(setData).catch(err => {})
    }, [handleAlert])

    return (<>
        <SnackbarAlert msg={alertMsg} />
        <Stack id="ListingPage">
            <ListingFilterAccordion />
            <ListingResultTable data={data} />
        </Stack>
    </>);
}