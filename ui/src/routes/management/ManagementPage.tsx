import { Stack } from '@mui/material'
import DatabaseAccordion from './database/DatabaseAccordion';
import './ManagementPage.less'
import KeyConfigAccordion from './key/KeyConfigAccordion';
import AccountConfigAccordion from './account/AccountConfigAccordion';

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