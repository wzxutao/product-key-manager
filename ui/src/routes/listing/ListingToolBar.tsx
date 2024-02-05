import React from 'react';

import { Button, Paper, Stack } from '@mui/material';

export default function ListingToolBar(
    props: {
        onQuery: () => void
    }
) {
    return (
        <Paper elevation={0} className='list-page-toolbar'>
            <Stack direction='row' justifyContent='space-evenly'>
                <Button variant='contained' onClick={props.onQuery} sx={{flexGrow: 1}}>按条件加载数据</Button>
            </Stack>
        </Paper>
    )
}