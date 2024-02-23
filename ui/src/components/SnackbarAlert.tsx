import React from 'react';

import { Alert, AlertColor, Snackbar, Stack } from '@mui/material';

export type SnackbarAlertMessage = {
    text: string,
    severity?: AlertColor
}

export function useAlert(): [SnackbarAlertMessage | undefined, (msg: string, severity?: SnackbarAlertMessage['severity']) => void] {
    const [alertMsg, setAlertMsg] = React.useState<SnackbarAlertMessage | undefined>(undefined);
    const handleAlert = React.useCallback((msg: string, severity?: SnackbarAlertMessage['severity']) => {
        setAlertMsg({ text: msg, severity });
    }, [])
    return [ alertMsg, handleAlert ]
}

export default function SnackbarAlert(props: {
    msg?: SnackbarAlertMessage,
    autoHideDuration?: number
}) {
    const msg = props.msg;
    const msgQueue = React.useMemo((): SnackbarAlertMessage[] => [], [])
    const [, setUpdate] = React.useState<boolean>(false)
    React.useEffect(() => {
        if (msg !== undefined) {
            msgQueue.push(msg)
            setUpdate(prev => !prev)
        }
    }, [msg, msgQueue])

    return (
        <Stack direction='column'
            spacing={2}
            sx={{
                position: 'fixed',
                top: '10vh',
                right: '5vw',
                zIndex: 9999,
            }}>
            {msgQueue.map((msg, idx, arr) =>
                <Snackbar
                    open={arr[idx] !== undefined}
                    key={idx}
                    autoHideDuration={props.autoHideDuration ?? 6000}
                    sx={{ position: 'static' }}
                    onClose={(_, reason) => {
                        if(reason === 'timeout') {
                            delete arr[idx];
                            setUpdate(prev => !prev);
                        }
                    }}
                >
                    <Alert
                        severity={msg.severity ?? 'error'}
                        sx={{ width: '100%' }}
                        onClose={() => {
                            delete arr[idx];
                            setUpdate(prev => !prev);
                        }}
                    >
                        {msg.text}
                    </Alert>

                </Snackbar>

            )}
        </Stack>
    )
}