export type ApiError = {
  code?: string;
  message: string;
  status: number;
};

export type PageResponse<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
};

export type DealClassification =
  | 'EXCELENTE'
  | 'OTIMA'
  | 'BOA'
  | 'INTERESSANTE'
  | 'NORMAL';

export type Deal = {
  id: string;
  productId: string;
  productName: string;
  brand: string;
  imageUrl: string | null;
  storeId: string;
  storeName: string;
  price: number;
  originalPrice: number | null;
  discountPercent: number;
  coupon: string | null;
  available: boolean;
  url: string;
  collectedAt: string;
  score: number;
  classification: DealClassification;
};

export type Product = {
  id: string;
  name: string;
  brand: string;
  model: string;
  category: string;
  ean: string | null;
  imageUrl: string | null;
};

export type PricePoint = {
  storeId: string;
  storeName: string;
  price: number;
  collectedAt: string;
};

export type PriceHistory = {
  productId: string;
  currentPrice: number | null;
  lowestPrice: number | null;
  highestPrice: number | null;
  averagePrice: number | null;
  medianPrice: number | null;
  priceVariation: number | null;
  history: PricePoint[];
};

export type Favorite = {
  id: string;
  productId: string;
  productName: string;
  brand: string;
  category: string;
  imageUrl: string | null;
  createdAt: string;
};
