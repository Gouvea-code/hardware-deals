import {PriceAlert} from '../types/api'; import {apiClient} from './apiClient';
export async function getAlerts(){return (await apiClient.get<PriceAlert[]>('/alerts')).data;}
export async function saveAlert(productId:string,targetPrice:number){return (await apiClient.put<PriceAlert>(`/alerts/${productId}`,{targetPrice})).data;}
export async function removeAlert(productId:string){await apiClient.delete(`/alerts/${productId}`);}
