import React from 'react'
import "./ListingPage.less"
import { Stack } from '@mui/material';
import ListingResultTable from './ListingResultTable';
import ListingFilterAccordion from './ListingFilterAccordion';
import { RecordDto } from "../../http/dto/record-dto";
import { QueryRecordCriterion, rootCriterion, queryRecords } from '../../http/listing-api';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import ListingToolBar from './ListingToolBar';

export default function ListingPage() {

    const [data, setData] = React.useState<RecordDto[] | null>(null);
    const [alertMsg, handleAlert] = useAlert();
    const [rootCr, setRootCr] = React.useState<QueryRecordCriterion>(rootCriterion());


    const onQueryRecords = React.useCallback(() => {
        setData(null)
        queryRecords({
            criterion: rootCr
        }, handleAlert).then(setData).catch(err => {})
    }, [handleAlert, rootCr])

    React.useEffect(() => {
        onQueryRecords()
    }, []) // eslint-disable-line react-hooks/exhaustive-deps

    return (<>
        <SnackbarAlert msg={alertMsg} />
        <Stack id="ListingPage">
            <ListingFilterAccordion rootCr={rootCr} setRootCr={setRootCr}/>
            <ListingToolBar onQuery={onQueryRecords}/>
            <ListingResultTable data={data} />
        </Stack>
    </>);
}