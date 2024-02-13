import React from 'react';

import './KeyGenPage.less'
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { Backdrop, Box, Button, Checkbox, Chip, CircularProgress, Container, Divider, FormControl, FormControlLabel, FormHelperText, Grid, Input, InputAdornment, OutlinedInput, Paper, Stack, TextField, TextareaAutosize } from '@mui/material';
import { useCallbackRef } from '../../common/hooks';
import { genKeys, getMandatoryFields } from '../../http/keygen-api';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import { EasyCopyChip } from '../../components/EasyCopyChip';
import { INPUT_DATE_KEY, RECORD_KEY_COMMENT } from '../../common/constants';


const BATCH_COUNT_MAX = 10;

export default function KeyGenPage() {
    const [alertMsg, handleAlert] = useAlert();

    const [todayChecked, setTodayChecked] = React.useState<boolean>(false);
    const [dateValid, setDateValid] = React.useState<boolean>(true);

    const [formRef, attachFormRef] = useCallbackRef<HTMLFormElement>();

    const [mandatoryFields, setMandatoryFields] = React.useState<string[] | null>(null);
    const [additionalFields, setAdditionalFields] = React.useState<string[]>([]);
    const [comment, setComment] = React.useState<string>('');
    const [batchGenCount, setBatchGenCount] = React.useState<number>(1);

    const [generatedKeys, setGeneratedKeys] = React.useState<string[]>([]);
    const [multiKeysSeparator, setMultiKeysSeparator] = React.useState<string>(' ');
    const [submitting, setSubmitting] = React.useState<boolean>(false);

    const validateDate = React.useCallback((ev: React.FocusEvent<HTMLInputElement>) => {
        const regex = /^\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$/;
        setDateValid(regex.test(ev.target.value));
    }, [])

    const handleTodayCheckboxChange = React.useCallback((ev: React.ChangeEvent<HTMLInputElement>) => {
        if (formRef === null) return;

        const checked = ev.target.checked;
        setTodayChecked(checked);

        const today = new Date();
        (formRef.elements.namedItem(INPUT_DATE_KEY)! as HTMLInputElement).value =
            checked
                ? '' + today.getFullYear().toString().slice(-2) +
                ('0' + (today.getMonth() + 1)).slice(-2) +
                ('0' + today.getDate()).slice(-2)
                : '';
        setDateValid(checked);
    }, [formRef]);

    React.useEffect(() => {
        if (mandatoryFields === null) {
            getMandatoryFields(handleAlert)
                .then((fields) => {
                    setMandatoryFields(fields);
                })
                .catch((_err) => {
                });
        }

        if (formRef !== null) {
            handleTodayCheckboxChange({ target: { checked: true } } as any);
        }
    }, [formRef]);

    const handleAddAdditionalField = React.useCallback(() => {
        setAdditionalFields((prev) => [...prev, '']);
    }, [])

    const handleSubmit = React.useCallback(async () => {
        if (formRef === null) return;
        if (batchGenCount > BATCH_COUNT_MAX) {
            handleAlert(`批量生成上限为${BATCH_COUNT_MAX}个`);
            return;
        }
        if (!dateValid) {
            handleAlert('日期格式错误');
            return;
        }

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
            const keys = await genKeys({
                count: batchGenCount,
                data: kvPairs
            }, handleAlert);

            setGeneratedKeys(keys);
            handleAlert(`生成了${keys.length}个序列号`, 'success');
        } catch (err) {

        } finally {
            setSubmitting(false);
        }


    }, [formRef, batchGenCount, handleAlert, dateValid])

    const handleCopyAll = React.useCallback(() => {
        const str = generatedKeys.join(multiKeysSeparator);
        navigator.clipboard.writeText(str);
        handleAlert('已复制: ' + str, 'success');
    }, [multiKeysSeparator, generatedKeys])

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
                    {/* date */}
                    <div className="key-gen-form-row">
                        <Grid item component={TextField} className='field-key' variant="filled" disabled
                            xs={3} defaultValue='日期' />
                        <Grid item component={FormControlLabel} control={
                            <Checkbox
                                checked={todayChecked}
                                onChange={handleTodayCheckboxChange} />}
                            xs={1}
                            label="今天" />
                        <Grid item component={Input}
                            placeholder='YYMMDD'
                            name={INPUT_DATE_KEY}
                            onBlur={validateDate}
                            error={!dateValid}
                            disabled={todayChecked} xs={8} />
                    </div>
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
                    <Grid item component={Button} variant='contained' xs={2}
                        className="gen-btn"
                        onClick={handleSubmit}
                        disabled={submitting}
                    >生成</Grid>
                    <Grid item component={FormControl} xs={1} variant="outlined">
                        <OutlinedInput
                            value={batchGenCount}
                            type='number'
                            inputProps={{
                                min: 1,
                                max: BATCH_COUNT_MAX,
                                step: 1,
                            }}
                            onChange={(ev) => { setBatchGenCount(parseInt(ev.target.value)) }}
                            endAdornment={<InputAdornment position="end">个</InputAdornment>}
                        />
                    </Grid>
                    <Grid item component={Container} className="key-container" xs={8}>
                        {
                            generatedKeys.map((key, i) => {
                                return <EasyCopyChip key={key} text={key} />
                            })
                        }

                        {generatedKeys.length > 1 && <>
                            <Divider />
                            <Container>
                                <OutlinedInput
                                    type='text'
                                    value={multiKeysSeparator}
                                    onChange={(ev) => { setMultiKeysSeparator(ev.target.value) }}
                                    startAdornment={<InputAdornment position="end">分隔符</InputAdornment>}
                                />
                                <Button variant='contained' onClick={handleCopyAll}>
                                    复制全部
                                </Button>
                            </Container>
                        </>}
                    </Grid>

                </Grid>
            </Paper>
        </Stack>
    </>)
}