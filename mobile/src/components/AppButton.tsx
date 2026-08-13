import {Pressable, StyleSheet} from 'react-native';

import {colors, spacing, typography} from '../theme';
import {AppText} from './AppText';

type AppButtonProps = {
  label: string;
  onPress: () => unknown;
  disabled?: boolean;
};

export function AppButton({label, onPress, disabled = false}: AppButtonProps) {
  return (
    <Pressable
      accessibilityRole="button"
      disabled={disabled}
      onPress={onPress}
      style={({pressed}) => [
        styles.button,
        pressed && styles.pressed,
        disabled && styles.disabled,
      ]}>
      <AppText style={styles.label}>{label}</AppText>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  button: {
    alignItems: 'center',
    backgroundColor: colors.primary,
    borderRadius: 12,
    paddingHorizontal: spacing.lg,
    paddingVertical: spacing.md,
  },
  disabled: {opacity: 0.5},
  label: {
    color: colors.onPrimary,
    fontWeight: typography.weights.bold,
  },
  pressed: {backgroundColor: colors.primaryPressed},
});
