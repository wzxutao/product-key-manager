import React, { useState } from 'react';
import {
  Box, Grid, Paper,
  Button,
  Typography
} from '@mui/material';
import { useCookies } from 'react-cookie';
import { COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME } from '../../common/constants';

const LogoutSection = (props: {
  username: string
}) => {
  const [, , removeCookie] = useCookies([COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME])

  const handleLogOut = React.useCallback(() => {
    removeCookie(COOKIE_KEY_NORMAL_AUTH)
    removeCookie(COOKIE_KEY_USERNAME)
  }, [])

  return (
    <Grid id="LogoutSection" item xs={12} lg={5} component={Paper} elevation={6} square>
      <Box
        sx={{
          my: 8,
          mx: 4,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography variant="h2" gutterBottom>
          {props.username}
        </Typography>
        <Button
          type="button"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
          onClick={handleLogOut}
        >
          登出
        </Button>
      </Box>
    </Grid>
  )
}

export default LogoutSection;