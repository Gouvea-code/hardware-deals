export type RootStackParamList = {
  Home: undefined;
  ProductDetails: {productId: string};
  Search: {initialQuery?: string} | undefined;
};
