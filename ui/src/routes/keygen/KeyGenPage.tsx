import React from 'react';

import './KeyGenPage.less'
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';
import { Checkbox, FormControl, FormControlLabel, FormHelperText, Grid, Input, InputLabel, Stack, TextField } from '@mui/material';
import { Label } from '@mui/icons-material';
import { useCallbackRef } from '../../common/hooks';

const INPUT_DATE_KEY = "__date";

export default function KeyGenPage() {
    const [alertMsg, handleAlert] = useAlert();

    const [todayChecked, setTodayChecked] = React.useState<boolean>(true);
    const [dateValid, setDateValid] = React.useState<boolean>(true);

    const [formRef, attachFormRef] = useCallbackRef<HTMLFormElement>();

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
        console.log("blur")
        const regex = /^\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$/;
        setDateValid(regex.test(ev.target.value));
    }, [])

    return (<>
        <SnackbarAlert msg={alertMsg} />
        <Stack id="KeyGenPage" >
            <Grid
                id="key-gen-form"
                component="form"
                container
                ref={attachFormRef}
            >
                <div className="key-gen-form-row">
                    <Grid item component={TextField} className='mandatory-key' variant="filled" disabled xs={1} value='日期' />
                    <FormControlLabel control={
                        <Checkbox value={todayChecked}
                            onChange={handleTodayCheckboxChange} />}
                        sx={{ padding: '0 20px' }}
                        label="今天" />
                    <Grid item component={Input}
                        placeholder='YYMMDD'
                        id={INPUT_DATE_KEY}
                        onBlur={validateDate}
                        error={!dateValid}
                        disabled={todayChecked} xs={8} />
                </div>
            </Grid>
        </Stack>
    </>)
}