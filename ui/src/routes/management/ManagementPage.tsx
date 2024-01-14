import { Stack } from '@mui/material'
import DatabaseAccordion from './DatabaseAccordion';
import './ManagementPage.less'
import KeyConfigAccordion from './KeyConfigAccordion';
import AccountConfigAccordion from './AccountConfigAccordion';

const ManagementPage = () => {
    return (
        <Stack id='ManagementPage'>
            <DatabaseAccordion />
            <KeyConfigAccordion />
            <AccountConfigAccordion />
        </Stack>
    )
}

export default ManagementPage;