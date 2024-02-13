import React from 'react';

import './ManualUploadPage.less'
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { Backdrop, Box, Button, Checkbox, Chip, CircularProgress, Container, Divider, FormControl, FormControlLabel, FormHelperText, Grid, Input, InputAdornment, OutlinedInput, Paper, Stack, TextField, TextareaAutosize } from '@mui/material';
import { useCallbackRef } from '../../common/hooks';
import { genKeys, getMandatoryFields } from '../../http/keygen-api';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import { INPUT_DATE_KEY, RECORD_KEY_COMMENT } from '../../common/constants';


const BATCH_COUNT_MAX = 10;

export default function ManualUploadPage() {
    const [alertMsg, handleAlert] = useAlert();

    const [dateValid, setDateValid] = React.useState<boolean>(true);

    const [formRef, attachFormRef] = useCallbackRef<HTMLFormElement>();

    const [mandatoryFields, setMandatoryFields] = React.useState<string[] | null>(null);
    const [additionalFields, setAdditionalFields] = React.useState<string[]>([]);
    const [comment, setComment] = React.useState<string>('');

    const [submitting, setSubmitting] = React.useState<boolean>(false);

    const validateDate = React.useCallback((ev: React.FocusEvent<HTMLInputElement>) => {
        const regex = /^\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$/;
        setDateValid(regex.test(ev.target.value));
    }, [])


    React.useEffect(() => {
        if (mandatoryFields === null) {
            getMandatoryFields(handleAlert)
                .then((fields) => {
                    setMandatoryFields(fields);
                })
                .catch((_err) => {
                });
        }

    }, []);

    const handleAddAdditionalField = React.useCallback(() => {
        setAdditionalFields((prev) => [...prev, '']);
    }, [])

    const handleSubmit = React.useCallback(async () => {
        if (formRef === null) return;

        setSubmitting(true);

        const valueInputs: HTMLInputElement[] = [];
        for (let i = 0; i < formRef.elements.length; i++) {
            const el = formRef.elements[i];

            if (!el.hasAttribute("name")) continue;
            if (el.nodeName !== "INPUT") continue;

            if (el.getAttribute("name")!.trim().length === 0) continue;

            valueInputs.push(el as any);
        }

        const kvPairs: Record<string, string> = {}
        for (const el of valueInputs) {
            kvPairs[el.getAttribute("name")!] = el.value;
        }

        kvPairs[RECORD_KEY_COMMENT] = comment;

        try {
        } catch (err) {

        } finally {
            setSubmitting(false);
        }


    }, [formRef, handleAlert, dateValid])


    return (<>
        <SnackbarAlert msg={alertMsg} />
        <Stack id="KeyGenPage" >
            <Paper id="key-gen-form-container" elevation={3}>
                <Grid
                    id="key-gen-form"
                    component="form"
                    container
                    ref={attachFormRef}
                >
                    <Grid item className="form-divider" component={Divider} xs={12}>
                        <Chip label="必填项" size="small" />
                    </Grid>

                    {/* mandatory fields*/}
                    <Backdrop
                        sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
                        open={mandatoryFields === null}
                    >
                        <CircularProgress color="inherit" />
                    </Backdrop>
                    {mandatoryFields?.map(f =>
                        <div key={f} className="key-gen-form-row">
                            <Grid item component={TextField} className='field-key' variant="filled" disabled
                                xs={4} defaultValue={f} />
                            <Grid item component={Input}
                                placeholder={f}
                                name={f}
                                xs={8} />
                        </div>
                    )}
                    {/* additional fields*/}
                    <Grid item className="form-divider" component={Divider} xs={12}>
                        <Chip label="额外项" size="small" />
                    </Grid>

                    <Grid item component={AddCircleOutlineIcon}
                        className="add-field-btn"
                        onClick={handleAddAdditionalField}
                        xs={12} />
                    {additionalFields.map((f, i) => {
                        if (f === undefined) return undefined;

                        return <div className="key-gen-form-row" key={i}>
                            <Grid item component={TextField} className='field-key' variant="filled"
                                xs={4}
                                onChange={(ev: any) => {
                                    setAdditionalFields((prev) => {
                                        const rv = [...prev];
                                        rv[i] = ev.target.value;
                                        return rv;
                                    });
                                }}
                                value={f}
                            />
                            <Grid item component={Input}
                                placeholder={f}
                                name={f}
                                xs={7} />
                            <Grid item component={RemoveCircleOutlineIcon}
                                className='remove-field-btn'
                                onClick={() => {
                                    setAdditionalFields((prev) => {
                                        const rv = [...prev];
                                        delete rv[i];
                                        return rv;
                                    });
                                }}
                                xs={1} />
                        </div>
                    })}
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

            <Paper id="key-gen-result-container" elevation={0}>
                <Grid container justifyContent='space-evenly'>


                    <Grid item component={Container} className="key-container" xs={8}>
                        <TextField
                        fullWidth
                            label="序列号"
                            variant="standard"
                        />
                    </Grid>
                    <Grid item component={Button} variant='contained' xs={4}
                        className="gen-btn"
                        onClick={handleSubmit}
                        disabled={submitting}
                    >提交</Grid>

                </Grid>
            </Paper>
        </Stack>
    </>)
}