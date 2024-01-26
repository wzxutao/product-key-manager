import React from 'react'
import "./ListingPage.less"
import { Stack } from '@mui/material';
import ListingResultTable from './ListingResultTable';
import ListingFilterAccordion from './ListingFilterAccordion';
import { RecordDto } from "../../http/dto/record-dto";
import { queryRecords } from '../../http/listing-api';

export default function ListingPage() {

    const [data, setData] = React.useState<RecordDto[] | null> (null)

    React.useEffect(() => {
        queryRecords({}).then(setData)
    }, [])

    return (
        <Stack id="ListingPage">
            <ListingFilterAccordion />
            <ListingResultTable data={data}/>
        </Stack>
    );
}