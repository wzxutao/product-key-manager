import React, {  } from 'react';
import {
  Box, Grid, Paper,
  Button,
  Typography
} from '@mui/material';
import { useCookies } from 'react-cookie';
import { COOKIE_KEY_ADMIN_AUTH, COOKIE_KEY_AUTH_EXPIRATION, COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME } from '../../common/constants';

const LogoutSection = (props: {
  username: string
}) => {
  const [, , removeCookie] = useCookies([COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME
    , COOKIE_KEY_ADMIN_AUTH, COOKIE_KEY_AUTH_EXPIRATION])

  const handleLogOut = React.useCallback(() => {
    removeCookie(COOKIE_KEY_NORMAL_AUTH)
    removeCookie(COOKIE_KEY_USERNAME)
    removeCookie(COOKIE_KEY_ADMIN_AUTH)
    removeCookie(COOKIE_KEY_AUTH_EXPIRATION)
  }, [removeCookie])

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
          注销登录
        </Button>
      </Box>
    </Grid>
  )
}

export default LogoutSection;