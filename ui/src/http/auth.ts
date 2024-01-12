import axios from "axios";
import { API_URL } from "../common/constants";

export const login = async (username: string, password: string): Promise<string | null> => {
    try{
        const { data } = await axios.post(
            API_URL + "/new-key/login",
            { 
                account: username, 
                password 
            });
        return data;
    }catch(err: any){
        if(err?.response?.status === 401) {
            alert("用户名或密码不正确")
        }else{
            console.error(err);
        }
        return null;
    }
};