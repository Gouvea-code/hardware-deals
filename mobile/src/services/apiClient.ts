import axios from 'axios';
import {Platform} from 'react-native';

import {useSessionStore} from '../store/sessionStore';

const localHost = Platform.OS === 'android' ? '10.0.2.2' : 'localhost';

export const API_BASE_URL = `http://${localHost}:8080/api/v1`;

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10_000,
});

apiClient.interceptors.request.use(config => {
  const accessToken = useSessionStore.getState().accessToken;

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});
