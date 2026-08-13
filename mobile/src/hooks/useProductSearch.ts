import {useQuery} from '@tanstack/react-query';

import {searchProducts} from '../services/searchService';
import {useDebouncedValue} from './useDebouncedValue';

export const MIN_SEARCH_LENGTH = 2;

export function useProductSearch(query: string) {
  const normalizedQuery = query.trim();
  const debouncedQuery = useDebouncedValue(normalizedQuery);
  const canSearch = debouncedQuery.length >= MIN_SEARCH_LENGTH;
  const result = useQuery({
    enabled: canSearch,
    queryFn: () => searchProducts(debouncedQuery),
    queryKey: ['products', 'search', debouncedQuery],
  });

  return {
    ...result,
    canSearch,
    debouncedQuery,
    isDebouncing: normalizedQuery !== debouncedQuery,
    products: result.data?.content ?? [],
  };
}
