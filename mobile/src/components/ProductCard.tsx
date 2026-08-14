import {Image, Pressable, StyleSheet, View} from 'react-native';

import {colors, spacing, typography} from '../theme';
import {Product} from '../types/api';
import {AppText} from './AppText';

export function ProductCard({product, onPress}: {product: Product; onPress?: () => void}) {
  return (
    <Pressable
      accessibilityLabel={`${product.name}, marca ${product.brand}, categoria ${product.category}`}
      accessibilityRole={onPress ? 'button' : undefined}
      onPress={onPress}
      style={({pressed}) => [styles.card, pressed && onPress && styles.pressed]}>
      {product.imageUrl ? (
        <Image
          accessibilityLabel={`Imagem de ${product.name}`}
          resizeMode="contain"
          source={{uri: product.imageUrl}}
          style={styles.image}
        />
      ) : (
        <View style={[styles.image, styles.fallback]}>
          <AppText style={styles.fallbackText}>HD</AppText>
        </View>
      )}
      <View style={styles.details}>
        <AppText numberOfLines={2} style={styles.name}>
          {product.name}
        </AppText>
        <AppText style={styles.brand}>{product.brand}</AppText>
        <View style={styles.category}>
          <AppText style={styles.categoryText}>{product.category}</AppText>
        </View>
      </View>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  brand: {
    color: colors.textMuted,
    marginTop: spacing.xs,
  },
  card: {
    backgroundColor: colors.surface,
    borderColor: colors.border,
    borderRadius: 16,
    borderWidth: 1,
    flexDirection: 'row',
    overflow: 'hidden',
  },
  category: {
    alignSelf: 'flex-start',
    backgroundColor: colors.background,
    borderRadius: 999,
    marginTop: spacing.sm,
    paddingHorizontal: spacing.sm,
    paddingVertical: spacing.xs,
  },
  categoryText: {
    color: colors.textMuted,
    fontSize: 12,
    fontWeight: typography.weights.medium,
  },
  details: {
    flex: 1,
    justifyContent: 'center',
    padding: spacing.md,
  },
  fallback: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  fallbackText: {
    color: colors.textMuted,
    fontWeight: typography.weights.bold,
  },
  image: {
    backgroundColor: colors.background,
    height: 132,
    width: 132,
  },
  name: {
    fontWeight: typography.weights.bold,
  },
  pressed: {opacity: 0.75},
});
