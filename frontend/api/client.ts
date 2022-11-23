import axios from "axios";
import { useAuthStore } from "../store/auth";

export const apiClient = () =>  {
    const token = useAuthStore.getState().userToken
    return axios.create(
        { 
            baseURL: process.env.NEXT_PUBLIC_API_URL,
            headers: {
                'Authorization' : `Bearer ${token}`
            },
        }
    )
};
