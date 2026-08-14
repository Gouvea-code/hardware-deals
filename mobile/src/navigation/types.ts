export type RootStackParamList = {
  Account: undefined;
  AlertForm: {productId: string; productName: string; currentPrice?: number};
  Alerts: undefined;
  Favorites: undefined;
  Home: undefined;
  Login: undefined;
  ProductDetails: {productId: string};
  Register: undefined;
  ForgotPassword: undefined;
  ResetPassword: {token?: string} | undefined;
  VerifyEmail: {token?: string} | undefined;
  Search: {initialQuery?: string} | undefined;
};
