import {Deal, PriceHistory, Product} from '../types/api';
import {apiClient} from './apiClient';

export async function getProduct(productId: string) {
  const response = await apiClient.get<Product>(`/products/${productId}`);
  return response.data;
}

export async function getProductOffers(productId: string) {
  const response = await apiClient.get<Deal[]>(`/products/${productId}/offers`, {
    params: {sort: 'score'},
  });
  return response.data;
}

export async function getProductPriceHistory(productId: string) {
  const response = await apiClient.get<PriceHistory>(
    `/products/${productId}/price-history`,
  );
  return response.data;
}
