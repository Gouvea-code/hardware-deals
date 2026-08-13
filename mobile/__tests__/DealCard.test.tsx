import React from 'react';
import ReactTestRenderer from 'react-test-renderer';

import {DealCard} from '../src/components/DealCard';
import {Deal} from '../src/types/api';

const deal: Deal = {
  available: true,
  brand: 'NVIDIA',
  classification: 'EXCELENTE',
  collectedAt: '2026-08-13T12:00:00',
  coupon: null,
  discountPercent: 20,
  id: 'deal-1',
  imageUrl: null,
  originalPrice: 2499.9,
  price: 1999.9,
  productId: 'product-1',
  productName: 'GeForce RTX',
  score: 92,
  storeId: 'store-1',
  storeName: 'Loja Teste',
  url: 'https://example.com/deal',
};

test('renders the complete deal summary', async () => {
  let renderer: ReactTestRenderer.ReactTestRenderer;
  await ReactTestRenderer.act(() => {
    renderer = ReactTestRenderer.create(<DealCard deal={deal} />);
  });

  const output = JSON.stringify(renderer!.toJSON());
  expect(output).toContain('GeForce RTX');
  expect(output).toContain('Loja Teste');
  expect(output).toContain('1.999,90');
  expect(output).toContain('2.499,90');
  expect(output).toContain('Score ');
  expect(output).toContain('92');
});
