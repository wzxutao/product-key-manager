import React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { CircularProgress, Stack, TextField } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';

export default function UserCredentialDialogue(props: {
    title?: string
    username: string | null
    open: boolean
    onClose: () => void
    onSubmit: (username: string, password: string) => Promise<void>
}) {
    const { title, username: pUsername, open, onClose, onSubmit } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const [username, setUsername] = React.useState<string>('');
    const [password, setPassword] = React.useState<string>('');

    React.useEffect(() => {
        if (pUsername !== null) {
            setUsername(pUsername);
        }else{
            setUsername('')
        }

        setPassword('');
    }, [open, pUsername])


    const handleSubmit = React.useCallback(() => {
        setSubmitting(true);
        onSubmit(username, password).finally(() => {
            setSubmitting(false);
        })
    }, [username, password])

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
                <DialogTitle>{title ?? '请输入用户名和密码'}</DialogTitle>
                <DialogContent>
                    <Stack direction='column' sx={{
                        minWidth: '300px',
                        margin: '20px auto'
                    }}>
                        <TextField label='用户名'
                            value={username}
                            onChange={ev => setUsername(ev.target.value)}
                            disabled={props.username !== null} />
                        <TextField label='密码'
                            onChange={ev => setPassword(ev.target.value)}
                            value={password} />
                    </Stack>
                </DialogContent>
                <DialogActions>
                <Button onClick={onClose} disabled={submitting}>返回</Button>
                    <Button onClick={handleSubmit} disabled={submitting} variant='contained'>提交</Button>
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