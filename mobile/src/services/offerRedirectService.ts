import {apiClient} from './apiClient';

type OfferRedirect = {clickId:string;redirectUrl:string;clickedAt:string};

export async function registerOfferClick(offerId:string){
  return (await apiClient.post<OfferRedirect>(`/offers/${offerId}/click`)).data;
}
