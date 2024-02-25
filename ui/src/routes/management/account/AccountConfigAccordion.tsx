import * as React from 'react';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Button, CircularProgress, Divider, Stack, TextField } from '@mui/material';
import SettingsIcon from '@mui/icons-material/Settings';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import SnackbarAlert, { useAlert } from '../../../components/SnackbarAlert';
import { getAdminExpiry } from '../../../http/admin-api';
import AdminExpiryDialog from './AdminExpiryDialogue';
import NormalAccountDialogue from './NormalAccountDialogue';

export default function AccountConfigAccordion() {

  const [alertMsg, handleAlert] = useAlert();
  const [adminExpiryDialogueOpen, setAdminExpiryDialogueOpen] = React.useState(false);
  const [adminExpiry, setAdminExpiry] = React.useState<number | null>(null);
  const [refreshFlag, setRefreshFlag] = React.useState<boolean>(false);

  const [normalAccountDialogueOpen, setNormalAccountDialogueOpen] = React.useState(false);

  React.useEffect(() => {
    getAdminExpiry(handleAlert).then(setAdminExpiry).catch()
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
          <Typography>账号管理</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Stack direction='row' sx={{ justifyContent: 'space-around' }}>
            <Button startIcon={<ManageAccountsIcon />} onClick={() => setNormalAccountDialogueOpen(true)}>
              普通账号
            </Button>
            <NormalAccountDialogue open={normalAccountDialogueOpen} 
            onClose={(shouldRefresh?: boolean) => { setNormalAccountDialogueOpen(false); if (shouldRefresh) { refresh(); } }} />
          </Stack>
          <Divider />
          <Stack direction='row' sx={{ justifyContent: 'center', marginTop: '8px' }}>
            <Button startIcon={<SettingsIcon />} onClick={() => setAdminExpiryDialogueOpen(true)}>
              管理员登陆有效期（分钟）
            </Button>
            <AdminExpiryDialog open={adminExpiryDialogueOpen} onClose={(shouldRefresh?: boolean) => {
              setAdminExpiryDialogueOpen(false)
              if (shouldRefresh) {
                refresh();
              }
            }} currentLength={adminExpiry} />
            {
              adminExpiry !== null ?
                <TextField
                  disabled
                  inputProps={{
                    readOnly: true,
                  }}
                  label="当前值"
                  value={adminExpiry ?? -1}
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