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
import SnackbarAlert, { SnackbarAlertMessage } from '../../../components/SnackbarAlert';

export default function BackupDialog(props: {
    open: boolean
    handleClose: () => void
}) {
    const { open, handleClose } = props;
    const [working, setWorking] = React.useState(false);
    const [alertMsg, setAlertMsg] = React.useState<SnackbarAlertMessage | undefined>(undefined);
    const handleAlert = React.useCallback((msg: string, severity?: SnackbarAlertMessage['severity']) => {
        setAlertMsg({ text: msg, severity });
    }, [])

    const handleConfirm = React.useCallback(async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setWorking(true);
        const formData = new FormData(event.currentTarget);
        const formJson = Object.fromEntries((formData as any).entries());
        const fileName = formJson.name;
        try {
            await doBackup(fileName, handleAlert);
            handleAlert('备份成功', 'success')
            handleClose();
        } catch (e) {
        } finally {
            setWorking(false);
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
                    onSubmit: handleConfirm,
                }}
            
            >
                <DialogTitle>请输入备份文件名</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        required
                        margin="dense"
                        id="name"
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
                    <Button onClick={handleClose} disabled={working}>取消</Button>
                    <Button type="submit" disabled={working} variant='contained'>提交</Button>
                    {working && <CircularProgress
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