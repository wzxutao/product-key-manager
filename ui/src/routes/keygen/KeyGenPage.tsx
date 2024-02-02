import React from 'react';

import './KeyGenPage.less'
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { Backdrop, Box, Button, Checkbox, Chip, CircularProgress, Divider, FormControlLabel, Grid, Input, Paper, Stack, TextField } from '@mui/material';
import { useCallbackRef } from '../../common/hooks';
import { getMandatoryFields } from '../../http/keygen-api';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';

const INPUT_DATE_KEY = "__date";

export default function KeyGenPage() {
    const [alertMsg, handleAlert] = useAlert();

    const [todayChecked, setTodayChecked] = React.useState<boolean>(true);
    const [dateValid, setDateValid] = React.useState<boolean>(true);

    const [formRef, attachFormRef] = useCallbackRef<HTMLFormElement>();

    const [mandatoryFields, setMandatoryFields] = React.useState<string[] | null>(null);
    const [additionalFields, setAdditionalFields] = React.useState<string[]>([]);

    React.useEffect(() => {
        getMandatoryFields(handleAlert)
            .then((fields) => {
                setMandatoryFields(fields);
            })
            .catch((err) => {
                handleAlert(err.message);
            });
    }, []);

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
    }, [formRef]);

    const validateDate = React.useCallback((ev: React.FocusEvent<HTMLInputElement>) => {
        const regex = /^\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$/;
        setDateValid(regex.test(ev.target.value));
    }, [])

    const handleAddAdditionalField = React.useCallback(() => {
        setAdditionalFields((prev) => [...prev, '']);
    }, [])

    console.log(additionalFields);

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
                            xs={3} value='日期' />
                        <Grid item component={FormControlLabel} control={
                            <Checkbox value={todayChecked}
                                onChange={handleTodayCheckboxChange} />}
                            xs={1}
                            label="今天" />
                        <Grid item component={Input}
                            placeholder='YYMMDD'
                            id={INPUT_DATE_KEY}
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
                                xs={4} value={f} />
                            <Grid item component={Input}
                                placeholder={f}
                                id={f}
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
                    {additionalFields.map((f, i) => (
                        <div className="key-gen-form-row" key={i}>
                            <Grid item component={TextField} className='field-key' variant="filled"
                                xs={4} />
                            <Grid item component={Input}
                                placeholder={f}
                                xs={8} />
                        </div>
                    ))}
                </Grid>
            </Paper>

            <Paper id="key-gen-result-container" elevation={0}>
                <Grid container justifyContent='space-evenly'>
                    <Grid item component={Button} variant='contained' xs={2}
                        className="gen-btn"
                    >生成</Grid>
                    <Grid item component={Box} className="key-container" xs={8}>
                        <code>123123</code>
                    </Grid>
                </Grid>
            </Paper>
        </Stack>
    </>)
}