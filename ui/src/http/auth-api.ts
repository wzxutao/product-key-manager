import axios from "axios";
import { API_URL } from './base-api'

export const login = async (username: string, password: string, 
    isAdmin: boolean, 
    errorLogger?: (msg: string) => void): Promise<void> => {
    try {
        const fd = new FormData();
        fd.append('account', username);
        fd.append('password', password);

        await axios.post(
            API_URL + (isAdmin ? '/auth/login' : '/new-key/login'),
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