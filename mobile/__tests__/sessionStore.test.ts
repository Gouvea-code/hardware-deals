import {useSessionStore} from '../src/store/sessionStore';

jest.mock('../src/services/sessionStorage', () => ({
  clearStoredSession: jest.fn().mockResolvedValue(undefined),
  loadSession: jest.fn().mockResolvedValue(null),
  saveSession: jest.fn().mockResolvedValue(undefined),
}));

describe('sessionStore', () => {
  beforeEach(async () => useSessionStore.getState().clearSession());

  it('stores and clears the token pair', async () => {
    await useSessionStore.getState().setSession({accessToken: 'token', refreshToken: 'refresh'});
    expect(useSessionStore.getState().accessToken).toBe('token');
    expect(useSessionStore.getState().refreshToken).toBe('refresh');

    await useSessionStore.getState().clearSession();
    expect(useSessionStore.getState().accessToken).toBeNull();
  });
});
