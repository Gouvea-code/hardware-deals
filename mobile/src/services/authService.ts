import {TokenPair} from '../types/auth';import {apiClient} from './apiClient';
export async function login(email:string,password:string){return(await apiClient.post<TokenPair>('/auth/login',{email,password})).data;}
export async function register(name:string,email:string,password:string){return(await apiClient.post('/auth/register',{email,name,password})).data;}
export async function forgotPassword(email:string){return(await apiClient.post('/auth/forgot-password',{email})).data;}
export async function logout(refreshToken:string){return(await apiClient.post('/auth/logout',{refreshToken})).data;}
export async function verifyEmail(token:string){return(await apiClient.post('/auth/verify-email',{token})).data;}
export async function resetPassword(token:string,newPassword:string){return(await apiClient.post('/auth/reset-password',{newPassword,token})).data;}
