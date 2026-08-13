import React from 'react';
import ReactTestRenderer from 'react-test-renderer';

import {ProductCard} from '../src/components/ProductCard';
import {Product} from '../src/types/api';

const product: Product = {
  brand: 'Nvidia',
  category: 'GPU',
  ean: null,
  id: 'product-1',
  imageUrl: null,
  model: 'RTX 5070',
  name: 'GeForce RTX 5070',
};

test('renders product name, brand and category', async () => {
  let renderer: ReactTestRenderer.ReactTestRenderer;
  await ReactTestRenderer.act(() => {
    renderer = ReactTestRenderer.create(<ProductCard product={product} />);
  });

  const output = JSON.stringify(renderer!.toJSON());
  expect(output).toContain('GeForce RTX 5070');
  expect(output).toContain('Nvidia');
  expect(output).toContain('GPU');
});
