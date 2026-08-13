import {StyleSheet} from 'react-native';

import {spacing, typography} from '../theme';
import {AppText} from './AppText';

export function SectionHeader({title}: {title: string}) {
  return <AppText style={styles.title}>{title}</AppText>;
}

const styles = StyleSheet.create({
  title: {
    fontSize: 20,
    fontWeight: typography.weights.bold,
    marginBottom: spacing.md,
  },
});
