import create, { StateCreator } from "zustand";
import produce from "immer";
import { persist, PersistOptions } from "zustand/middleware"

export interface AuthState {
    userToken: string,
    setToken: (token: string) => void,
}

export type authPersist = (
    config: StateCreator<AuthState>,
    options: PersistOptions<AuthState>
) => StateCreator<AuthState>

export const useAuthStore = create<AuthState>(
    (persist as authPersist) (
        (set) => ({
            userToken: "",
            setToken: (token: string) => {
                set((state) => ({...state, userToken: token}))
            }
        }),
        {
            name: 'userToken'
        }
    )
)