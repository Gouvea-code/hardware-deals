import {Image, StyleSheet, View} from 'react-native';

import {colors, spacing, typography} from '../theme';
import {Deal} from '../types/api';
import {formatCurrency} from '../utils/formatCurrency';
import {AppText} from './AppText';

type DealCardProps = {
  deal: Deal;
  compact?: boolean;
};

export function DealCard({deal, compact = false}: DealCardProps) {
  const hasPreviousPrice =
    deal.originalPrice !== null && deal.originalPrice > deal.price;

  return (
    <View
      accessibilityLabel={`${deal.productName}, ${formatCurrency(deal.price)}, loja ${deal.storeName}`}
      style={[styles.card, compact && styles.compactCard]}>
      {deal.imageUrl ? (
        <Image
          accessibilityLabel={`Imagem de ${deal.productName}`}
          resizeMode="contain"
          source={{uri: deal.imageUrl}}
          style={[styles.image, compact && styles.compactImage]}
        />
      ) : (
        <View style={[styles.image, styles.imageFallback, compact && styles.compactImage]}>
          <AppText style={styles.fallbackText}>HD</AppText>
        </View>
      )}

      <View style={styles.details}>
        <AppText numberOfLines={2} style={styles.name}>
          {deal.productName}
        </AppText>
        <AppText style={styles.store}>{deal.storeName}</AppText>
        {hasPreviousPrice ? (
          <AppText style={styles.previousPrice}>
            {formatCurrency(deal.originalPrice as number)}
          </AppText>
        ) : null}
        <AppText style={styles.price}>{formatCurrency(deal.price)}</AppText>
        <View style={styles.scoreBadge}>
          <AppText style={styles.score}>Score {deal.score}</AppText>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.surface,
    borderColor: colors.border,
    borderRadius: 16,
    borderWidth: 1,
    overflow: 'hidden',
    width: 220,
  },
  compactCard: {
    flexDirection: 'row',
    width: '100%',
  },
  compactImage: {
    height: 132,
    width: 132,
  },
  details: {
    flex: 1,
    padding: spacing.md,
  },
  fallbackText: {
    color: colors.textMuted,
    fontWeight: typography.weights.bold,
  },
  image: {
    backgroundColor: colors.background,
    height: 160,
    width: '100%',
  },
  imageFallback: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  name: {
    fontWeight: typography.weights.bold,
    minHeight: 40,
  },
  previousPrice: {
    color: colors.textMuted,
    fontSize: typography.sizes.label,
    marginTop: spacing.sm,
    textDecorationLine: 'line-through',
  },
  price: {
    color: colors.primary,
    fontSize: 20,
    fontWeight: typography.weights.bold,
    marginTop: spacing.xs,
  },
  score: {
    color: colors.scoreText,
    fontSize: 12,
    fontWeight: typography.weights.bold,
  },
  scoreBadge: {
    alignSelf: 'flex-start',
    backgroundColor: colors.scoreBackground,
    borderRadius: 999,
    marginTop: spacing.sm,
    paddingHorizontal: spacing.sm,
    paddingVertical: spacing.xs,
  },
  store: {
    color: colors.textMuted,
    fontSize: typography.sizes.label,
    marginTop: spacing.xs,
  },
});
