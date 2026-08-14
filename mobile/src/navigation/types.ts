export type RootStackParamList = {
  Favorites: undefined;
  Home: undefined;
  ProductDetails: {productId: string};
  Search: {initialQuery?: string} | undefined;
};
