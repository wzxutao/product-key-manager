import React from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import StarIcon from '@mui/icons-material/Star';
import { QueryRecordCriterion } from '../../http/listing-api';


export default function RulesGraph(props: {
    criteria: QueryRecordCriterion | null,
    selectedCriteria: QueryRecordCriterion | null,
    setSelectedCriteria: (criteria: QueryRecordCriterion | null) => void,
}) {
    const { criteria, selectedCriteria, setSelectedCriteria } = props;

    const graph = React.useMemo(() => {
        if (criteria === null) {
            return <p>所有数据</p>
        }

        return <List
            className="rule-graph-list"
        >
            <ListItem disablePadding>
                <ListItemButton>
                    <ListItemIcon>
                        <StarIcon />
                    </ListItemIcon>
                    <ListItemText primary="Chelsea Otakan" />
                </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
                <ListItemButton>
                    <ListItemText inset primary="Eric Hoffman" />
                </ListItemButton>
            </ListItem>
        </List>
    }, [criteria, selectedCriteria])

    return (
        <div className='rule-graph'>
            {graph}
        </div>
    );
}