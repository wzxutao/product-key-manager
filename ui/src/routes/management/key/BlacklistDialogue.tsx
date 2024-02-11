import { Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, FormControl, FormLabel, Stack, TextareaAutosize } from '@mui/material';
import React from 'react';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';
import { green } from '@mui/material/colors';
import { getKeyGenBlackList, updateKeyGenBlackList } from '../../../http/admin-api';

export default function BlacklistDialogue(props: {
    open: boolean,
    onClose: (refresh?: boolean) => void
}) {
    const { open, onClose } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const [blackList, setBlackList] = React.useState<string[] | null>(null);

    const [text, setText] = React.useState<string>('');

    React.useEffect(() => {
        if (open) {
            getKeyGenBlackList(handleAlert).then(
                rv => {
                    setBlackList(rv);
                    setText(rv.join(', '));
                }).catch();
        }
    }, [open])

    const handleSubmit = React.useCallback(() => {
        setSubmitting(true);
        const reqBody = text.split(',').map(v => v.trim()).filter(v => v.length > 0);
        updateKeyGenBlackList(reqBody ?? [], handleAlert).then(() => {
            handleAlert('设置成功', 'success');
            onClose(true);
        }).catch().finally(() => {
            setSubmitting(false);
        })
    }, [text])

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
                <DialogTitle>请输入屏蔽词汇</DialogTitle>
                <DialogContent>
                    <Stack>
                        <FormControl>
                            <FormLabel>逗号分隔</FormLabel>
                            <TextareaAutosize placeholder="CNM, CSJ" minRows={2} 
                            onChange={(e) => {setText(e.target.value)}}
                            value={text}
                            disabled={submitting || blackList === null}
                            style={{
                                minWidth: '300px',
                                minHeight: '100px'
                            }} />
                        </FormControl>
                    </Stack>

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
    )
}