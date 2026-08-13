import {create} from 'zustand';

type SessionState = {
  accessToken: string | null;
  clearSession: () => void;
  setAccessToken: (accessToken: string) => void;
};

export const useSessionStore = create<SessionState>(set => ({
  accessToken: null,
  clearSession: () => set({accessToken: null}),
  setAccessToken: accessToken => set({accessToken}),
}));
