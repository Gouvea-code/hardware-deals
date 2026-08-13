import {PropsWithChildren} from 'react';
import {StyleSheet, View} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';

import {colors, spacing} from '../theme';

export function Screen({children}: PropsWithChildren) {
  return (
    <SafeAreaView edges={['bottom']} style={styles.safeArea}>
      <View style={styles.content}>{children}</View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  content: {
    flex: 1,
    padding: spacing.md,
  },
  safeArea: {
    backgroundColor: colors.background,
    flex: 1,
  },
});
