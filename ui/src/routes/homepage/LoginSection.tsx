import React, { useCallback, useState } from 'react';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import {
  Box, Grid, Paper,
  Avatar, Typography,
  TextField, Button, Chip
} from '@mui/material';
import { login } from '../../http/auth-api';
import SnackbarAlert, { useAlert } from '../../components/SnackbarAlert';

const LoginSection = () => {
  const [iusername, setIusername] = useState<string | null>(null)
  const [ipassword, setIpassword] = useState<string | null>(null)
  const [alertMsg, handleAlert] = useAlert();

  const handleLoginSubmit: React.FormEventHandler<HTMLFormElement> = useCallback(async (ev) => {
    ev.preventDefault()

    try {
      await login(iusername!, ipassword!, handleAlert)
      handleAlert("登陆成功", 'success')
    } catch (e) {
    }
  }, [iusername, ipassword, handleAlert])

  return (
    <>
      <SnackbarAlert msg={alertMsg} />
      <Grid id="LoginSection" item xs={12} lg={5} component={Paper} elevation={6} square>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            请登录
          </Typography>
          <Box component="form"
            onSubmit={handleLoginSubmit} sx={{ mt: 1, width: '80%' }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="用户名"
              name="account"
              autoComplete="username"
              autoFocus
              onInput={(ev: any) => setIusername(ev.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="密码"
              type="password"
              id="password"
              autoComplete="current-password"
              onInput={(ev: any) => setIpassword(ev.target.value)}
            />
            <Grid container>
              <Grid item component={Chip} xs={6}
                sx={{
                  backgroundColor: 'transparent',
                  justifyContent: 'flex-start'
                }}
                label={<Chip label="30天内免登录" />}
              />
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              登录
            </Button>
          </Box>
        </Box>
      </Grid >
    </>

  )
}

export default LoginSection;