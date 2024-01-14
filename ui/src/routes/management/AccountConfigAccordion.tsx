import * as React from 'react';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Button, Divider, Stack, TextField } from '@mui/material';
import SettingsIcon from '@mui/icons-material/Settings';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';

export default function AccountConfigAccordion() {
  return (
    <Accordion defaultExpanded className='man-page-section'>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
      >
        <Typography>账号管理</Typography>
      </AccordionSummary>
      <AccordionDetails>
        <Stack direction='row' sx={{ justifyContent: 'space-around' }}>
          <Button startIcon={<ManageAccountsIcon />}>
            普通账号
          </Button>
        </Stack>
        <Divider />
        <Stack direction='row' sx={{ justifyContent: 'center', marginTop: '8px' }}>
          <Button startIcon={<SettingsIcon />}>
            管理员登陆有效期（分钟）
          </Button>
          <TextField
            disabled
            label="当前值"
            defaultValue="-1"
            size="small"
            variant='standard'
          />
        </Stack>
        

      </AccordionDetails>
    </Accordion>
  );
}