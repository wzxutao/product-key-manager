import * as React from 'react';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Button, CircularProgress, Divider, Stack, TextField } from '@mui/material';
import ChecklistIcon from '@mui/icons-material/Checklist';
import SettingsIcon from '@mui/icons-material/Settings';
import BlockIcon from '@mui/icons-material/Block';
import MandatoryFieldsDialogue from './MandatoryFieldsDialogue';
import { getKeyLength } from '../../../http/admin-api';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';
import KeyLengthDialog from './KeyLengthDialogue';

export default function KeyConfigAccordion() {
  const [mandatoryFieldsDialogueOpen, setMandatoryFieldsDialogueOpen] = React.useState(false);

  const [KeyLengthDialogOpen, setKeyLengthDialogOpen] = React.useState(false);
  const [keyLength, setKeyLength] = React.useState<number | null>(null);
  const [alertMsg, handleAlert] = useAlert();

  const [refreshFlag, setRefreshFlag] = React.useState<boolean>(false);

  React.useEffect(() => {
    getKeyLength(handleAlert).then(setKeyLength).catch()
  }, [refreshFlag, handleAlert])

  const refresh = React.useCallback(() => {
    setRefreshFlag(prev => !prev);
  }, []);


  return (
    <>
      <SnackbarAlert msg={alertMsg} />
      <Accordion defaultExpanded className='man-page-section'>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
        >
          <Typography>序列号生成设置</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Stack direction='column' sx={{ justifyContent: 'space-around' }}>
            <Button startIcon={<ChecklistIcon />} onClick={() => setMandatoryFieldsDialogueOpen(true)}>
              必填项
            </Button>
            <MandatoryFieldsDialogue open={mandatoryFieldsDialogueOpen} handleClose={() => setMandatoryFieldsDialogueOpen(false)} />
          </Stack>
          <Divider />
          <Stack direction='column' sx={{ justifyContent: 'space-around' }}>
            <Button startIcon={<BlockIcon />}>
              屏蔽单词
            </Button>
          </Stack>
          <Divider />
          <Stack direction='row' sx={{ justifyContent: 'center', marginTop: '8px' }}>
            <Button startIcon={<SettingsIcon />} onClick={() => setKeyLengthDialogOpen(true)}>
              序列号长度（不影响已创建的序列号）
            </Button>
            <KeyLengthDialog currentLength={keyLength} open={KeyLengthDialogOpen} onClose={(shouldRefresh?: boolean) => {
              setKeyLengthDialogOpen(false);
              if (shouldRefresh) {
                refresh();
              }
            }} />
            {
              keyLength !== null
                ? <TextField
                  disabled
                  InputProps={
                    {
                      readOnly: true,
                    }
                  }
                  label="当前值"
                  value={keyLength}
                  size="small"
                  variant='standard'
                />
                : <CircularProgress />
            }

          </Stack>


        </AccordionDetails>
      </Accordion>
    </>
  );
}