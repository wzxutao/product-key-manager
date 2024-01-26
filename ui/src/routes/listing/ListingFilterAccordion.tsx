import React from 'react';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Divider, Paper } from '@mui/material';
import NewRuleInput from './NewRuleInput';
import RulesGraph from './RulesGraph';
import { QueryRecordCriterion } from '../../http/listing-api';

export default function FilterAccordion() {

    const [criteria, setCriteria] = React.useState<QueryRecordCriterion | null>(null);
    const [selectedCriteria, setSelectedCriteria] = React.useState<QueryRecordCriterion | null>(null);

    return (
        <Paper className='list-page-filter' elevation={3}>
            <Accordion defaultExpanded>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                >
                    <Typography><b>筛选条件</b></Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <NewRuleInput />
                    <Divider />
                    <Typography>
                        <b>当前条件：</b>
                    </Typography>
                    <RulesGraph
                        criteria={criteria}
                        selectedCriteria={selectedCriteria}
                        setSelectedCriteria={setSelectedCriteria} />
                </AccordionDetails>
            </Accordion>
        </Paper>
    );
}