import {create} from 'zustand';
import {clearStoredSession, loadSession, saveSession} from '../services/sessionStorage';
import {StoredSession} from '../types/auth';

type SessionState = {
  accessToken: string | null;
  refreshToken: string | null;
  hydrated: boolean;
  clearSession: () => Promise<void>;
  hydrate: () => Promise<void>;
  setSession: (session: StoredSession) => Promise<void>;
};

export const useSessionStore = create<SessionState>(set => ({
  accessToken: null,
  refreshToken: null,
  hydrated: false,
  clearSession: async () => {await clearStoredSession();set({accessToken:null,refreshToken:null});},
  hydrate: async () => {const session=await loadSession();set({accessToken:session?.accessToken??null,
    refreshToken:session?.refreshToken??null,hydrated:true});},
  setSession: async session => {await saveSession(session);set(session);},
}));
