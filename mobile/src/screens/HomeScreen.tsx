import {
  ActivityIndicator,
  RefreshControl,
  ScrollView,
  StyleSheet,
  View,
} from 'react-native';

import {
  AppButton,
  AppText,
  CategoryChip,
  DealCard,
  Screen,
  SectionHeader,
} from '../components';
import {useHomeData} from '../hooks/useHomeData';
import {colors, spacing, typography} from '../theme';

export function HomeScreen() {
  const {
    bestDeals,
    categories,
    error,
    isLoading,
    isRefreshing,
    recentDeals,
    refresh,
  } = useHomeData();

  if (isLoading) {
    return (
      <Screen>
        <View style={styles.centered}>
          <ActivityIndicator color={colors.primary} size="large" />
          <AppText style={styles.feedback}>Carregando ofertas...</AppText>
        </View>
      </Screen>
    );
  }

  if (error && bestDeals.length === 0 && recentDeals.length === 0) {
    return (
      <Screen>
        <View style={styles.centered}>
          <AppText style={styles.title}>Não foi possível carregar a Home</AppText>
          <AppText style={styles.feedback}>Verifique sua conexão e tente novamente.</AppText>
          <AppButton label="Tentar novamente" onPress={refresh} />
        </View>
      </Screen>
    );
  }

  return (
    <Screen>
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={
          <RefreshControl
            colors={[colors.primary]}
            onRefresh={refresh}
            refreshing={isRefreshing}
            tintColor={colors.primary}
          />
        }
        showsVerticalScrollIndicator={false}>
        <View>
          <AppText style={styles.eyebrow}>ECONOMIZE NO SEU SETUP</AppText>
          <AppText style={styles.heroTitle}>As melhores ofertas de hardware</AppText>
        </View>

        <View style={styles.section}>
          <SectionHeader title="Melhores ofertas" />
          {bestDeals.length > 0 ? (
            <ScrollView
              contentContainerStyle={styles.horizontalList}
              horizontal
              showsHorizontalScrollIndicator={false}>
              {bestDeals.slice(0, 6).map(deal => (
                <DealCard deal={deal} key={deal.id} />
              ))}
            </ScrollView>
          ) : (
            <AppText style={styles.feedback}>Nenhuma oferta disponível agora.</AppText>
          )}
        </View>

        <View style={styles.section}>
          <SectionHeader title="Categorias" />
          {categories.length > 0 ? (
            <View style={styles.categories}>
              {categories.map(category => (
                <CategoryChip key={category} name={category} />
              ))}
            </View>
          ) : (
            <AppText style={styles.feedback}>Nenhuma categoria encontrada.</AppText>
          )}
        </View>

        <View style={styles.section}>
          <SectionHeader title="Ofertas recentes" />
          <View style={styles.recentList}>
            {recentDeals.length > 0 ? (
              recentDeals.slice(0, 10).map(deal => (
                <DealCard compact deal={deal} key={deal.id} />
              ))
            ) : (
              <AppText style={styles.feedback}>Nenhuma oferta recente.</AppText>
            )}
          </View>
        </View>
      </ScrollView>
    </Screen>
  );
}

const styles = StyleSheet.create({
  categories: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: spacing.sm,
  },
  centered: {
    alignItems: 'center',
    flex: 1,
    gap: spacing.md,
    justifyContent: 'center',
  },
  content: {
    paddingBottom: spacing.xl,
  },
  eyebrow: {
    color: colors.primary,
    fontSize: 12,
    fontWeight: typography.weights.bold,
    letterSpacing: 1,
  },
  feedback: {
    color: colors.textMuted,
    textAlign: 'center',
  },
  heroTitle: {
    fontSize: 28,
    fontWeight: typography.weights.bold,
    marginTop: spacing.sm,
  },
  horizontalList: {
    gap: spacing.md,
    paddingRight: spacing.md,
  },
  recentList: {
    gap: spacing.md,
  },
  section: {
    marginTop: spacing.xl,
  },
  title: {
    fontSize: 20,
    fontWeight: typography.weights.bold,
    textAlign: 'center',
  },
});
