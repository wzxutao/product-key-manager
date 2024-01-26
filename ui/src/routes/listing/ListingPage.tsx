
import "./ListingPage.less"
import { Stack } from '@mui/material';
import ListingResultTable from './ListingResultTable';
import ListingFilterAccordion from './ListingFilterAccordion';



export default function ListingPage() {
    return (
        <Stack id="ListingPage">
            <ListingFilterAccordion />
            <ListingResultTable />
        </Stack>
    );
}