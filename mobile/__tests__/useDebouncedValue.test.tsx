import React from 'react';
import {Text} from 'react-native';
import ReactTestRenderer from 'react-test-renderer';

import {useDebouncedValue} from '../src/hooks/useDebouncedValue';

function Probe({value}: {value: string}) {
  const debouncedValue = useDebouncedValue(value, 400);
  return <Text>{debouncedValue}</Text>;
}

test('updates only after the configured delay', async () => {
  jest.useFakeTimers();
  let renderer: ReactTestRenderer.ReactTestRenderer;

  await ReactTestRenderer.act(() => {
    renderer = ReactTestRenderer.create(<Probe value="rt" />);
  });
  await ReactTestRenderer.act(() => {
    renderer!.update(<Probe value="rtx" />);
  });

  expect(renderer!.root.findByType(Text).props.children).toBe('rt');

  await ReactTestRenderer.act(() => {
    jest.advanceTimersByTime(400);
  });

  expect(renderer!.root.findByType(Text).props.children).toBe('rtx');
  jest.useRealTimers();
});
