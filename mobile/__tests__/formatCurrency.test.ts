import {formatCurrency} from '../src/utils/formatCurrency';

test('formats a value in Brazilian reais', () => {
  expect(formatCurrency(1999.9)).toMatch(/1\.999,90/);
});
