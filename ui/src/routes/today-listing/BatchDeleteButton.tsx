import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import React from 'react';
import { TodayListingResultTableHandle } from './TodayListingResultTable';

export function BatchDeleteButton(props: {
    tableRef: TodayListingResultTableHandle | null
}) {
    const [open, setOpen] = React.useState(false);

    const handleClose = React.useCallback(() => {
        setOpen(false);
    }, []);

    return (
        <>
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
                        {props.tableRef?.getSelectedKeys().map(k => <li key={k}>{k}</li>)}
                    </ul>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>取消</Button>
                    <Button onClick={handleClose} autoFocus>
                        确认
                    </Button>
                </DialogActions>
            </Dialog>
            <Button variant='outlined' color='error' onClick={() => setOpen(true)}>批量删除</Button>
        </>

    )
}