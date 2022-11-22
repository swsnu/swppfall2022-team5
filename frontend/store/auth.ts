import create from "zustand";
import produce from "immer";

interface AuthState {
    userToken: string,
    setToken: (token: string) => void,
}


export const useAuthStore = create<AuthState>()((set, get) => ({
    userToken: "",
    setToken: (token) => {
        set(
            produce((state: AuthState) => {
                state.userToken = token
            })
        )
    },
}))