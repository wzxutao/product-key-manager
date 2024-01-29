import { Button, Stack } from '@mui/material';
import React from 'react';


export default function ListingRuleOperations(props: {
    onClear: () => void,
    onDeleteSelected: () => void,
}) {
    return (
        <Stack direction='row' spacing={1} justifyContent='space-evenly'>
            <Button variant='outlined' color="error"
                onClick={props.onDeleteSelected}>
                删除所选
            </Button>
            <Button variant='contained' color="error"
                onClick={props.onClear}>
                重置
            </Button>
        </Stack>
    )
}