import * as React from 'react';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Button, Divider, Stack, TextField } from '@mui/material';
import ChecklistIcon from '@mui/icons-material/Checklist';
import SettingsIcon from '@mui/icons-material/Settings';
import BlockIcon from '@mui/icons-material/Block';

export default function KeyConfigAccordion() {
  return (
    <Accordion defaultExpanded className='man-page-section'>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
      >
        <Typography>序列号生成设置</Typography>
      </AccordionSummary>
      <AccordionDetails>
        <Stack direction='column' sx={{ justifyContent: 'space-around' }}>
          <Button startIcon={<ChecklistIcon />}>
            必填项
          </Button>
        </Stack>
        <Divider />
        <Stack direction='column' sx={{ justifyContent: 'space-around' }}>
          <Button startIcon={<BlockIcon />}>
            屏蔽单词
          </Button>
        </Stack>
        <Divider />
        <Stack direction='row' sx={{ justifyContent: 'center', marginTop: '8px' }}>
          <Button startIcon={<SettingsIcon />}>
            序列号长度（不影响已创建的序列号）
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