import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import React from 'react';
import { TodayListingResultTableHandle } from './TodayListingResultTable';
import { batchDeleteMyTodayRecords } from '../../http/today-listing-api';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';

export function BatchDeleteButton(props: {
    tableRef: TodayListingResultTableHandle | null
    onDeleted: () => void,
}) {
    const { tableRef, onDeleted } = props;
    const [open, setOpen] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const productKeys = React.useMemo<string[]>(() => {
        if (!tableRef) return [];
        if (!open) return [];

        return tableRef.getSelectedKeys();
    }, [tableRef, open])

    const handleClose = React.useCallback(() => {
        setOpen(false);
    }, []);

    const handleConfirm = React.useCallback(async () => {
        await batchDeleteMyTodayRecords(productKeys, handleAlert);
        handleClose();
        onDeleted();
    }, [productKeys, handleAlert, handleClose, onDeleted])

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>
                    确认删除?
                </DialogTitle>
                <DialogContent>
                    删除以下序列号:
                    <ul>
                        {productKeys.map(k => <li key={k}>{k}</li>)}
                    </ul>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>取消</Button>
                    <Button onClick={handleConfirm} disabled={productKeys.length === 0}>
                        确认
                    </Button>
                </DialogActions>
            </Dialog>
            <Button variant='outlined' color='error' onClick={() => setOpen(true)}>删除所选</Button>
        </>

    )
}