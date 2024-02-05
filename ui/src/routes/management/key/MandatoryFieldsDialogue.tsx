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

        setNameHasError(false);
    }, [mandatoryFields])

    const handleReload = React.useCallback(() => {
        getMandatoryFields(handleAlert)
            .then((fields) => {
                setMandatoryFields(fields);
                setSeletedFieldIdx(0);
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



    const handleConfirm = React.useCallback(() => {

    }, [handleClose, handleAlert])


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
                                <Button>上移</Button>
                                <Button>下移</Button>
                                <Button>置顶</Button>
                                <Button>置底</Button>
                                <Button disabled></Button>
                                <Button color='error'>删除</Button>
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
                        />
                        <Stack direction='row' justifyContent='space-between' spacing={2}>
                            <Chip label="添加到: " />
                            <ButtonGroup size="small">
                                <Button>选中项上</Button>
                                <Button>选中项下</Button>
                                <Button>顶部</Button>
                                <Button>底部</Button>
                            </ButtonGroup>
                        </Stack>
                    </Container>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleReload} endIcon={<ReplayIcon />}>重置</Button>
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