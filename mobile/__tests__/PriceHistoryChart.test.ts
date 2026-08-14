import {buildChart, filterByPeriod} from '../src/components/PriceHistoryChart';
import {PricePoint} from '../src/types/api';

const history: PricePoint[] = [
  {collectedAt: '2026-01-01T00:00:00Z', price: 120, storeId: '1', storeName: 'A'},
  {collectedAt: '2026-01-25T00:00:00Z', price: 100, storeId: '1', storeName: 'A'},
  {collectedAt: '2026-01-31T00:00:00Z', price: 110, storeId: '2', storeName: 'B'},
];

test('filters history relative to the newest point', () => {
  expect(filterByPeriod(history, 7)).toHaveLength(2);
  expect(filterByPeriod(history, 30)).toHaveLength(3);
  expect(filterByPeriod(history, 'all')).toEqual(history);
});

test('calculates chart metrics and current price', () => {
  expect(buildChart(history)).toMatchObject({current: 110, maximum: 120, minimum: 100});
  expect(buildChart([])).toBeNull();
});
