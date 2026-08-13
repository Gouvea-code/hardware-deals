import {PageResponse, Product} from '../types/api';
import {apiClient} from './apiClient';

export async function searchProducts(query: string, page = 0) {
  const response = await apiClient.get<PageResponse<Product>>('/products/search', {
    params: {page, q: query.trim(), size: 20},
  });
  return response.data;
}
