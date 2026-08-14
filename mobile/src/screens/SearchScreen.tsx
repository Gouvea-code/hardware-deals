import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {
  ActivityIndicator,
  FlatList,
  StyleSheet,
  TextInput,
  View,
} from 'react-native';

import {AppButton, AppText, ProductCard, Screen} from '../components';
import {MIN_SEARCH_LENGTH, useProductSearch} from '../hooks/useProductSearch';
import {RootStackParamList} from '../navigation/types';
import {colors, spacing, typography} from '../theme';
import {useEffect, useRef, useState} from 'react';
import {trackEventSafely} from '../services/analyticsService';

type SearchScreenProps = NativeStackScreenProps<RootStackParamList, 'Search'>;

export function SearchScreen({navigation, route}: SearchScreenProps) {
  const [query, setQuery] = useState(route.params?.initialQuery ?? '');
  const lastTrackedQuery = useRef('');
  const {
    canSearch,
    debouncedQuery,
    error,
    isDebouncing,
    isFetching,
    products,
    refetch,
  } = useProductSearch(query);

  const hasMinimumInput = query.trim().length >= MIN_SEARCH_LENGTH;
  const isLoading = hasMinimumInput && (isDebouncing || isFetching);
  const showEmpty =
    hasMinimumInput && canSearch && !isLoading && !error && products.length === 0;

  useEffect(()=>{if(canSearch&&!isFetching&&!error&&debouncedQuery!==lastTrackedQuery.current){
    lastTrackedQuery.current=debouncedQuery;trackEventSafely('SEARCH');
  }},[canSearch,debouncedQuery,error,isFetching]);

  return (
    <Screen>
      <TextInput
        accessibilityLabel="Campo de pesquisa"
        autoCapitalize="none"
        autoCorrect={false}
        autoFocus
        clearButtonMode="while-editing"
        onChangeText={setQuery}
        placeholder="Produto, marca ou modelo"
        placeholderTextColor={colors.textMuted}
        returnKeyType="search"
        style={styles.input}
        value={query}
      />

      {!hasMinimumInput ? (
        <Feedback
          description="Digite pelo menos dois caracteres para começar."
          title="Encontre seu próximo hardware"
        />
      ) : null}

      {isLoading ? (
        <View accessibilityLabel="Carregando resultados" style={styles.loading}>
          <ActivityIndicator color={colors.primary} size="large" />
          <AppText style={styles.muted}>
            {isDebouncing ? 'Preparando busca...' : 'Buscando produtos...'}
          </AppText>
        </View>
      ) : null}

      {hasMinimumInput && error && !isLoading ? (
        <View style={styles.loading}>
          <AppText style={styles.feedbackTitle}>Não foi possível buscar</AppText>
          <AppText style={styles.muted}>Verifique sua conexão e tente novamente.</AppText>
          <AppButton label="Tentar novamente" onPress={refetch} />
        </View>
      ) : null}

      {showEmpty ? (
        <Feedback
          description="Tente outro nome, marca ou modelo."
          title={`Nenhum resultado para “${debouncedQuery}”`}
        />
      ) : null}

      {hasMinimumInput && !isLoading && !error && products.length > 0 ? (
        <FlatList
          contentContainerStyle={styles.results}
          data={products}
          ItemSeparatorComponent={Separator}
          keyboardShouldPersistTaps="handled"
          keyExtractor={product => product.id}
          renderItem={({item}) => (
            <ProductCard
              onPress={() =>
                navigation.navigate('ProductDetails', {productId: item.id})
              }
              product={item}
            />
          )}
          showsVerticalScrollIndicator={false}
        />
      ) : null}
    </Screen>
  );
}

function Feedback({description, title}: {description: string; title: string}) {
  return (
    <View style={styles.feedback}>
      <AppText style={styles.feedbackTitle}>{title}</AppText>
      <AppText style={styles.muted}>{description}</AppText>
    </View>
  );
}

function Separator() {
  return <View style={styles.separator} />;
}

const styles = StyleSheet.create({
  feedback: {
    alignItems: 'center',
    flex: 1,
    gap: spacing.sm,
    justifyContent: 'center',
    paddingHorizontal: spacing.lg,
  },
  feedbackTitle: {
    fontSize: 20,
    fontWeight: typography.weights.bold,
    textAlign: 'center',
  },
  input: {
    backgroundColor: colors.surface,
    borderColor: colors.border,
    borderRadius: 12,
    borderWidth: 1,
    color: colors.text,
    fontSize: typography.sizes.body,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.sm,
  },
  loading: {
    alignItems: 'center',
    flex: 1,
    gap: spacing.md,
    justifyContent: 'center',
  },
  muted: {
    color: colors.textMuted,
    textAlign: 'center',
  },
  results: {
    paddingBottom: spacing.xl,
    paddingTop: spacing.md,
  },
  separator: {
    height: spacing.md,
  },
});
