import {apiClient} from '../src/services/apiClient';
import {getCategories, getDeals} from '../src/services/homeService';

jest.mock('../src/services/apiClient', () => ({
  apiClient: {get: jest.fn()},
}));

const getMock = apiClient.get as jest.Mock;

beforeEach(() => getMock.mockReset());

test('requests deals using the selected ordering', async () => {
  getMock.mockResolvedValue({data: [{id: 'deal-1'}]});

  await expect(getDeals('score')).resolves.toEqual([{id: 'deal-1'}]);
  expect(getMock).toHaveBeenCalledWith('/deals', {params: {sort: 'score'}});
});

test('returns unique sorted product categories', async () => {
  getMock.mockResolvedValue({
    data: {
      content: [
        {category: 'Placas de vídeo'},
        {category: 'Memória'},
        {category: 'Placas de vídeo'},
      ],
    },
  });

  await expect(getCategories()).resolves.toEqual(['Memória', 'Placas de vídeo']);
});
