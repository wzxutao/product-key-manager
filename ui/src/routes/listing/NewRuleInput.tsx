import React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import AddIcon from '@mui/icons-material/Add';
import { Button, Stack, Switch, TextField, Tooltip, Typography } from '@mui/material';
import { OperatorDefinition } from './ListingFilterAccordion';
import { QueryRecordCriterion } from '../../http/listing-api';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';

export default function NewRuleInput(props: {
    operators: OperatorDefinition,
    isFirstCriterion: boolean
    onAddCriterion: (criterion: QueryRecordCriterion) => void
}) {
    const { operators, isFirstCriterion, onAddCriterion } = props;

    const [filterTarget, setFilterTarget] = React.useState<string | null>(null);
    const [filterOperator, setFilterOperator] = React.useState<OperatorDefinition[0][0] | null>(null)
    const [value1, setValue1] = React.useState<any>(null);
    const [value2, setValue2] = React.useState<any>(null);
    const [alertMsg, handleAlert] = useAlert();

    const handleChangeFilterTarget = React.useCallback((event: SelectChangeEvent) => {
        setFilterTarget(event.target.value);
        setFilterOperator(null);
        setValue1(null);
        setValue2(null);
    }, []);

    const filterTargetMenuItems = React.useMemo(() => Object.keys(operators).map(k =>
        <MenuItem key={k} value={k}>{k}</MenuItem>
    ), [operators]);


    const filterOperatorMenuItems = React.useMemo(() =>
        filterTarget === null ? undefined : operators[filterTarget]
            .map(o =>
                <MenuItem key={o.code} value={o.code}>{o.operator}</MenuItem>),
        [operators, filterTarget]);

    const handleChangeOperator = React.useCallback((event: SelectChangeEvent) => {
        setFilterOperator(operators[filterTarget!].filter(op => op.code === event.target.value)[0]);
        setValue1(null);
        setValue2(null);
    }, [operators, filterTarget]);

    const operand1 = React.useMemo(() => {
        if (!filterOperator) return null;

        const { label, selectOptions, datetimeFormatter } = filterOperator.value1Config;

        if (selectOptions) {
            return <Select
                labelId="new-rule-input-field-label"
                value={value1 ?? ''}
                required
                onChange={ev => setValue1(ev.target.value)}
            >
                {Object.keys(selectOptions).map(k =>
                    <MenuItem key={selectOptions[k]} value={selectOptions[k]}>{k}</MenuItem>
                )}
            </Select>
        } else if (datetimeFormatter) {
            if (!value1) {
                setValue1(new Date().toISOString().substring(0, 16))
            }
            return (
                <TextField
                    id="new-rule-operand1"
                    label={label}
                    type="datetime-local"
                    autoComplete={label}
                    value={value1}
                    required
                    onChange={ev => setValue1(ev.target.value)}
                />
            )

        } else {
            return <TextField
                label={label}
                type="text"
                autoComplete={label}
                required
                value={value1 ?? ''}
                onChange={ev => setValue1(ev.target.value)}
            />
        }

    }, [filterOperator, value1])

    const operand2 = React.useMemo(() => {
        if (!filterOperator || !filterOperator.value2Config) return null;

        const { label, selectOptions, datetimeFormatter } = filterOperator.value2Config;

        if (selectOptions) {
            return <Select
                labelId="new-rule-input-field-label"
                value={value2 ?? ''}
                required
                onChange={ev => setValue2(ev.target.value)}
            >
                {Object.keys(selectOptions).map(k =>
                    <MenuItem key={selectOptions[k]} value={selectOptions[k]}>{k}</MenuItem>
                )}
            </Select>
        } else if (datetimeFormatter) {
            if (!value2) {
                setValue2(new Date().toISOString().substring(0, 16))
            }
            return (
                <TextField
                    id="new-rule-operand1"
                    label={label}
                    type="datetime-local"
                    autoComplete={label}
                    placeholder={new Date().toISOString().substring(0, 16)}
                    value={value2}
                    required
                    onChange={ev => setValue2(ev.target.value)}
                />
            )
        } else {
            return <TextField
                label={label}
                type="text"
                autoComplete={label}
                required
                value={value2 ?? ''}
                onChange={ev => setValue2(ev.target.value)}
            />
        }

    }, [filterOperator, value2])

    const handleAddCriterion = React.useCallback(() => {
        if(!filterOperator) {
            handleAlert("请选择运算符")
            return;
        }

        let value1Converted = value1;
        let value1Formatted = value1;

        if(filterOperator.value1Config.datetimeFormatter) {
            value1Converted = filterOperator.value1Config.datetimeFormatter(value1)
            value1Formatted = new Date(value1Converted).toLocaleString()
        }
        else if(filterOperator.value1Config.selectOptions) {
            value1Formatted = Object.keys(filterOperator.value1Config.selectOptions).filter(k => filterOperator.value1Config?.selectOptions?.[k] === value1)[0]
        }

        let value2Converted = filterOperator.value2Config ? value2 : null;
        let value2Formatted = value2 ?? '';

        if(filterOperator.value2Config?.datetimeFormatter) {
            value2Converted = filterOperator.value2Config.datetimeFormatter(value2)
            value2Formatted = new Date(value2Converted).toLocaleString()
        }
        else if(filterOperator.value2Config?.selectOptions) {
            value2Formatted = Object.keys(filterOperator.value2Config.selectOptions).filter(k => filterOperator.value2Config?.selectOptions?.[k] === value2)[0]
        }
        if(filterOperator.value2Config) {
            value2Formatted = '| ' + value2Formatted 
        }

        const cr: QueryRecordCriterion = {
            children: [],
            operand1: value1Converted,
            operand2: value2Converted,
            operator: filterOperator.code,
            helperText: `${filterTarget} ${filterOperator.operator} ${value1Formatted} ${value2Formatted}`
        }
        onAddCriterion(cr)
    }, [onAddCriterion, value1, value2, filterOperator])


    return (
        <>
        <SnackbarAlert msg={alertMsg} />
        <Stack className='rule-input' direction='row'>
            <FormControl variant="standard" sx={{ m: 1, minWidth: 220 }} required>
                <InputLabel id="new-rule-input-field-label">筛选对象</InputLabel>
                <Select
                    labelId="new-rule-input-field-label"
                    value={filterTarget ?? ''}
                    onChange={handleChangeFilterTarget}
                >

                    {filterTargetMenuItems}
                </Select>
            </FormControl>
            <FormControl variant="filled" sx={{ m: 1, minWidth: 220 }} required>
                <InputLabel id="new-rule-input-operator-label">运算符</InputLabel>
                <Select
                    labelId="new-rule-input-operator-label"
                    value={filterOperator?.code ?? ''}
                    onChange={handleChangeOperator}
                >
                    {filterOperatorMenuItems}
                </Select>
            </FormControl>
            <FormControl variant="standard" sx={{ m: 1, minWidth: 240 }}>
                {operand1}
            </FormControl>
            <FormControl variant="standard" sx={{ m: 1, minWidth: 240 }}>
                {operand2}
            </FormControl>

            <Tooltip title="和带星号的条件取交集">
                <Button className="rule-input-add-button"
                    variant="contained"
                    endIcon={<AddIcon />}
                    onClick={handleAddCriterion}
                    >
                    添加
                </Button>
            </Tooltip>
        </Stack>
        </>
    )
}