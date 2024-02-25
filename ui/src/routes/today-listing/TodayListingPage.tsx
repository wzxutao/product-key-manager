import React from 'react';

import './TodayListingPage.less'
import { RecordDto } from '../../http/dto/record-dto';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import TodayListingResultTable, { TodayListingResultTableHandle } from './TodayListingResultTable';
import { Button, FormControl, InputLabel, NativeSelect, Stack } from '@mui/material';
import { StatusFilter, getMyTodayRecords } from '../../http/today-listing-api';
import { BatchDeleteButton } from './BatchDeleteButton';
import { useCallbackRef } from '../../common/hooks';


export function TodayListingPage() {
    const [data, setData] = React.useState<RecordDto[] | null>(null);
    const [alertMsg, handleAlert] = useAlert();
    const [tableRef, attachTableRef] = useCallbackRef<TodayListingResultTableHandle>();

    const [statusFilter, setStatusFilter] = React.useState<StatusFilter>('NORMAL');

    const handleLoadData = React.useCallback(() => {
        getMyTodayRecords(statusFilter, handleAlert).then(setData).catch()
    }, [statusFilter, handleAlert]);

    React.useEffect(() => {
        handleLoadData();
    }, [handleLoadData])

    const handleDataDeleted = React.useCallback(() => {
        handleAlert('删除成功', 'success');
        handleLoadData();
    }, [handleLoadData, handleAlert]);

    React.useEffect(() => {
        handleLoadData();
    }, [statusFilter, handleLoadData])

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Stack id="TodayListingPage" direction='column'>
                <Stack id="today-listing-page-tool-bar" direction='row' spacing={2}>
                    <BatchDeleteButton tableRef={tableRef} onDeleted={handleDataDeleted} />
                    <Button className="reload-btn" variant='outlined' onClick={handleLoadData}>重新加载</Button>
                    <FormControl>
                        <InputLabel variant="standard" htmlFor="uncontrolled-native">
                            记录状态
                        </InputLabel>
                        <NativeSelect
                            value={statusFilter}
                            onChange={ev => { 
                                setStatusFilter(ev.target.value as any)
                            }}
                        >
                            <option value='ALL'>全部</option>
                            <option value='NORMAL'>正常</option>
                            <option value='DELETED'>删除</option>
                        </NativeSelect>
                    </FormControl>
                </Stack>
                <TodayListingResultTable data={data} ref={attachTableRef} onChange={handleLoadData} />
            </Stack>
        </>
    )
}