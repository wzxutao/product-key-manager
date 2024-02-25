import React from 'react';
import { Outlet } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { CssBaseline } from '@mui/material';
import TheAppBar from './appbar/TheAppBar';
import LoginExpiryNotification from './LoginExpiryNotification';


const defaultTheme = createTheme()

const Root = () => {
    return (
        <ThemeProvider theme={defaultTheme}>
            <CssBaseline />
                <TheAppBar />
                <LoginExpiryNotification />
                <Outlet />
        </ThemeProvider>
    )
}

export default Root;