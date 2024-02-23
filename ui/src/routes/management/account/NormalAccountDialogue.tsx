import React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { deleteNormalAccount, getNormalAccounts, upsertNormalAccount, verifyNormalAccount } from '../../../http/admin-api';
import { CircularProgress, Container, Divider, IconButton, List, ListItem, ListItemButton, ListItemText, Tooltip } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';
import DeleteIcon from '@mui/icons-material/Delete';
import KeyIcon from '@mui/icons-material/Key';
import UserCredentialDialogue from './UserCredentialDialogue';

type CredentialDialogueMode = 'add' | 'edit' | 'verify' | 'delete' | 'off';

export default function NormalAccountDialogue(props: {
    open: boolean
    onClose: (refresh?: boolean) => void
}) {
    const { open, onClose } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const [normalAccounts, setNormalAccounts] = React.useState<string[] | null>(null);

    const [credentialsDialogMode, setCredentialsDialogMode] = React.useState<CredentialDialogueMode>('off');
    const [selectedUsername, setSelectedUsername] = React.useState<string | null>(null);

    const refreshAccounts = React.useCallback(() => {
        getNormalAccounts(handleAlert).then(setNormalAccounts).catch()
    }, [handleAlert])

    React.useEffect(() => {
        refreshAccounts();
    }, [refreshAccounts]);

    const handleCredentialsDialogueSubmit = React.useCallback(async (username: string, password: string) => {
        if (credentialsDialogMode === 'add' || credentialsDialogMode === 'edit') {
            setSubmitting(true);
            upsertNormalAccount(username, password, handleAlert)
                .then(_ => {
                    handleAlert("操作成功", 'success')
                    refreshAccounts();
                })
                .catch()
                .finally(() => {
                    setSubmitting(false);
                    setCredentialsDialogMode('off');
                })
        } else if (credentialsDialogMode === 'verify') {
            setSubmitting(true);
            verifyNormalAccount(username, password, handleAlert)
                .then(rv => {
                    if (rv === true) {
                        handleAlert("密码正确", 'success')
                        setCredentialsDialogMode('off');
                    } else if (rv === false) {
                        handleAlert("密码错误", 'error')
                    }
                })
                .catch()
                .finally(() => {
                    setSubmitting(false);
                })
        } else if (credentialsDialogMode === 'delete') {
            setSubmitting(true);
            deleteNormalAccount(username, handleAlert)
                .then(_ => {
                    handleAlert("删除成功", 'success')
                    refreshAccounts();
                })
                .catch()
                .finally(() => {
                    setSubmitting(false);
                    setCredentialsDialogMode('off');
                })
        }
    }, [credentialsDialogMode, handleAlert, refreshAccounts]);

    const credentialsDialogueTitle = React.useMemo(() => {
        if (credentialsDialogMode === 'add') {
            return '添加普通账号';
        } else if (credentialsDialogMode === 'edit') {
            return '修改密码';
        } else if (credentialsDialogMode === 'verify') {
            return '验证密码';
        } else if (credentialsDialogMode === 'delete') {
            return '确认删除账号？'
        }
        return undefined;
    }, [credentialsDialogMode]);

    const listItems = React.useMemo(() => {
        if (normalAccounts === null) return <CircularProgress />;
        return normalAccounts.map((username) => (

            <ListItem
                key={username}
                secondaryAction={[
                    <Tooltip key={username + '_edit'} title="修改密码" placement="right-start">
                        <IconButton edge="end" onClick={() => {
                            setSelectedUsername(username);
                            setCredentialsDialogMode('edit');
                        }}>
                            <KeyIcon />
                        </IconButton>
                    </Tooltip>,
                    <Tooltip key={username + '_delete'} title="删除账号" placement="right">
                        <IconButton edge="end" onClick={() => {
                            setSelectedUsername(username);
                            setCredentialsDialogMode('delete');
                        }}>
                            <DeleteIcon />
                        </IconButton>
                    </Tooltip>
                ]
                }
            >
                <Tooltip title="点击验证密码" placement="left">
                    <ListItemButton dense onClick={
                        () => {
                            setSelectedUsername(username);
                            setCredentialsDialogMode('verify');
                        }
                    }>
                        <ListItemText primary={username} />
                    </ListItemButton>
                </Tooltip>

            </ListItem>
        ));
    }, [normalAccounts])

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <UserCredentialDialogue
                title={credentialsDialogueTitle}
                open={credentialsDialogMode !== 'off'}
                onClose={() => setCredentialsDialogMode('off')}
                onSubmit={handleCredentialsDialogueSubmit}
                username={selectedUsername}
                hidePasswordInput={credentialsDialogMode === 'delete'}
            />
            <Dialog
                open={open}
                onClose={(_, reason) => {
                    if (reason === 'escapeKeyDown')
                        onClose();
                }}
            >
                <DialogTitle>普通账号</DialogTitle>
                <DialogContent>
                    <List sx={{ minWidth: '200px', maxHeight: '70vh', overflow: 'scroll' }}>
                        {listItems}
                    </List>
                    <Divider />
                    <Container sx={{ textAlign: 'center', margin: '4px auto 0' }}>
                        <Button variant='contained'
                            onClick={() => {
                                setCredentialsDialogMode('add');
                                setSelectedUsername(null);
                            }}
                            sx={{ width: '100%' }}>添加</Button>
                    </Container>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => onClose()} disabled={submitting} variant='contained'>返回</Button>
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