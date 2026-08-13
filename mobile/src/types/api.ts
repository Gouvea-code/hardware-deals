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
