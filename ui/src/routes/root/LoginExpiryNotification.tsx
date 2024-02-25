import { Backdrop } from '@mui/material';
import React from 'react';
import { useCookies } from 'react-cookie';
import {  COOKIE_KEY_AUTH_EXPIRATION } from '../../common/constants';


export default function LoginExpiryNotification() {
    const [cookies, ,] = useCookies([COOKIE_KEY_AUTH_EXPIRATION])
    const cookieExpiration = cookies[COOKIE_KEY_AUTH_EXPIRATION];
    const [open, setOpen] = React.useState<boolean>(false);
    const [refreshFlag, setRefreshFlag] = React.useState<boolean>(false);

    React.useEffect(() => {
        if (cookieExpiration === undefined) return;
        const now = new Date().getTime();
        const expiration = new Date(cookieExpiration).getTime();
        if(expiration - now <= 1000 * 60 * 5) {
            setOpen(true);
            return;
        }

        const handle = setTimeout(() => {
            setRefreshFlag(prev => !prev);
        }, 1000)

        return () => clearTimeout(handle);
    }, [cookieExpiration, refreshFlag])

    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
            open={open}
            onClick={() => setOpen(false)}
        >
            <h1>登录有效期剩余不足5分钟，请尽快重新登录！</h1>
            <p>（点击任意位置关闭此弹窗）</p>
        </Backdrop>
    )
}