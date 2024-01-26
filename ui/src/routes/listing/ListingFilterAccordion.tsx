
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Paper } from '@mui/material';

export default function FilterAccordion() {

    return (
        <Paper className='list-page-filter' elevation={3}>
        <Accordion defaultExpanded>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
            >
                <Typography>筛选条件</Typography>
            </AccordionSummary>
            <AccordionDetails>


            </AccordionDetails>
        </Accordion>
        </Paper>
    );
}