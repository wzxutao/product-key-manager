import React from 'react';

import "./EasyCopyChip.less"
import { Tooltip } from '@mui/material';

export function EasyCopyChip(props: {
    text: string
}) {
    const { text } = props;

    const [copied, setCopied] = React.useState<boolean>(false);
    const [tooltipShowCopied, setTooltipShowCopied] = React.useState<boolean>(false);

    const handleClick = React.useCallback((ev: React.MouseEvent<HTMLInputElement>) => {
        (ev.target as HTMLInputElement).select();
        navigator.clipboard.writeText(text);
        setCopied(true);
        setTooltipShowCopied(true);
    }, [text]);

    return (<>
        <Tooltip title={tooltipShowCopied ? '已复制' : '单击复制'}>
            <input className={['easy-copy-chip',
                copied ? 'easy-copy-chip-copied' : ''].join(' ')}
                onClick={handleClick} value={text} readOnly
                onBlur={() => setTooltipShowCopied(false)}></input>
        </Tooltip>
    </>)
}