import {useQuery} from '@tanstack/react-query';

import {
  getProduct,
  getProductOffers,
  getProductPriceHistory,
} from '../services/productDetailsService';

export function useProductDetails(productId: string) {
  const product = useQuery({
    queryFn: () => getProduct(productId),
    queryKey: ['product', productId],
  });
  const offers = useQuery({
    queryFn: () => getProductOffers(productId),
    queryKey: ['product', productId, 'offers'],
  });
  const priceHistory = useQuery({
    queryFn: () => getProductPriceHistory(productId),
    queryKey: ['product', productId, 'price-history'],
  });
  const queries = [product, offers, priceHistory];

  return {
    error: queries.find(query => query.error)?.error ?? null,
    isLoading: queries.some(query => query.isLoading),
    offers: offers.data ?? [],
    priceHistory: priceHistory.data,
    product: product.data,
    refresh: async () => {
      await Promise.all(queries.map(query => query.refetch()));
    },
  };
}
