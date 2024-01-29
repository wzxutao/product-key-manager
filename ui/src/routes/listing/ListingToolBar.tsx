import React from 'react';

import { Button, Paper, Stack } from '@mui/material';

export default function ListingToolBar(
    props: {
        onQuery: () => void
    }
) {
    return (
        <Paper elevation={2} className='list-page-toolbar'>
            <Stack direction='row' spacing={1} justifyContent='space-evenly'>
                <Button variant='contained' onClick={props.onQuery}>加载数据</Button>
            </Stack>
        </Paper>
    )
}