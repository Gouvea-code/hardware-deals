import {Deal, PageResponse, Product} from '../types/api';
import {apiClient} from './apiClient';

export type DealSort = 'score' | 'recent';

export async function getDeals(sort: DealSort) {
  const response = await apiClient.get<Deal[]>('/deals', {params: {sort}});
  return response.data;
}

export async function getCategories() {
  const response = await apiClient.get<PageResponse<Product>>('/products', {
    params: {size: 100},
  });

  return Array.from(
    new Set(
      response.data.content
        .map(product => product.category?.trim())
        .filter((category): category is string => Boolean(category)),
    ),
  ).sort((first, second) => first.localeCompare(second, 'pt-BR'));
}
