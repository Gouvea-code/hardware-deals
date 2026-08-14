import {Platform} from 'react-native'; import {apiClient} from './apiClient';
export type MobilePlatform='android'|'ios';
export async function registerDevice(token:string){return (await apiClient.put('/devices',{platform:Platform.OS as MobilePlatform,token})).data;}
export async function deactivateDevice(token:string){await apiClient.delete('/devices',{params:{token}});}
