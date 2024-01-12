import React, { useState } from 'react';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { 
    Box, FormControlLabel, Grid, Paper, 
    Avatar, Typography, 
    TextField, Checkbox, Button } from '@mui/material';
import { useCookies } from 'react-cookie';
import { login } from '../../http/auth';
import { COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME } from '../../common/constants';

const LoginSection = () => {
    const [, setCookie,] = useCookies([COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME])
    const [iusername, setIusername] = useState<string | null>(null)
    const [ipassword, setIpassword] = useState<string | null>(null)

    const handleLoginSubmit: React.FormEventHandler<HTMLFormElement> = async (ev) => {
        ev.preventDefault()
        const resp = await login(iusername!, ipassword!)
        if(resp !== null) {
            setCookie(COOKIE_KEY_NORMAL_AUTH, resp)
            setCookie(COOKIE_KEY_USERNAME, iusername)
        }
    }

    return (
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
          <Box component="form" onSubmit={handleLoginSubmit} sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="用户名"
              name="username"
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
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" defaultChecked/>}
              label="30天内免登录"
            />
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
      </Grid>
    )
}

export default LoginSection;