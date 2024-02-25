import React from 'react';

import { Button, Paper, Stack, TextField } from '@mui/material';

export default function ListingToolBar(
    props: {
        onQueryByProductKey: (prodctKey: string) => void,
        onQuickSearch: (input: string) => void,
    }
) {
    const { onQueryByProductKey, onQuickSearch } = props;

    const [productKey, setProductKey] = React.useState<string>('')
    const [quickSearchInput, setQuickSearchInput] = React.useState<string>('')

    const handleSearchByProductKey = React.useCallback(() => {
        onQueryByProductKey(productKey);
    }, [onQueryByProductKey, productKey])

    const handleQuickSearch = React.useCallback(() => {
        onQuickSearch(quickSearchInput);
    }, [onQuickSearch, quickSearchInput])

    return (
        <>
            <Paper elevation={3} className='list-page-toolbar'>
                <Stack direction='row' justifyContent='space-evenly' spacing={1}>
                    <TextField label="序列号｜内容｜用户名｜备注"
                        size='small' sx={{ flexGrow: 1 }}
                        value={quickSearchInput}
                        onChange={(ev) => setQuickSearchInput(ev.target.value)}
                        onKeyDown={ev => {
                            if (ev.key === 'Enter') {
                                handleQuickSearch();
                            }
                        }}
                        autoFocus />
                    <Button variant='contained' onClick={handleQuickSearch}
                        sx={{ flexGrow: 1, width: '10%' }}>模糊查找(Enter)</Button>
                </Stack>
            </Paper>
            <Paper elevation={3} className='list-page-toolbar'>
                <Stack direction='row' justifyContent='space-evenly' spacing={1}>
                    <TextField label="序列号"
                        size='small' sx={{ flexGrow: 1 }}
                        value={productKey}
                        onChange={(ev) => setProductKey(ev.target.value)}
                        onKeyDown={ev => {
                            if (ev.key === 'Enter') {
                                handleSearchByProductKey();
                            }
                        }}
                        />
                    <Button variant='contained' onClick={handleSearchByProductKey}
                        sx={{ flexGrow: 1, width: '10%' }}>序列号精确查找(Enter)</Button>
                </Stack>
            </Paper>
        </>
    )
}