import axios from "axios";
import { API_URL, ErrorLogger } from './base-api'

export const login = async (username: string, password: string, 
    errorLogger?: ErrorLogger): Promise<void> => {
    try {
        const fd = new FormData();
        fd.append('username', username);
        fd.append('password', password);

        await axios.post(
            API_URL +  '/auth/login',
            fd, {
                headers:{
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
            withCredentials: true
        });
    } catch (err: any) {
        console.error(err);

        if (err?.response?.status === 401) {
            const msg = "用户名或密码不正确"
            errorLogger ? errorLogger(msg) : alert(msg)
        } else {
            const msg = '登陆失败：' + (err?.response?.status ?? '未知错误')
            errorLogger ? errorLogger(msg) : alert(msg)
        }
        throw err;
    }
};