import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { CircularProgress } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { RecordDto } from '../../http/dto/record-dto';

export default function EditKeyDialog(props: {
    isAdmin ?: boolean,
    record: RecordDto | null,
    onClose: () => void,
    onSubmit: (record: RecordDto) => void
}) {
    const { isAdmin, record, onClose, onSubmit } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={record !== null}
                onClose={(_, reason) => {
                    if(reason === 'escapeKeyDown')
                    onClose();
                }}
            >
                <DialogTitle>修改记录</DialogTitle>
                <DialogContent>
                    
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} disabled={submitting}>取消</Button>
                    <Button disabled={submitting} variant='contained'>提交</Button>
                    {submitting && <CircularProgress
                        size={24}
                        sx={{
                            color: green[500],
                            position: 'absolute',
                            top: '50%',
                            left: '50%',
                            marginTop: '-12px',
                            marginLeft: '-12px',
                        }}
                    />
                    }
                </DialogActions>
            </Dialog>
        </>
    );
}