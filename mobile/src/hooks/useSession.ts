import {useSessionStore} from '../store/sessionStore';

export function useSession() {
  const accessToken = useSessionStore(state => state.accessToken);
  const clearSession = useSessionStore(state => state.clearSession);
  const setAccessToken = useSessionStore(state => state.setAccessToken);

  return {accessToken, clearSession, setAccessToken};
}
