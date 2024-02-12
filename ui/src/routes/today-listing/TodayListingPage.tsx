import React from 'react';

import './TodayListingPage.less'
import { RecordDto } from '../../http/dto/record-dto';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import TodayListingResultTable, { TodayListingResultTableHandle } from './TodayListingResultTable';
import { Button, Stack } from '@mui/material';
import { getMyTodayRecords } from '../../http/today-listing-api';
import { BatchDeleteButton } from './BatchDeleteButton';
import { useCallbackRef } from '../../common/hooks';

export function TodayListingPage() {
    const [data, setData] = React.useState<RecordDto[] | null>(null);
    const [alertMsg, handleAlert] = useAlert();
    const [tableRef, attachTableRef] = useCallbackRef<TodayListingResultTableHandle>();

    const handleLoadData = React.useCallback(() => {
        getMyTodayRecords(handleAlert).then(setData).catch(err => { })
    }, []);

    React.useEffect(() => {
        handleLoadData();
    }, [])

    const handleDataDeleted = React.useCallback(() => {
        handleAlert('删除成功', 'success');
        handleLoadData();
    }, []);

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Stack id="TodayListingPage" direction='column'>
                <Stack id="today-listing-page-tool-bar" direction='row' spacing={2}>
                    <BatchDeleteButton tableRef={tableRef} onDeleted={handleDataDeleted}/>
                    <Button className="reload-btn" variant='outlined' onClick={handleLoadData}>重新加载</Button>
                </Stack>
                <TodayListingResultTable data={data} ref={attachTableRef} onChange={handleLoadData} />
            </Stack>
        </>
    )
}