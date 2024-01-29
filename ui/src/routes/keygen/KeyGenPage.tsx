import React from 'react';

import './KeyGenPage.less'
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { Stack } from '@mui/material';

export default function KeyGenPage() {
    const [alertMsg, handleAlert] = useAlert();

    return (<>
        <SnackbarAlert msg={alertMsg} />
        <Stack id="KeyGenPage">
        </Stack>
    </>)
}