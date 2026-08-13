import {PropsWithChildren} from 'react';
import {StyleSheet, Text, TextProps} from 'react-native';

import {colors, typography} from '../theme';

export function AppText({children, style, ...props}: PropsWithChildren<TextProps>) {
  return (
    <Text {...props} style={[styles.text, style]}>
      {children}
    </Text>
  );
}

const styles = StyleSheet.create({
  text: {
    color: colors.text,
    fontSize: typography.sizes.body,
  },
});
