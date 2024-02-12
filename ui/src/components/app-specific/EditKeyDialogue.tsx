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
import { RECORD_STATUS_DELETED, RECORD_STATUS_NORMAL } from '../../common/constants';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import { getMandatoryFields } from '../../http/keygen-api';
import './EditKeyDialogue.less'

export default function EditKeyDialog(props: {
    isAdmin?: boolean,
    record: RecordDto | null,
    onClose: () => void,
    onSubmit: (record: RecordDto) => void
}) {
    const { isAdmin, record, onClose, onSubmit } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();
    const [mandatoryFields, setMandatoryFields] = React.useState<string[] | null>(null);

    React.useEffect(() => {
        getMandatoryFields(handleAlert)
            .then((fields) => {
                setMandatoryFields(fields);
            })
            .catch((_err) => {
            });
    }, []);

    const handleAddAdditionalField = React.useCallback(() => {

    }, []);

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
                <DialogTitle textAlign='center'>修改记录</DialogTitle>
                <DialogContent >
                    {record &&
                        <Stack direction='column'>
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
                            <Paper sx={{ marginTop: '16px' }} elevation={0}>
                                <Stack direction='row' spacing={2} justifyContent='center' alignItems='center'>
                                    <Chip label="状态： " />
                                    <FormControl variant="standard">
                                        <Select
                                            value={record.status}
                                        >
                                            <MenuItem value={RECORD_STATUS_NORMAL}>正常</MenuItem>
                                            <MenuItem value={RECORD_STATUS_DELETED}>删除</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Stack>
                            </Paper>

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
                                    {[].map((f, i) => {
                                        if (f === undefined) return undefined;

                                        return <div className="key-gen-form-row" key={i}>
                                            <Grid item component={TextField} className='field-key' variant="filled"
                                                xs={4}
                                                value={f}
                                            />
                                            <Grid item component={Input}
                                                placeholder={f}
                                                name={f}
                                                xs={7} />
                                            <Grid item component={RemoveCircleOutlineIcon}
                                                className='remove-field-btn'
                                                xs={1} />
                                        </div>
                                    })}
                                </Grid>
                            </Paper>
                        </Stack>
                    }
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} disabled={submitting}>取消(Esc)</Button>
                    <Button disabled={submitting} variant='contained'>提交</Button>
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