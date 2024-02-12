import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Backdrop, Card, CardContent, Chip, CircularProgress, Divider, FormControl, Grid, Input, MenuItem, Paper, Select, Stack, Typography } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { RecordDto } from '../../http/dto/record-dto';
import { INPUT_DATE_KEY, RECORD_STATUS_DELETED, RECORD_STATUS_NORMAL } from '../../common/constants';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import { getMandatoryFields } from '../../http/keygen-api';
import './EditKeyDialogue.less';

type KVPair = {
    k: string,
    v: string
}

export default function EditKeyDialog(props: {
    record: RecordDto | null,
    onClose: () => void,
    onSubmit: (record: RecordDto) => Promise<any>
}) {
    const { record: pRecord, onClose, onSubmit } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();
    const [mandatoryFieldKeys, setMandatoryFieldKeys] = React.useState<string[] | null>(null);

    const [record, setRecord] = React.useState<RecordDto | null>(pRecord);

    const [mandatoryFields, setMandatoryFields] = React.useState<KVPair[]>([]);
    const [optionalFields, setOptionalFields] = React.useState<KVPair[]>([]);

    React.useEffect(() => {
        if (pRecord === null) {
            setRecord(null);
            setMandatoryFields([]);
            setOptionalFields([]);
            return;
        }

        if (mandatoryFieldKeys === null) return;

        const r: RecordDto = JSON.parse(JSON.stringify(pRecord));

        const man: KVPair[] = mandatoryFieldKeys.map(k => ({
            k,
            v: r.expandedAllFields[k] ?? ''
        }));
        const opt: KVPair[] = [];



        Object.getOwnPropertyNames(r.expandedAllFields).forEach((k) => {
            if (k === INPUT_DATE_KEY) return;

            if (!mandatoryFieldKeys.includes(k)) {
                opt.push({
                    k: k,
                    v: r.expandedAllFields[k]
                });
            }
        })

        setMandatoryFields(man);
        setOptionalFields(opt);
        r.expandedAllFields = {}
        setRecord(r);
    }, [pRecord, mandatoryFieldKeys])

    React.useEffect(() => {
        getMandatoryFields(handleAlert)
            .then((fields) => {
                setMandatoryFieldKeys(fields);
            })
            .catch((_err) => {
            });
    }, []);

    const handleAddAdditionalField = React.useCallback(() => {
        setOptionalFields(prev => [...prev, { k: '', v: '' }])
    }, []);

    const mandatoryFieldRows = React.useMemo(() => {
        return mandatoryFields.map((kv, idx) => {
            if(kv === undefined) return undefined;

            const { k, v } = kv;

            return (<div key={k} className="key-gen-form-row">
                <Grid item component={TextField} className='field-key' variant="filled"
                    disabled
                    xs={4} defaultValue={k} />
                <Grid item component={Input}
                    value={v}
                    onChange={ev => {
                        mandatoryFields[idx] = {
                            k,
                            v: ev.target.value
                        }
                        setMandatoryFields([...mandatoryFields])
                    }}
                    xs={8} />
            </div>
            )
        })
    }, [mandatoryFields])

    const optionalFieldRows = React.useMemo(() => {
        const rv: JSX.Element[] = []

        optionalFields.map((f, idx) => {
            if(f === undefined) return undefined;

            const { k, v } = f;

            rv.push(
                <div className="key-gen-form-row" key={idx}>
                    <Grid item component={TextField} className='field-key' variant="filled"
                        xs={4}
                        value={k}
                        onChange={(ev: any) => {
                            optionalFields[idx] = {
                                k: ev.target.value,
                                v
                            };
                            setOptionalFields([...optionalFields])
                        }}
                    />
                    <Grid item component={Input}
                        value={v}
                        onChange={ev => {
                            optionalFields[idx] = {
                                k,
                                v: ev.target.value
                            }

                            setOptionalFields([...optionalFields]);
                        }}
                        xs={7} />
                    <Grid item component={RemoveCircleOutlineIcon}
                        className='remove-field-btn'
                        onClick={_ => {
                            delete optionalFields[idx];
                            setOptionalFields([...optionalFields])
                        }}
                        xs={1} />
                </div>)
        })


        return rv;
    }, [optionalFields]);

    const handleSubmit = React.useCallback(() => {
        if (record === null) return;

        const fields: Record<string, string> = {}
        for (const kv of [...mandatoryFields, ...optionalFields]) {
            if (kv === undefined) continue;

            const { k, v } = kv;
            fields[k] = v;
        }

        setSubmitting(true);
        onSubmit({ ...record, expandedAllFields: fields }).then(_ => {
            handleAlert('修改成功', 'success');
            onClose();
        })
            .catch(_ => {
                handleAlert('修改失败', 'error');
            })
            .finally(() => {
                setSubmitting(false);
            })
    }, [record, mandatoryFields, optionalFields, handleAlert])

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
                                            onChange={ev => {
                                                setRecord({ ...record, status: ev.target.value });
                                            }}
                                        >
                                            <MenuItem value={RECORD_STATUS_NORMAL}>正常</MenuItem>
                                            <MenuItem value={RECORD_STATUS_DELETED}>删除</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Stack>
                            </Paper>
                            {/* fields */}
                            <Paper sx={{ marginTop: '16px' }}>
                                <Grid
                                    className="edit-key-form"
                                    component="form"
                                    container
                                >
                                    <Grid item className="form-divider" component={Divider} xs={12}>
                                        <Chip label="必填项" size="small" />
                                    </Grid>
                                    {/* mandatory fields*/}
                                    <Backdrop
                                        sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
                                        open={mandatoryFieldKeys === null}
                                    >
                                        <CircularProgress color="inherit" />
                                    </Backdrop>
                                    {mandatoryFieldRows}
                                    {/* additional fields*/}
                                    <Grid item className="form-divider" component={Divider} xs={12}>
                                        <Chip label="额外项" size="small" />
                                    </Grid>

                                    <Grid item component={AddCircleOutlineIcon}
                                        className="add-field-btn"
                                        onClick={handleAddAdditionalField}
                                        xs={12} />
                                    {optionalFieldRows}
                                </Grid>
                            </Paper>
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