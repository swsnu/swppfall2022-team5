import create, { StateCreator } from "zustand";
import produce from "immer";
import { persist, PersistOptions } from "zustand/middleware";
import { apiClient } from "../api/client";

export interface AuthState {
  userToken: string;
  setToken: (token: string) => void;
}

export type authPersist = (
  config: StateCreator<AuthState>,
  options: PersistOptions<AuthState>,
) => StateCreator<AuthState>;

export const useAuthStore = create<AuthState>(
  (persist as authPersist)(
    (set) => ({
      userToken: "",
      setToken: (token: string) => {
        apiClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        set((state) => ({ ...state, userToken: token }));
      },
    }),
    {
      name: "userToken",
    },
  ),
);
