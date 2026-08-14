import {apiClient} from '../src/services/apiClient';
import {
  getProduct,
  getProductOffers,
  getProductPriceHistory,
} from '../src/services/productDetailsService';

jest.mock('../src/services/apiClient', () => ({apiClient: {get: jest.fn()}}));

const getMock = apiClient.get as jest.Mock;

beforeEach(() => getMock.mockResolvedValue({data: {id: 'result'}}));

test('loads the product details resources', async () => {
  await getProduct('product-1');
  await getProductOffers('product-1');
  await getProductPriceHistory('product-1');

  expect(getMock).toHaveBeenNthCalledWith(1, '/products/product-1');
  expect(getMock).toHaveBeenNthCalledWith(2, '/products/product-1/offers', {
    params: {sort: 'score'},
  });
  expect(getMock).toHaveBeenNthCalledWith(3, '/products/product-1/price-history');
});
