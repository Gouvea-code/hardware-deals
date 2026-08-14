/**
 * @format
 */

import React from 'react';
import ReactTestRenderer from 'react-test-renderer';
import App from '../App';

jest.mock('../src/navigation/RootNavigator', () => ({
  RootNavigator: () => null,
}));
jest.mock('../src/components/PushRegistration', () => ({
  PushRegistration: () => null,
}));

test('renders correctly', async () => {
  await ReactTestRenderer.act(() => {
    ReactTestRenderer.create(<App />);
  });
});
