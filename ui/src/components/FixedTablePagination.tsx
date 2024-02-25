import { Button, Stack, TextField } from '@mui/material';
import React from 'react';

import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import './FixedTablePagination.less';


const FixedTablePagination = (props: {
    page: number,
    onChangePage: (page: number) => boolean,
}) => {
    const { page: pPage, onChangePage } = props;

    const [pageInput, setPageInput] = React.useState<string>('1')

    React.useEffect(() => {
        setPageInput('' + (pPage + 1));
    }, [pPage])
    return (
        <>
            <Stack className='fixed-table-pagination-container' direction='row'>
                <Button startIcon={<KeyboardArrowLeftIcon />}
                onClick={() => onChangePage(pPage - 1)}
                />
                <TextField
                    value={pageInput}
                    label="页码"
                    type="number"
                    variant='filled'
                    InputProps={{
                        inputProps: {
                            min: 1,
                            step: 1
                        }
                    }}
                    onChange={(e) => {
                        setPageInput(e.target.value);
                    }}
                    onBlur={_ => {
                        if (!onChangePage(+pageInput - 1)) {
                            setPageInput('' + (pPage + 1));
                        }
                    }}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                            if (!onChangePage(+pageInput - 1)) {
                                setPageInput('' + (pPage + 1));
                            }
                        }
                    }}
                    sx={{
                        flexGrow: 1,
                        flexBasis: '20px',
                    }}
                />
                <Button startIcon={<KeyboardArrowRightIcon />}
                    onClick={() => onChangePage(pPage + 1)}
                />
            </Stack>
        </>
    )
}

export default FixedTablePagination;