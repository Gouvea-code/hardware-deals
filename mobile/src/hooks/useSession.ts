import {useSessionStore} from '../store/sessionStore';

export function useSession() {
  const accessToken = useSessionStore(state => state.accessToken);
  const clearSession = useSessionStore(state => state.clearSession);
  const refreshToken = useSessionStore(state => state.refreshToken);
  const setSession = useSessionStore(state => state.setSession);

  return {accessToken, clearSession, refreshToken, setSession};
}
