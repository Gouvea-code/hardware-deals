import {StyleSheet, View} from 'react-native';

import {colors, spacing, typography} from '../theme';
import {AppText} from './AppText';

type CategoryChipProps = {
  name: string;
};

export function CategoryChip({name}: CategoryChipProps) {
  return (
    <View accessibilityLabel={`Categoria ${name}`} style={styles.container}>
      <AppText style={styles.label}>{name}</AppText>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.surface,
    borderColor: colors.border,
    borderRadius: 999,
    borderWidth: 1,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.sm,
  },
  label: {
    fontSize: typography.sizes.label,
    fontWeight: typography.weights.medium,
  },
});
