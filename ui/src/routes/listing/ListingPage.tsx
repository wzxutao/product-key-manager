import React from 'react'
import { Stack } from '@mui/material';
import ListingResultTable from './ListingResultTable';
import ListingFilterAccordion from './ListingFilterAccordion';
import { RecordDto } from "../../http/dto/record-dto";
import { QueryRecordCriterion, rootCriterion, queryRecords } from '../../http/listing-api';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import ListingToolBar from './ListingToolBar';
import "./ListingPage.less"
import { getByProductKey } from '../../http/listing-api';
import { quickSearchRecords } from '../../http/listing-api';


export default function ListingPage() {

    const [data, setData] = React.useState<RecordDto[] | null>(null);
    const [alertMsg, handleAlert] = useAlert();
    const [rootCr, setRootCr] = React.useState<QueryRecordCriterion>(rootCriterion());


    const onQueryRecords = React.useCallback(() => {
        setData(null)
        queryRecords({
            criterion: rootCr
        }, handleAlert).then(setData).catch()
    }, [handleAlert, rootCr])

    const handleQueryByProductKey = React.useCallback((productKey: string) => {
        setData(null);
        getByProductKey(productKey)
        .then(rv => setData(rv !== null ? [rv] : [])).catch()
    }, [handleAlert])

    const handleQuickSearch = React.useCallback((searchInput: string) => {
        setData(null);
        quickSearchRecords(searchInput)
        .then(rv => setData(rv)).catch()
    }, [handleAlert])

    React.useEffect(() => {
        onQueryRecords()
    }, []) // eslint-disable-line react-hooks/exhaustive-deps

    return (<>
        <SnackbarAlert msg={alertMsg} />
        <Stack id="ListingPage">
            <ListingToolBar onQueryByProductKey={handleQueryByProductKey} onQuickSearch={handleQuickSearch}/>
            <ListingFilterAccordion rootCr={rootCr} setRootCr={setRootCr} onQuery={onQueryRecords} />
            <ListingResultTable data={data} onChange={onQueryRecords} />
        </Stack>
    </>);
}