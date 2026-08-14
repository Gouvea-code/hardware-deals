import {apiClient} from '../src/services/apiClient';
import {addFavorite, getFavorites, removeFavorite} from '../src/services/favoriteService';

jest.mock('../src/services/apiClient', () => ({apiClient: {delete: jest.fn(), get: jest.fn(), put: jest.fn()}}));

test('uses the authenticated favorites endpoints', async () => {
  (apiClient.get as jest.Mock).mockResolvedValue({data: []});
  (apiClient.put as jest.Mock).mockResolvedValue({data: {id: '1'}});
  (apiClient.delete as jest.Mock).mockResolvedValue({});
  await getFavorites(); await addFavorite('p1'); await removeFavorite('p1');
  expect(apiClient.get).toHaveBeenCalledWith('/favorites');
  expect(apiClient.put).toHaveBeenCalledWith('/favorites/p1');
  expect(apiClient.delete).toHaveBeenCalledWith('/favorites/p1');
});
