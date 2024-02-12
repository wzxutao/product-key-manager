import React from 'react';

import { Button, Paper, Stack, TextField } from '@mui/material';

export default function ListingToolBar(
    props: {
        onQuery: (prodctKey: string) => void
    }
) {
    const { onQuery } = props;

    const [productKey, setProductKey] = React.useState<string>('')

    const handleSearch = React.useCallback(() => {
        onQuery(productKey);
    }, [onQuery, productKey])

    return (
        <Paper elevation={3} className='list-page-toolbar'>
            <Stack direction='row' justifyContent='space-evenly' spacing={1}>
                <TextField label="序列号"
                    size='small' sx={{ flexGrow: 1 }}
                    value={productKey}
                    onChange={(ev) => setProductKey(ev.target.value)}
                    onKeyDown={ev => {
                        if (ev.key === 'Enter') {
                            handleSearch();
                        }
                    }}
                    autoFocus />
                <Button variant='contained' onClick={handleSearch} sx={{ flexGrow: 1 }}>按序列号查找(或输入完回车)</Button>
            </Stack>
        </Paper>
    )
}