import {useSessionStore} from '../src/store/sessionStore';

describe('sessionStore', () => {
  beforeEach(() => useSessionStore.getState().clearSession());

  it('stores and clears the access token', () => {
    useSessionStore.getState().setAccessToken('token');
    expect(useSessionStore.getState().accessToken).toBe('token');

    useSessionStore.getState().clearSession();
    expect(useSessionStore.getState().accessToken).toBeNull();
  });
});
