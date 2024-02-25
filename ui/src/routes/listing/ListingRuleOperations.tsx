import { Button, Stack } from '@mui/material';
import React from 'react';


export default function ListingRuleOperations(props: {
    onClear: () => void,
    onDeleteSelected: () => void,
}) {
    return (
        <Stack direction='row' spacing={1} justifyContent='space-evenly'>
            <Button variant='outlined' color="error" sx={{ flexGrow: 2 }}
                onClick={props.onDeleteSelected}>
                删除所选
            </Button>
            <Button variant='outlined' color="error"
                onClick={props.onClear}
                sx={{ flexGrow: 1 }}
            >
                重置
            </Button>
        </Stack>
    )
}