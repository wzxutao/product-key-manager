import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { updateKeyLength } from '../../../http/admin-api';
import { CircularProgress } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';

export default function KeyLengthDialog(props: {
    currentLength: number | null
    open: boolean
    onClose: (refresh ?: boolean) => void
}) {
    const { open, onClose, currentLength } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();
    const [length, setLength] = React.useState<number>(currentLength ?? 0);

    React.useEffect(() => {
        setLength(currentLength ?? 0)
    }, [currentLength])


    const handleSubmit = React.useCallback(async () => {
        setSubmitting(true);
        try {
            await updateKeyLength(length, handleAlert);
            handleAlert('设置成功', 'success')
            onClose(true);
        } catch (e) {
        } finally {
            setSubmitting(false);
        }
    }, [length, onClose, handleAlert])


    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={open}
                onClose={(_, reason) => {
                    if (reason === 'escapeKeyDown')
                        onClose();
                }}
            >
                <DialogTitle>请输入序列号长度</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        required
                        margin="dense"
                        name="length"
                        label="长度"
                        type="number"
                        fullWidth
                        variant="standard"
                        inputProps={
                            {
                                min: 1,
                                max: 100,
                                step: 1
                            }
                        }
                        value={length}
                        onChange={e => setLength(parseInt(e.target.value) || 1)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => onClose()} disabled={submitting}>取消</Button>
                    <Button disabled={submitting} variant='contained' onClick={handleSubmit}>提交</Button>
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