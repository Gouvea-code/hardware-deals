import {apiClient} from '../src/services/apiClient';
import {searchProducts} from '../src/services/searchService';

jest.mock('../src/services/apiClient', () => ({
  apiClient: {get: jest.fn()},
}));

test('trims and sends the search query with pagination', async () => {
  const getMock = apiClient.get as jest.Mock;
  getMock.mockResolvedValue({data: {content: [], page: 0}});

  await searchProducts('  rtx  ', 2);

  expect(getMock).toHaveBeenCalledWith('/products/search', {
    params: {page: 2, q: 'rtx', size: 20},
  });
});
