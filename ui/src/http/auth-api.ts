import axios from "axios";
import { API_URL } from './base-api'

export const login = async (username: string, password: string, isAdmin: boolean): Promise<string | null> => {
    try {
        const { data } = await axios.post(
            API_URL + (isAdmin ? '/auth/login' : '/new-key/login'),
            {
                account: username,
                password
            });
        return data;
    } catch (err: any) {
        if (err?.response?.status === 401) {
            alert("用户名或密码不正确")
        } else {
            console.error(err);
            alert('登陆失败：' + err?.response?.status ?? '未知错误')
        }
        return null;
    }
};