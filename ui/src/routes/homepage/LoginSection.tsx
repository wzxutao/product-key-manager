import React, { useCallback, useState } from 'react';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import {
  Box, FormControlLabel, Grid, Paper,
  Avatar, Typography,
  TextField, Checkbox, Button, Tooltip, Chip
} from '@mui/material';
import { useCookies } from 'react-cookie';
import { login } from '../../http/auth';
import { COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME, COOKIE_KEY_ADMIN_AUTH } from '../../common/constants';

const LoginSection = () => {
  const [, setCookie, removeCookie] = useCookies([COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME, COOKIE_KEY_ADMIN_AUTH])
  const [iusername, setIusername] = useState<string | null>(null)
  const [ipassword, setIpassword] = useState<string | null>(null)
  const [adminChecked, setAdminChecked] = useState<boolean>(false)

  const handleLoginSubmit: React.FormEventHandler<HTMLFormElement> = useCallback(async (ev) => {
    ev.preventDefault()
    const resp = await login(iusername!, ipassword!, adminChecked)
    if (resp !== null) {
      if (adminChecked) {
        removeCookie(COOKIE_KEY_NORMAL_AUTH)
        setCookie(COOKIE_KEY_ADMIN_AUTH, resp)
      } else {
        removeCookie(COOKIE_KEY_ADMIN_AUTH)
        setCookie(COOKIE_KEY_NORMAL_AUTH, resp)
      }
      setCookie(COOKIE_KEY_USERNAME, iusername)
    }
  }, [iusername, ipassword, setCookie, adminChecked])

  const handleToggleAdminCheckbox = useCallback(() => {
    setAdminChecked(prev => !prev)
  }, [])

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
        <Box component="form" onSubmit={handleLoginSubmit} sx={{ mt: 1, width: '80%' }}>
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
          <Grid container>
            <Grid item component={Chip} xs={6}
              sx={{ 
                visibility: adminChecked ? 'hidden' : 'visible',
                backgroundColor: 'transparent',
                justifyContent: 'flex-start'
              }}
              label={<Chip label="30天内免登录" />}
            />
            <Tooltip title="以管理员身份登录">
              <Grid item component={FormControlLabel} xs={6} sx={{ justifyContent: 'end' }}
                control={
                  <Checkbox value="admin" color="primary"
                    checked={adminChecked} onChange={handleToggleAdminCheckbox} />
                }
                label="管理员"
              />
            </Tooltip>
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
  )
}

export default LoginSection;