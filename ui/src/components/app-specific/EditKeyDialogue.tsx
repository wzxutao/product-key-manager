import React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Card, CardContent, Chip, CircularProgress, FormControl, MenuItem, Paper, Select, Stack, Typography } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { RecordDto } from '../../http/dto/record-dto';
import { RECORD_STATUS_DELETED, RECORD_STATUS_NORMAL } from '../../common/constants';
import './EditKeyDialogue.less';
import { useCallbackRef } from '../../common/hooks';
import RecordFieldsForm, { RecordFieldsFormHandle } from './RecordFieldsForm';

export default function EditKeyDialog(props: {
    record: RecordDto | null,
    onClose: () => void,
    onSubmit: (record: RecordDto) => Promise<any>
}) {
    const { record: pRecord, onClose, onSubmit } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const [record, setRecord] = React.useState<RecordDto | null>(pRecord);
    const [formRef, attachFormRef] = useCallbackRef<RecordFieldsFormHandle>();

    React.useEffect(() => {
        setRecord(pRecord);
    }, [pRecord]);

    const handleChangeStatus = React.useCallback((ev: any) => {
        if(record === null) return;

        setRecord({ ...record, ...formRef?.getData() ?? {}, status: ev.target.value as string });
    }, [record, formRef])

    const handleSubmit = React.useCallback(() => {
        if (record === null) return;
        if (formRef === null) return;

        setSubmitting(true);
        onSubmit({ ...record, ...formRef.getData() }).then(_ => {
            handleAlert('修改成功', 'success');
            onClose();
        })
            .catch(_ => {
                handleAlert('修改失败', 'error');
            })
            .finally(() => {
                setSubmitting(false);
            })
    }, [record, handleAlert, formRef, onClose, onSubmit])

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={record !== null}
                onClose={(_, reason) => {
                    if (reason === 'escapeKeyDown')
                        onClose();
                }}
                fullScreen
            >
                <DialogTitle textAlign='center' sx={{
                    color: 'white',
                    backgroundColor: '#1976d2'
                }}>修改记录</DialogTitle>
                <DialogContent >
                    {record &&
                        <Stack direction='column' mt='20px'>
                            {/* readonly */}
                            <Card sx={{ backgroundColor: '#cecece' }}>
                                <CardContent>
                                    <Typography variant="h5" component="div">
                                        {record.productKey}
                                    </Typography>
                                    <Typography sx={{ mb: 1.5 }} color="text.secondary">
                                        {record.username}
                                    </Typography>
                                    <Typography variant="body2">
                                        {new Date(record.createdMilli).toLocaleString()}
                                    </Typography>
                                </CardContent>
                            </Card>
                            {/* status */}
                            <Paper sx={{ marginTop: '16px' }} elevation={0}>
                                <Stack direction='row' spacing={2} justifyContent='center' alignItems='center'>
                                    <Chip label="状态：" />
                                    <FormControl variant="standard">
                                        <Select
                                            value={record.status}
                                            onChange={handleChangeStatus}
                                        >
                                            <MenuItem value={RECORD_STATUS_NORMAL}>正常</MenuItem>
                                            <MenuItem value={RECORD_STATUS_DELETED}>删除</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Stack>
                            </Paper>
                            {/* fields */}
                            <RecordFieldsForm record={record} ref={attachFormRef} />
                        </Stack>
                    }
                </DialogContent>
                <DialogActions>
                    <Stack direction='row' justifyContent='space-between' width='100%' spacing={2}>
                        <Button sx={{
                            flexGrow: 1
                        }} onClick={onClose} disabled={submitting} variant='outlined'>取消(Esc)</Button>
                        <Button sx={{
                            flexGrow: 1
                        }}
                            onClick={handleSubmit}
                            disabled={submitting} variant='contained'>提交</Button>
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
                    </Stack>
                </DialogActions>
            </Dialog>
        </>
    );
}