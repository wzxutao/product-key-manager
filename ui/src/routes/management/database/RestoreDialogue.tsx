import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { backup as doBackup, getBackupFiles, restore } from '../../../http/admin-api';
import { CircularProgress, FormControlLabel, Radio, RadioGroup } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { SnackbarAlertMessage, useAlert } from '../../../components/SnackbarAlert';

export default function RestoreDialog(props: {
    open: boolean
    handleClose: () => void
}) {
    const { open, handleClose } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();
    const [backupFileNames, setBackupFileNames] = React.useState<string[] | null>(null);

    const [selectedFile, setSelectedFile] = React.useState<string | null>(null)

    React.useEffect(() => {
        if (!open) return;

        getBackupFiles(handleAlert)
            .then(setBackupFileNames)
    }, [open])

    const handleSubmit = React.useCallback(async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (selectedFile === null) return;
        setSubmitting(true);
        try {
            await restore(selectedFile, handleAlert)
            handleAlert('成功恢复，服务器即将重启', 'success')
            handleClose();
        } catch (e) { }
        finally {
            setSubmitting(false);
        }

    }, [selectedFile]);

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={open}
                onClose={(_, reason) => {
                    if (reason === 'escapeKeyDown')
                        handleClose();
                }}
                PaperProps={{
                    component: 'form',
                    onSubmit: handleSubmit,
                }}

            >
                <DialogTitle>从...恢复</DialogTitle>
                <RadioGroup
                    sx={{ margin: '0 8px' }}
                    onChange={(e) => setSelectedFile(e.target.value as string)}
                >
                    {backupFileNames === null && <CircularProgress />}
                    {backupFileNames && backupFileNames.map((name) =>
                        <FormControlLabel control={<Radio
                            disableRipple
                            color="default"
                            // checkedIcon={<BpCheckedIcon />}
                            // icon={<BpIcon />}
                            value={name}
                        />}
                            key={name}
                            label={name}
                        />

                    )}
                </RadioGroup>
                <DialogContent>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} disabled={submitting}>取消</Button>
                    <Button type="submit" disabled={submitting} variant='contained'>确定</Button>
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