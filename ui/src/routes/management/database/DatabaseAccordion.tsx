import * as React from 'react';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Button, Divider, Stack } from '@mui/material';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import RestoreIcon from '@mui/icons-material/Restore';
import AutoModeIcon from '@mui/icons-material/AutoMode';
import BackupDialog from './BackupDialogue';
import RestoreDialog from './RestoreDialogue';

export default function DatabaseAccordion() {
  const [backupDialogOpen, setBackupDialogOpen] = React.useState(false);
  const [restoreDialogOpen, setRestoreDialogOpen] = React.useState(false);

  return (
    <Accordion defaultExpanded className='man-page-section'>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
      >
        <Typography>数据库管理</Typography>
      </AccordionSummary>
      <AccordionDetails>
      <Divider variant="middle">本地备份/恢复</Divider>
        <Stack direction='row' sx={{ justifyContent: 'space-around' }}>
          <Button startIcon={<ContentCopyIcon />} onClick={() => setBackupDialogOpen(true)}>立即本地备份</Button>
          <BackupDialog open={backupDialogOpen} handleClose={() => setBackupDialogOpen(false)}/>
          <Button startIcon={<RestoreIcon />} onClick={() => setRestoreDialogOpen(true)}>查看/恢复本地备份</Button>
          <RestoreDialog open={restoreDialogOpen} handleClose={() => setRestoreDialogOpen(false)} />
        </Stack>
        <Divider variant="middle">自动本地备份</Divider>
        <Stack direction='row' sx={{ justifyContent: 'space-around' }}>
          <Button startIcon={<AutoModeIcon />}>自动备份设置</Button>
        </Stack>

      </AccordionDetails>
    </Accordion>
  );
}