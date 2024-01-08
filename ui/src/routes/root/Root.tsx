import React from 'react';
import { Outlet } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { Container, CssBaseline } from '@mui/material';
import TheAppBar from './appbar/TheAppBar';


const defaultTheme = createTheme()

const Root = () => {
    return (
        <ThemeProvider theme={defaultTheme}>
            <CssBaseline />
                <TheAppBar />
                <Outlet />
        </ThemeProvider>
    )
}

export default Root;