import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';

import { RecordDto } from '../../http/dto/record-dto';
import { Checkbox, CircularProgress } from '@mui/material';


interface Column {
    id: string;
    label: string;
    format: (row: RecordDto) => any;
    minWidth?: number;
    align?: 'right';
}

export interface TodayListingResultTableProps {
    data: RecordDto[] | null
}

export interface TodayListingResultTableHandle {
    getSelectedKeys: () => string[]
}

const TodayListingResultTable = React.forwardRef<TodayListingResultTableHandle, TodayListingResultTableProps>((props, ref) => {
    const { data } = props;
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [rowsSelected, setRowsSelected] = React.useState<number[]>([])

    const handleChangePage = React.useCallback((_event: unknown, newPage: number) => {
        setPage(newPage);
        setRowsSelected([]);
    }, []);

    const handleChangeRowsPerPage = React.useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
        setRowsSelected([]);
    }, []);

    React.useEffect(() => {
        setRowsSelected([]);
    }, [data])

    React.useImperativeHandle(ref, () => {
        return {
            getSelectedKeys: () => {
                return data?.filter((_, i) => rowsSelected.indexOf(i) !== -1).map(r => r.productKey) ?? []
            }
        }
    }, [data, rowsSelected]);

    const columns: Column[] = React.useMemo(() => {
        return [
            {
                "id": "productKey",
                "label": "序列号",
                "format": dto => dto.productKey
            },
            {
                "id": "status",
                "label": "状态",
                "format": dto => dto.status
            },
            {
                "id": "username",
                "label": "创建者",
                "format": dto => dto.username
            },
            {
                "id": "createdAt",
                "label": "创建时间",
                "format": dto => new Date(dto.createdMilli).toLocaleString()
            },
            {
                "id": "contents",
                "label": "内容",
                "format": dto => {
                    var fields = dto.expandedAllFields;
                    const rv = [];
                    for (const k in fields) {
                        rv.push(
                            <p key={k} className='content-row'>
                                <span className='content-key'>{k}: </span>{fields[k]}
                            </p>
                        )
                    }
                    return rv;
                }
            }
        ]
    }, [])

    const rows = React.useMemo(() => {
        return (
            data
                ?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, idx) => {
                    return (
                        <TableRow
                            hover role="checkbox" tabIndex={-1} key={row.id}>
                            <TableCell padding="checkbox">
                                <Checkbox
                                    color="primary"
                                    checked={rowsSelected.indexOf(idx) !== -1}
                                    onChange={(e) => {
                                        if (e.target.checked) {
                                            setRowsSelected([...rowsSelected, idx])
                                        } else {
                                            setRowsSelected(rowsSelected.filter(i => idx !== i))
                                        }
                                    }}
                                />
                            </TableCell>

                            {columns.map((column) => {
                                return (
                                    <TableCell className='table-row-cell'
                                        key={column.id} align={column.align}>
                                        {column.format(row)}
                                    </TableCell>
                                );
                            })}
                        </TableRow>
                    );
                }))
    }, [data, page, rowsPerPage, columns, rowsSelected])

    return (
        <Paper className="list-page-table">
            <TableContainer>
                <Table stickyHeader aria-label="sticky table">
                    <TableHead>
                        <TableRow>
                            <TableCell className='table-head-cell'></TableCell>
                            {columns.map((column) => (
                                <TableCell
                                    className='table-head-cell'
                                    key={column.id}
                                    align={column.align}
                                    style={{ minWidth: column.minWidth ?? 170 }}
                                >
                                    {column.label}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data === null &&
                            <TableRow>
                                <TableCell colSpan={columns.length} sx={{
                                    textAlign: 'center',
                                }}>
                                    <CircularProgress />
                                </TableCell>
                            </TableRow>
                        }
                        {data !== null && rows}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[10, 25, 100]}
                component="div"
                count={data?.length ?? 0}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
});

export default TodayListingResultTable;