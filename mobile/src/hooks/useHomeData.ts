import {useQuery} from '@tanstack/react-query';

import {getCategories, getDeals} from '../services/homeService';

export function useHomeData() {
  const bestDeals = useQuery({
    queryFn: () => getDeals('score'),
    queryKey: ['deals', 'score'],
  });
  const recentDeals = useQuery({
    queryFn: () => getDeals('recent'),
    queryKey: ['deals', 'recent'],
  });
  const categories = useQuery({
    queryFn: getCategories,
    queryKey: ['categories'],
    staleTime: 5 * 60_000,
  });

  const queries = [bestDeals, recentDeals, categories];

  return {
    bestDeals: bestDeals.data ?? [],
    categories: categories.data ?? [],
    error: queries.find(query => query.error)?.error ?? null,
    isLoading: queries.some(query => query.isLoading),
    isRefreshing: queries.some(query => query.isRefetching),
    recentDeals: recentDeals.data ?? [],
    refresh: async () => {
      await Promise.all(queries.map(query => query.refetch()));
    },
  };
}
