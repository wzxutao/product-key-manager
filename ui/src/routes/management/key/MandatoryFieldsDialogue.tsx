import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { ButtonGroup, Chip, CircularProgress, Container, Divider, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Stack } from '@mui/material';
import { green } from '@mui/material/colors';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';
import { getMandatoryFields } from '../../../http/keygen-api';
import StarIcon from '@mui/icons-material/Star';
import ReplayIcon from '@mui/icons-material/Replay';
import { updateMandatoryFields } from '../../../http/admin-api';
import { INPUT_DATE_KEY } from '../../../common/constants';

export default function MandatoryFieldsDialogue(props: {
    open: boolean
    handleClose: () => void
}) {
    const { open, handleClose } = props;
    const [submitting, setSubmitting] = React.useState(false);
    const [alertMsg, handleAlert] = useAlert();

    const [mandatoryFields, setMandatoryFields] = React.useState<string[] | null>(null);
    const [selectedFieldIdx, setSeletedFieldIdx] = React.useState<number | null>(null);

    const [nameHasError, setNameHasError] = React.useState(false);

    const [newFieldName, setNewFieldName] = React.useState<string>('');

    const validateName = React.useCallback((ev: React.FocusEvent<HTMLInputElement>) => {
        const value = ev.target.value;

        if (value.length === 0) {
            setNameHasError(true);
            handleAlert("字段名不能为空")
            return;
        }

        if (mandatoryFields === null) {
            setNameHasError(false);
            return;
        }

        if (mandatoryFields.includes(value)) {
            setNameHasError(true);
            handleAlert("字段名不能重复")
            return;
        }

        if(value === INPUT_DATE_KEY) {
            setNameHasError(true);
            handleAlert("日期为固定字段名，不能使用")
            return;
        }

        setNameHasError(false);
    }, [mandatoryFields])

    const handleReload = React.useCallback(() => {
        getMandatoryFields(handleAlert)
            .then((fields) => {
                setMandatoryFields(fields);
                setSeletedFieldIdx(fields.length === 0 ? null : 0);
            })
            .catch((_err) => {
            });
    }, []);

    React.useEffect(() => {
        if (open) {
            handleReload();
        }
    }, [open])

    const mandatoryFieldList = React.useMemo(() => {
        return (
            <List
                sx={{ width: '100%', bgcolor: 'background.paper' }}
            >
                {mandatoryFields?.map((field, idx) => {
                    const selected = selectedFieldIdx === idx;

                    return (
                        <ListItem key={field} onClick={() => setSeletedFieldIdx(idx)}>
                            <ListItemButton>
                                {selected &&
                                    <ListItemIcon>
                                        <StarIcon />
                                    </ListItemIcon>
                                }
                                <ListItemText inset={!selected} primary={field} />
                            </ListItemButton>
                        </ListItem>
                    )
                })}
            </List>
        )
    }, [mandatoryFields, selectedFieldIdx])

    const handleMoveField = React.useCallback(
        (direction: 'up' | 'down' | 'top' | 'bottom', steps?: number) => () => {
            if (selectedFieldIdx === null || mandatoryFields === null) return;

            const stepsDefined = steps ?? 1;

            let fields = mandatoryFields;
            let targetIdx = 0;
            switch (direction) {
                case 'up':
                    targetIdx = selectedFieldIdx - stepsDefined;
                    break;
                case 'down':
                    targetIdx = selectedFieldIdx + stepsDefined;
                    break;
                case 'top':
                    targetIdx = 0;
                    break;
                case 'bottom':
                    targetIdx = fields.length - 1;
                    break;
            }

            targetIdx = Math.min(targetIdx, fields.length - 1);
            targetIdx = Math.max(targetIdx, 0);

            if (targetIdx === selectedFieldIdx) return;
            else if (targetIdx < selectedFieldIdx) {
                fields = fields.slice(0, targetIdx).concat(fields[selectedFieldIdx], fields.slice(targetIdx, selectedFieldIdx), fields.slice(selectedFieldIdx + 1));
            } else {
                fields = fields.slice(0, selectedFieldIdx).concat(fields.slice(selectedFieldIdx + 1, targetIdx + 1), fields[selectedFieldIdx], fields.slice(targetIdx + 1));
            }

            setMandatoryFields(fields);
            setSeletedFieldIdx(targetIdx);

        }, [mandatoryFields, selectedFieldIdx])

    const handleDeleteField = React.useCallback(() => {
        if (selectedFieldIdx === null || mandatoryFields === null) return;

        const fields = mandatoryFields.slice(0, selectedFieldIdx).concat(mandatoryFields.slice(selectedFieldIdx + 1));
        setMandatoryFields(fields);
        setSeletedFieldIdx(Math.max(Math.min(selectedFieldIdx, fields.length - 1), 0));
        if(fields.length === 0) {
            setSeletedFieldIdx(null);
        }
    }, [mandatoryFields, selectedFieldIdx])

    const handleDeleteAllFields = React.useCallback(() => {
        setMandatoryFields([]);
        setSeletedFieldIdx(null);
    }, []);


    const handleAddField = React.useCallback((direction: 'up' | 'down' | 'top' | 'bottom') => () => {
        const selectedFieldIdxDefined = selectedFieldIdx ?? 0;

        if (mandatoryFields === null) return;

        if (newFieldName.trim().length === 0) {
            handleAlert("字段名不能为空");
            return;
        }

        if (mandatoryFields.includes(newFieldName)) {
            handleAlert("字段名不能重复");
            return;
        }

        let fields = [...mandatoryFields];
        switch (direction) {
            case 'up':
                fields.splice(selectedFieldIdxDefined, 0, newFieldName);
                setSeletedFieldIdx(selectedFieldIdxDefined + 1);
                break;
            case 'down':
                fields.splice(selectedFieldIdxDefined + 1, 0, newFieldName);
                break;
            case 'top':
                fields.splice(0, 0, newFieldName);
                setSeletedFieldIdx(selectedFieldIdxDefined + 1);
                break;
            case 'bottom':
                fields.splice(fields.length, 0, newFieldName);
                break;
        }

        if (selectedFieldIdx === null) {
            setSeletedFieldIdx(0);
        }
        setMandatoryFields(fields);
    }, [mandatoryFields, selectedFieldIdx, newFieldName])

    const handleConfirm = React.useCallback(() => {
        updateMandatoryFields(mandatoryFields ?? [])
            .then(() => {
                handleAlert('更新成功', 'success');
                handleReload();
            }).catch(_err => { });
    }, [handleClose, handleAlert, mandatoryFields])


    return (
        <>
            <SnackbarAlert msg={alertMsg} />
            <Dialog
                open={open}
                onClose={(_, reason) => {
                    if (reason === 'escapeKeyDown')
                        handleClose();
                }}
            >
                <DialogTitle>序列号必填项</DialogTitle>
                <DialogContent>
                    <Container >
                        <Divider>现有</Divider>
                        <Container sx={{ 'display': 'flex', 'justifyContent': 'center' }}>
                            <ButtonGroup size="small">
                                <Button onClick={handleMoveField('up')}>上移</Button>
                                <Button onClick={handleMoveField('down')}>下移</Button>
                                <Button onClick={handleMoveField('top')}>置顶</Button>
                                <Button onClick={handleMoveField('bottom')}>置底</Button>
                                <Button disabled></Button>
                                <Button onClick={handleDeleteAllFields} color='error'>清空</Button>
                                <Button onClick={handleDeleteField} color='error'>删除</Button>
                            </ButtonGroup>
                        </Container>

                        {mandatoryFieldList}
                        <Divider>新增</Divider>
                        <TextField
                            required
                            margin="dense"
                            id="name"
                            name="name"
                            label="字段名"
                            type="text"
                            fullWidth
                            variant='outlined'
                            error={nameHasError}
                            onBlur={validateName}
                            value={newFieldName}
                            onChange={(ev) => setNewFieldName(ev.target.value)}
                        />
                        <Stack direction='row' justifyContent='space-between' spacing={2}>
                            <Chip label="添加到: " />
                            <ButtonGroup size="small">
                                <Button onClick={handleAddField('up')}>选中项上</Button>
                                <Button onClick={handleAddField('down')}>选中项下</Button>
                                <Button onClick={handleAddField('top')}>顶部</Button>
                                <Button onClick={handleAddField('bottom')}>底部</Button>
                            </ButtonGroup>
                        </Stack>
                    </Container>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleReload} endIcon={<ReplayIcon />}>重新加载</Button>
                    <Button onClick={handleClose} disabled={submitting}>取消</Button>
                    <Button onClick={handleConfirm} disabled={submitting} variant='contained'>提交</Button>
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