export type RootStackParamList = {
  AlertForm: {productId: string; productName: string; currentPrice?: number};
  Alerts: undefined;
  Favorites: undefined;
  Home: undefined;
  ProductDetails: {productId: string};
  Search: {initialQuery?: string} | undefined;
};
