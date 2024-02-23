import { Backdrop, Chip, CircularProgress, Divider, Grid, Input, Paper, TextField, TextareaAutosize } from '@mui/material';
import React from 'react';
import { RecordDto } from '../../http/dto/record-dto';
import { getMandatoryFields } from '../../http/keygen-api';
import SnackbarAlert, { useAlert } from '../SnackbarAlert';
import { INPUT_DATE_KEY } from '../../common/constants';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

type KVPair = {
    k: string,
    v: string
}

export interface RecordFieldsFormHandle {
    getData: () => Pick<RecordDto, 'expandedAllFields' | 'comment'>;
}

export interface RecordFieldsFormProps {
    record: RecordDto | null
}

export default React.forwardRef<RecordFieldsFormHandle, RecordFieldsFormProps>((props, ref) => {
    const { record: pRecord } = props;

    const [mandatoryFields, setMandatoryFields] = React.useState<KVPair[]>([]);
    const [optionalFields, setOptionalFields] = React.useState<KVPair[]>([]);
    const [comment, setComment] = React.useState<string>('');

    const [mandatoryFieldKeys, setMandatoryFieldKeys] = React.useState<string[] | null>(null);
    const [alertMsg, handleAlert] = useAlert();

    React.useImperativeHandle(ref, () => {
        return {
            getData: () => {
                const fields: Record<string, string> = {}
                for (const kv of [...mandatoryFields, ...optionalFields]) {
                    if (kv === undefined) continue;

                    const { k, v } = kv;
                    fields[k] = v;
                }

                return {
                    expandedAllFields: fields,
                    comment
                }
            }
        }
    }, [mandatoryFields, optionalFields, comment])

    React.useEffect(() => {
        if (pRecord === null) {
            setComment('');
            setMandatoryFields([]);
            setOptionalFields([]);
            return;
        }

        if (mandatoryFieldKeys === null) return;

        setComment(pRecord.comment ?? '');

        const man: KVPair[] = mandatoryFieldKeys.map(k => ({
            k,
            v: pRecord.expandedAllFields[k] ?? ''
        }));
        const opt: KVPair[] = [];



        Object.getOwnPropertyNames(pRecord.expandedAllFields).forEach((k) => {
            if (k === INPUT_DATE_KEY) return;

            if (!mandatoryFieldKeys.includes(k)) {
                opt.push({
                    k: k,
                    v: pRecord.expandedAllFields[k]
                });
            }
        })

        setMandatoryFields(man);
        setOptionalFields(opt);
    }, [pRecord, mandatoryFieldKeys])

    React.useEffect(() => {
        getMandatoryFields(handleAlert)
            .then((fields) => {
                setMandatoryFieldKeys(fields);
            })
            .catch((_err) => {
            });
    }, [handleAlert]);

    const handleAddAdditionalField = React.useCallback(() => {
        setOptionalFields(prev => [...prev, { k: '', v: '' }])
    }, []);

    const mandatoryFieldRows = React.useMemo(() => {
        return mandatoryFields.map((kv, idx) => {
            if (kv === undefined) return undefined;

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

        optionalFields.forEach((f, idx) => {
            if (f === undefined) return undefined;

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

    return (
        <>
            <SnackbarAlert msg={alertMsg} />
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
                    <Grid item className="form-divider" component={Divider} xs={12}>
                        <Chip label="备注" size="small" />
                    </Grid>
                    <div className="key-gen-form-row">
                        <TextareaAutosize minRows={2} style={{
                            marginTop: '4px',
                            width: '100%',
                            resize: 'none',
                        }}
                            value={comment}
                            onChange={(ev => { setComment(ev.target.value) })}
                        />
                    </div>
                </Grid>
            </Paper>
        </>);
});