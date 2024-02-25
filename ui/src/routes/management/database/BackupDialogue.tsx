import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { backup as doBackup } from '../../../http/admin-api';
import { CircularProgress } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';

export default function BackupDialog(props: {
    open: boolean
    handleClose: () => void
}) {
    const { open, handleClose } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const handleSubmit = React.useCallback(async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitting(true);
        const formData = new FormData(event.currentTarget);
        const formJson = Object.fromEntries((formData as any).entries());
        const fileName = formJson.name;
        try {
            await doBackup(fileName, handleAlert);
            handleAlert('备份成功', 'success')
            handleClose();
        } catch (e) {
        } finally {
            setSubmitting(false);
        }
    }, [handleClose, handleAlert])


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
                <DialogTitle>请输入备份文件名</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        required
                        margin="dense"
                        name="name"
                        label="文件名"
                        type="text"
                        fullWidth
                        variant="standard"
                        defaultValue={(() => {
                            const now = new Date();
                            return 'manual-' +
                                `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}-` +
                                `${now.getHours()}-${String(now.getMinutes()).padStart(2, '0')}-${String(now.getSeconds()).padStart(2, '0')}`
                        })()}
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