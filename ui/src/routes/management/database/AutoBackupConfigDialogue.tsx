import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { backup as doBackup, getAutoBackupTime, updateAutoBackupTime } from '../../../http/admin-api';
import { CircularProgress } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';

export default function AutoBackupConfigDialogue(props: {
    open: boolean
    handleClose: () => void
}) {
    const { open, handleClose } = props;
    const [loading, setLoading] = React.useState(true);
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const [time, setTime] = React.useState<string>('');
    

    React.useEffect(() => {
        getAutoBackupTime().then(rv => {
            setTime(rv.hour.toString().padStart(2, '0') + ':' + rv.minute.toString().padStart(2, '0'))
            setLoading(false);
        })
    }, [])


    const handleSubmit = React.useCallback(async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitting(true);
        try {
            const [hour, minute] = time.split(':');
            await updateAutoBackupTime({ 
                hour: parseInt(hour), 
                minute: parseInt(minute) });
            handleAlert('成功', 'success')
            handleClose();
        } catch (e) {
        } finally {
            setSubmitting(false);
        }
    }, [handleClose, handleAlert, time])


    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={open}
                onClose={(_, reason) => {
                    if(reason === 'escapeKeyDown')
                    handleClose();
                }}
                PaperProps={{
                    component: 'form',
                    onSubmit: handleSubmit,
                }}
            
            >
                <DialogTitle>每天备份的时间</DialogTitle>
                <DialogContent>
                    {loading && <CircularProgress />}
                    <TextField
                        autoFocus
                        required
                        margin="dense"
                        name="time"
                        type="time"
                        fullWidth
                        variant="standard"
                        value={time}
                        onChange={ev => setTime(ev.target.value)}
                        disabled={loading || submitting}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} disabled={submitting}>取消</Button>
                    <Button type="submit" disabled={submitting} variant='contained'>提交</Button>
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