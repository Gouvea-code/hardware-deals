import {Favorite} from '../types/api';
import {apiClient} from './apiClient';

export async function getFavorites() {
  return (await apiClient.get<Favorite[]>('/favorites')).data;
}
export async function addFavorite(productId: string) {
  return (await apiClient.put<Favorite>(`/favorites/${productId}`)).data;
}
export async function removeFavorite(productId: string) {
  await apiClient.delete(`/favorites/${productId}`);
}
