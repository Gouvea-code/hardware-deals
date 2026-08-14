import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {useEffect} from 'react';
import {ActivityIndicator, Alert, Image, Linking, ScrollView, StyleSheet, View} from 'react-native';

import {AppButton, AppText, PriceHistoryChart, Screen, SectionHeader} from '../components';
import {useProductDetails} from '../hooks/useProductDetails';
import {useFavorites} from '../hooks/useFavorites';
import {RootStackParamList} from '../navigation/types';
import {colors, spacing, typography} from '../theme';
import {formatCurrency} from '../utils/formatCurrency';
import {registerOfferClick} from '../services/offerRedirectService';
import {trackEventSafely} from '../services/analyticsService';

type Props = NativeStackScreenProps<RootStackParamList, 'ProductDetails'>;

export function ProductDetailsScreen({navigation, route}: Props) {
  const {error, isLoading, offers, priceHistory, product, refresh} =
    useProductDetails(route.params.productId);
  const favorites = useFavorites();
  useEffect(()=>{trackEventSafely('PRODUCT_VIEW',{productId:route.params.productId});},[route.params.productId]);

  if (isLoading) {
    return <Feedback loading title="Carregando detalhes..." />;
  }

  if (error || !product) {
    return (
      <Feedback
        action={{label: 'Tentar novamente', onPress: refresh}}
        description="Verifique sua conexão e tente novamente."
        title="Não foi possível carregar o produto"
      />
    );
  }

  const bestOffer = offers[0];
  const currentPrice = priceHistory?.currentPrice ?? bestOffer?.price ?? null;
  const isFavorite = favorites.favorites.some(item => item.productId === product.id);
  const openOffer = async (offerId: string) => {
    try {
      const {redirectUrl} = await registerOfferClick(offerId);
      if (!(await Linking.canOpenURL(redirectUrl))) throw new Error('unsupported URL');
      await Linking.openURL(redirectUrl);
    } catch {
      Alert.alert('Não foi possível abrir a oferta', 'Tente novamente em alguns instantes.');
    }
  };

  return (
    <Screen>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        {product.imageUrl ? (
          <Image
            accessibilityLabel={`Imagem de ${product.name}`}
            resizeMode="contain"
            source={{uri: product.imageUrl}}
            style={styles.image}
          />
        ) : (
          <View style={[styles.image, styles.imageFallback]}>
            <AppText style={styles.fallbackText}>HD</AppText>
          </View>
        )}

        <AppText style={styles.brand}>{product.brand}</AppText>
        <AppText style={styles.title}>{product.name}</AppText>
        <AppText style={styles.category}>{product.category}</AppText>

        <View style={styles.stats}>
          <Stat label="Preço atual" value={money(currentPrice)} emphasized />
          <Stat label="Menor preço" value={money(priceHistory?.lowestPrice)} />
          <Stat label="Preço médio" value={money(priceHistory?.averagePrice)} />
          <Stat label="Deal Score" value={bestOffer ? String(bestOffer.score) : '—'} />
        </View>

        <View style={styles.actions}>
          <AppButton
            disabled={!favorites.accessToken || favorites.isUpdating}
            label={!favorites.accessToken ? 'Entre para favoritar' : isFavorite ? 'Remover dos favoritos' : 'Favoritar'}
            onPress={() => isFavorite ? favorites.remove(product.id) : favorites.add(product.id)}
          />
          <AppButton disabled={!favorites.accessToken} label={!favorites.accessToken ? 'Entre para criar alerta' : 'Criar alerta'}
            onPress={() => navigation.navigate('AlertForm', {productId: product.id, productName: product.name,
              currentPrice: currentPrice ?? undefined})} />
        </View>
        <AppText style={styles.helper}>
          {!favorites.accessToken
            ? 'A autenticação é necessária para sincronizar favoritos e alertas.'
            : 'Seus favoritos são sincronizados com sua conta.'}
        </AppText>

        <View style={styles.section}>
          <SectionHeader title="Lojas" />
          {offers.length ? (
            <View style={styles.list}>
              {offers.map(offer => (
                <View key={offer.id} style={styles.row}>
                  <View style={styles.rowContent}>
                    <AppText style={styles.rowTitle}>{offer.storeName}</AppText>
                    {offer.coupon ? (
                      <AppText style={styles.coupon}>Cupom: {offer.coupon}</AppText>
                    ) : null}
                  </View>
                  <View style={styles.right}>
                    <AppText style={styles.offerPrice}>{formatCurrency(offer.price)}</AppText>
                    <AppText style={styles.score}>Score {offer.score}</AppText>
                    <AppButton label="Ver oferta" onPress={() => openOffer(offer.id)} />
                  </View>
                </View>
              ))}
            </View>
          ) : (
            <AppText style={styles.muted}>Nenhuma loja disponível agora.</AppText>
          )}
        </View>

        <View style={styles.section}>
          <SectionHeader title="Histórico de preços" />
          <PriceHistoryChart history={priceHistory?.history ?? []} />
        </View>

        <View style={styles.section}>
          <SectionHeader title="Histórico recente" />
          {priceHistory?.history.length ? (
            <View style={styles.list}>
              {priceHistory.history.slice(-5).reverse().map(point => (
                <View key={`${point.storeId}-${point.collectedAt}`} style={styles.row}>
                  <View style={styles.rowContent}>
                    <AppText style={styles.rowTitle}>{point.storeName}</AppText>
                    <AppText style={styles.muted}>{formatDate(point.collectedAt)}</AppText>
                  </View>
                  <AppText style={styles.rowTitle}>{formatCurrency(point.price)}</AppText>
                </View>
              ))}
            </View>
          ) : (
            <AppText style={styles.muted}>Ainda não há histórico de preço.</AppText>
          )}
        </View>
      </ScrollView>
    </Screen>
  );
}

function Stat({emphasized = false, label, value}: {emphasized?: boolean; label: string; value: string}) {
  return (
    <View style={styles.stat}>
      <AppText style={styles.muted}>{label}</AppText>
      <AppText style={[styles.statValue, emphasized && styles.emphasized]}>{value}</AppText>
    </View>
  );
}

function Feedback({action, description, loading, title}: {
  action?: {label: string; onPress: () => unknown};
  description?: string;
  loading?: boolean;
  title: string;
}) {
  return (
    <Screen>
      <View style={styles.centered}>
        {loading ? <ActivityIndicator color={colors.primary} size="large" /> : null}
        <AppText style={styles.feedbackTitle}>{title}</AppText>
        {description ? <AppText style={styles.muted}>{description}</AppText> : null}
        {action ? <AppButton label={action.label} onPress={action.onPress} /> : null}
      </View>
    </Screen>
  );
}

function money(value: number | null | undefined) {
  return value === null || value === undefined ? '—' : formatCurrency(value);
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat('pt-BR', {dateStyle: 'short'}).format(new Date(value));
}

const styles = StyleSheet.create({
  actions: {gap: spacing.sm, marginTop: spacing.lg},
  brand: {color: colors.primary, fontWeight: typography.weights.bold, marginTop: spacing.lg},
  category: {color: colors.textMuted, marginTop: spacing.sm},
  centered: {alignItems: 'center', flex: 1, gap: spacing.md, justifyContent: 'center'},
  content: {paddingBottom: spacing.xl},
  coupon: {color: colors.scoreText, fontSize: typography.sizes.label, marginTop: spacing.xs},
  emphasized: {color: colors.primary},
  fallbackText: {color: colors.textMuted, fontSize: 32, fontWeight: typography.weights.bold},
  feedbackTitle: {fontSize: 20, fontWeight: typography.weights.bold, textAlign: 'center'},
  helper: {color: colors.textMuted, fontSize: 12, marginTop: spacing.sm, textAlign: 'center'},
  image: {backgroundColor: colors.surface, borderRadius: 16, height: 280, width: '100%'},
  imageFallback: {alignItems: 'center', justifyContent: 'center'},
  list: {gap: spacing.sm},
  muted: {color: colors.textMuted},
  offerPrice: {color: colors.primary, fontWeight: typography.weights.bold},
  right: {alignItems: 'flex-end'},
  row: {alignItems: 'center', backgroundColor: colors.surface, borderColor: colors.border, borderRadius: 12, borderWidth: 1, flexDirection: 'row', justifyContent: 'space-between', padding: spacing.md},
  rowContent: {flex: 1, paddingRight: spacing.md},
  rowTitle: {fontWeight: typography.weights.bold},
  score: {color: colors.textMuted, fontSize: 12, marginTop: spacing.xs},
  section: {marginTop: spacing.xl},
  stat: {backgroundColor: colors.surface, borderColor: colors.border, borderRadius: 12, borderWidth: 1, padding: spacing.md, width: '48%'},
  stats: {flexDirection: 'row', flexWrap: 'wrap', gap: spacing.sm, marginTop: spacing.lg},
  statValue: {fontSize: 18, fontWeight: typography.weights.bold, marginTop: spacing.xs},
  title: {fontSize: 26, fontWeight: typography.weights.bold, marginTop: spacing.xs},
});
