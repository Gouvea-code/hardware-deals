import {
  ActivityIndicator,
  Pressable,
  RefreshControl,
  ScrollView,
  StyleSheet,
  View,
} from 'react-native';
import {NativeStackScreenProps} from '@react-navigation/native-stack';

import {
  AppButton,
  AppText,
  CategoryChip,
  DealCard,
  Screen,
  SectionHeader,
} from '../components';
import {useHomeData} from '../hooks/useHomeData';
import {RootStackParamList} from '../navigation/types';
import {colors, spacing, typography} from '../theme';

type HomeScreenProps = NativeStackScreenProps<RootStackParamList, 'Home'>;

export function HomeScreen({navigation}: HomeScreenProps) {
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

        <Pressable
          accessibilityRole="button"
          onPress={() => navigation.navigate('Search')}
          style={({pressed}) => [styles.search, pressed && styles.searchPressed]}>
          <AppText style={styles.searchPlaceholder}>Buscar produtos...</AppText>
          <AppText style={styles.searchAction}>Buscar</AppText>
        </Pressable>

        <Pressable
          accessibilityRole="button"
          onPress={() => navigation.navigate('Favorites')}
          style={({pressed}) => [styles.favoritesLink, pressed && styles.searchPressed]}>
          <AppText style={styles.searchAction}>Ver favoritos</AppText>
        </Pressable>
        <Pressable accessibilityRole="button" onPress={() => navigation.navigate('Alerts')}
          style={({pressed}) => [styles.favoritesLink, pressed && styles.searchPressed]}>
          <AppText style={styles.searchAction}>Ver alertas</AppText>
        </Pressable>
        <Pressable accessibilityRole="button" onPress={() => navigation.navigate('Account')}
          style={({pressed}) => [styles.favoritesLink, pressed && styles.searchPressed]}>
          <AppText style={styles.searchAction}>Minha conta</AppText>
        </Pressable>

        <View style={styles.section}>
          <SectionHeader title="Melhores ofertas" />
          {bestDeals.length > 0 ? (
            <ScrollView
              contentContainerStyle={styles.horizontalList}
              horizontal
              showsHorizontalScrollIndicator={false}>
              {bestDeals.slice(0, 6).map(deal => (
                <DealCard
                  deal={deal}
                  key={deal.id}
                  onPress={() =>
                    navigation.navigate('ProductDetails', {
                      productId: deal.productId,
                    })
                  }
                />
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
                <DealCard
                  compact
                  deal={deal}
                  key={deal.id}
                  onPress={() =>
                    navigation.navigate('ProductDetails', {
                      productId: deal.productId,
                    })
                  }
                />
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
  favoritesLink: {
    alignSelf: 'flex-end',
    marginTop: spacing.md,
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
  search: {
    alignItems: 'center',
    backgroundColor: colors.surface,
    borderColor: colors.border,
    borderRadius: 12,
    borderWidth: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: spacing.lg,
    padding: spacing.md,
  },
  searchAction: {
    color: colors.primary,
    fontWeight: typography.weights.bold,
  },
  searchPlaceholder: {
    color: colors.textMuted,
  },
  searchPressed: {
    opacity: 0.7,
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
