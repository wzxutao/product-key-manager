import React from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import StarIcon from '@mui/icons-material/Star';
import { QueryRecordCriterion, isRootCriterion } from '../../http/listing-api';

let key = 0;

function buildListItemFromCriterion(
    criterion: QueryRecordCriterion,
    selectedCriterion: QueryRecordCriterion,
    onClick: (cr: QueryRecordCriterion) => void
) {
    return <ListItem key={key++} disablePadding
        onClick={(ev) => {
            onClick(criterion)
            ev.stopPropagation();
        }}
    >
        <ListItemButton>
            {criterion === selectedCriterion && <ListItemIcon><StarIcon /></ListItemIcon>}
            <ListItemText inset primary={isRootCriterion(criterion) ? '所有数据' : criterion.helperText} />
        </ListItemButton>
        {criterion.children && criterion.children.length > 0 &&
            <List>
                {criterion.children.map(c => buildListItemFromCriterion(c, selectedCriterion, onClick))}
            </List>
        }
    </ListItem>
}


export default function RulesGraph(props: {
    criteria: QueryRecordCriterion,
    selectedCriteria: QueryRecordCriterion,
    setSelectedCriteria: (criteria: QueryRecordCriterion) => void,
}) {
    const { criteria, selectedCriteria, setSelectedCriteria } = props;

    const graph = React.useMemo(() => {
        return <List
            className="rule-graph-list"
        >
            {buildListItemFromCriterion(criteria, selectedCriteria, setSelectedCriteria)}
        </List>
    }, [criteria, selectedCriteria])

    return (
        <div className='rule-graph'>
            {graph}
        </div>
    );
}