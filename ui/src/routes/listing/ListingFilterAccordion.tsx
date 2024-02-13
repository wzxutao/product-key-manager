import React from 'react';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Button, Divider, Paper, Stack } from '@mui/material';
import NewRuleInput from './NewRuleInput';
import RulesGraph from './RulesGraph';
import { QueryRecordCriterion, rootCriterion } from '../../http/listing-api';
import ListingRuleOperations from './ListingRuleOperations';

export type OperandConfig = {
    label: string,
    selectOptions?: Record<string, any>,
    datetimeFormatter?: (date: string) => any,
}

export type OperatorDefinition = Record<string, {
    operator: string,
    code: string,
    value1Config: OperandConfig,
    value2Config?: OperandConfig
}[]>

const operators: OperatorDefinition = {
    "创建时间": [
        {
            operator: "晚于（包含头）",
            code: "CREATED_MILLIS_AFTER",
            value1Config: {
                label: "日期时间",
                datetimeFormatter: Date.parse
            }
        },
        {
            operator: "早于（包含尾）",
            code: "CREATED_MILLIS_BEFORE",
            value1Config: {
                label: "日期时间",
                datetimeFormatter: Date.parse
            }
        },
        {
            operator: "从...到...（包含头尾）",
            code: "CREATED_MILLIS_BETWEEN",
            value1Config: {
                label: "从",
                datetimeFormatter: Date.parse
            },
            value2Config: {
                label: "到",
                datetimeFormatter: Date.parse
            }
        }
    ],
    "用户名": [
        {
            operator: "等于",
            code: "USERNAME_EQUALS",
            value1Config: {
                label: "用户名"
            },
        },
        {
            operator: "不等于",
            code: "USERNAME_NOT_EQUALS",
            value1Config: {
                label: "用户名"
            },
        },
        {
            operator: "包含",
            code: "USERNAME_CONTAINS",
            value1Config: {
                label: "字符串"
            },
        }
    ],
    "状态": [
        {
            operator: "等于",
            code: "STATUS_EQUALS",
            value1Config: {
                label: "什么状态",
                selectOptions: {
                    "正常": 0,
                    "删除": 1,
                }
            },
        },
        {
            operator: "不等于",
            code: "STATUS_NOT_EQUALS",
            value1Config: {
                label: "什么状态",
                selectOptions: {
                    "正常": 0,
                    "删除": 1,
                }
            },
        }
    ],
    "内容|备注": [
        {
            operator: "包含",
            code: "PAYLOAD_CONTAINS",
            value1Config: {
                label: "字符串"
            },
        },
        {
            operator: "不包含",
            code: "PAYLOAD_NOT_CONTAINS",
            value1Config: {
                label: "字符串"
            },
        }
    ],
    "内容字段": [
        {
            operator: "等于",
            code: "FIELD_EQUALS",
            value1Config: {
                label: "字段名"
            },
            value2Config: {
                label: "字段值"
            },
        },
        {
            operator: "包含",
            code: "FIELD_CONTAINS",
            value1Config: {
                label: "字段名"
            },
            value2Config: {
                label: "字符串"
            },
        },
        {
            operator: "不包含",
            code: "FIELD_NOT_CONTAINS",
            value1Config: {
                label: "字段名"
            },
            value2Config: {
                label: "字符串"
            },
        }
    ]
}


export default function FilterAccordion(
    props: {
        rootCr: QueryRecordCriterion,
        setRootCr: (cr: QueryRecordCriterion) => void,
        onQuery: () => void,
    }
) {
    const { rootCr, setRootCr, onQuery } = props;

    const [selectedCriteria, setSelectedCriteria] = React.useState<QueryRecordCriterion>(rootCr);

    const handleAddCriterion = React.useCallback((criterion: QueryRecordCriterion) => {
        criterion.parent = selectedCriteria;
        selectedCriteria.children.push(criterion)
        setSelectedCriteria(criterion);
    }, [selectedCriteria]);

    const handleClear = React.useCallback(() => {
        const cr = rootCriterion()
        setRootCr(cr)
        setSelectedCriteria(cr)
    }, [setRootCr]);

    const handleDeleteSelected = React.useCallback(() => {
        if (selectedCriteria === null) return;
        const parent = selectedCriteria.parent;
        if (!parent) return;

        parent.children = parent.children.filter(c => c !== selectedCriteria);
        setSelectedCriteria(parent);
    }, [selectedCriteria]);


    return (
        <Paper className='list-page-filter' elevation={3}>
            <Accordion defaultExpanded={false}>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                >
                    <Typography><b>高级查找</b></Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Divider><b>添加新条件</b></Divider>
                    <NewRuleInput
                        operators={operators}
                        isFirstCriterion={selectedCriteria === null}
                        onAddCriterion={handleAddCriterion} />
                    <Divider><b>条件操作：</b></Divider>
                    <ListingRuleOperations
                        onClear={handleClear}
                        onDeleteSelected={handleDeleteSelected}
                    />
                    <Divider><b>当前条件：</b></Divider>
                    <RulesGraph
                        criteria={rootCr}
                        selectedCriteria={selectedCriteria}
                        setSelectedCriteria={setSelectedCriteria} />

                    <Stack direction='row' justifyContent='space-evenly'>
                        <Button variant='contained' onClick={onQuery} sx={{ flexGrow: 1 }}>按条件加载数据</Button>
                    </Stack>
                </AccordionDetails>
            </Accordion>
        </Paper>
    );
}