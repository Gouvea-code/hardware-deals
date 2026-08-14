import axios,{AxiosError,InternalAxiosRequestConfig} from 'axios';

import {API_BASE_URL} from '../config/environment';
import {useSessionStore} from '../store/sessionStore';
import {TokenPair} from '../types/auth';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10_000,
});

type RetryConfig=InternalAxiosRequestConfig&{_retry?:boolean};
let refreshRequest:Promise<string>|null=null;

apiClient.interceptors.request.use(config => {
  const accessToken = useSessionStore.getState().accessToken;

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

apiClient.interceptors.response.use(response=>response,async(error:AxiosError)=>{
 const config=error.config as RetryConfig|undefined;const refreshToken=useSessionStore.getState().refreshToken;
 if(error.response?.status!==401||!config||config._retry||!refreshToken)return Promise.reject(error);
 config._retry=true;
 try{
  refreshRequest??=axios.post<TokenPair>(`${API_BASE_URL}/auth/refresh`,{refreshToken}).then(async response=>{
   await useSessionStore.getState().setSession(response.data);return response.data.accessToken;
  }).finally(()=>{refreshRequest=null;});
  const accessToken=await refreshRequest;config.headers.Authorization=`Bearer ${accessToken}`;return apiClient(config);
 }catch(refreshError){await useSessionStore.getState().clearSession();return Promise.reject(refreshError);}
});
