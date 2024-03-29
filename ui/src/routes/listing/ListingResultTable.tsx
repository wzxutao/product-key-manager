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
import { Button, CircularProgress } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import EditKeyDialog from '../../components/app-specific/EditKeyDialogue';
import { updateRecord } from '../../http/today-listing-api';

import FixedTablePagination from '../../components/FixedTablePagination';


interface Column {
    id: string;
    label: string;
    format: (row: RecordDto) => any;
    minWidth?: number;
    maxWidth?: number;
    align?: 'right';
}

export default function ListingResultTable(props: {
    data: RecordDto[] | null
    onChange: () => void,
}) {
    const { data, onChange } = props;
    const [page, setPage] = React.useState<number>(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [selectedRecord, setSelectedRecord] = React.useState<RecordDto | null>(null);


    React.useEffect(() => {
        setPage(0);
    }, [data])

    const handleChangePage = React.useCallback((newPage: number): boolean => {

        const maxPage = Math.ceil((data?.length ?? 0) / rowsPerPage) - 1;
        const newPageClipped: number = Math.max(
            Math.min(
                newPage, maxPage),
            0);

        if(isNaN(newPageClipped)) {
            setPage(0);
            return false;
        }

        if (newPageClipped === page) return false;
        setPage(newPageClipped);
        return true;
    }, [page, data, rowsPerPage]);


    const handleChangeRowsPerPage = React.useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    }, []);

    const columns: Column[] = React.useMemo(() => {
        return [
            {
                "id": "productKey",
                "label": "序列号",
                "format": dto => dto.productKey
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
            },
            {
                "id": "comment",
                "label": "备注",
                "format": dto => <p style={{ overflowWrap: 'anywhere' }}>{dto.comment}</p>
            },
            {
                "id": "status",
                "label": "状态",
                "format": dto => dto.status
            },
            {
                "id": "username",
                "label": "创建用户",
                "format": dto => dto.username
            },
            {
                "id": "createdAt",
                "label": "创建时间",
                "format": dto => new Date(dto.createdMilli).toLocaleString()
            },
            {
                "id": 'edit',
                "label": '修改',
                "format": dto => {
                    return <Button startIcon={<EditIcon />}
                        onClick={() => setSelectedRecord(dto)}></Button>
                },
                "maxWidth": 12,
                "minWidth": 0,
            }
        ]
    }, [])

    const rows = React.useMemo(() => {
        return (
            data
                ?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row) => {
                    return (
                        <TableRow
                            hover role="checkbox" tabIndex={-1} key={row.id}>
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
    }, [data, page, rowsPerPage, columns])

    const handleUpdateRecord = React.useCallback((newRecord: RecordDto) => {
        return updateRecord(newRecord).then(() => {
            onChange();
        })
    }, [onChange])

    return (
        <>
            <EditKeyDialog record={selectedRecord}
                onClose={() => { setSelectedRecord(null); }}
                onSubmit={handleUpdateRecord}
            />
            <Paper className="list-page-table" sx={{
                paddingBottom: '50px'
            }}>
                <TableContainer>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                {columns.map((column) => (
                                    <TableCell
                                        className='table-head-cell'
                                        key={column.id}
                                        align={column.align}
                                        style={{
                                            minWidth: column.minWidth ?? 170,
                                            maxWidth: column.maxWidth ?? 'none'
                                        }}
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
                    rowsPerPageOptions={[10, 25, 50, 100]}
                    component="div"
                    count={data?.length ?? 0}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={(_, page) => handleChangePage(page)}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    ActionsComponent={undefined}
                />
                <FixedTablePagination page={page} onChangePage={handleChangePage} />
            </Paper>
        </>
    );
}