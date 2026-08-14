import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {ActivityIndicator, FlatList, StyleSheet, View} from 'react-native';

import {AppButton, AppText, ProductCard, Screen} from '../components';
import {useFavorites} from '../hooks/useFavorites';
import {RootStackParamList} from '../navigation/types';
import {colors, spacing, typography} from '../theme';

type Props = NativeStackScreenProps<RootStackParamList, 'Favorites'>;

export function FavoritesScreen({navigation}: Props) {
  const {accessToken, error, favorites, isLoading, refetch} = useFavorites();
  if (!accessToken) return <Message title="Entre na sua conta para sincronizar favoritos." />;
  if (isLoading) return <Message loading title="Carregando favoritos..." />;
  if (error) return <Message action={refetch} title="Não foi possível carregar seus favoritos." />;
  if (!favorites.length) return <Message title="Você ainda não adicionou favoritos." />;
  return (
    <Screen><FlatList contentContainerStyle={styles.list} data={favorites}
      ItemSeparatorComponent={Separator} keyExtractor={item => item.id}
      renderItem={({item}) => <ProductCard
        onPress={() => navigation.navigate('ProductDetails', {productId: item.productId})}
        product={{brand: item.brand, category: item.category, ean: null, id: item.productId,
          imageUrl: item.imageUrl, model: '', name: item.productName}} />} /></Screen>
  );
}

function Separator() { return <View style={styles.separator} />; }

function Message({action, loading, title}: {action?: () => unknown; loading?: boolean; title: string}) {
  return <Screen><View style={styles.centered}>
    {loading ? <ActivityIndicator color={colors.primary} size="large" /> : null}
    <AppText style={styles.title}>{title}</AppText>
    {action ? <AppButton label="Tentar novamente" onPress={action} /> : null}
  </View></Screen>;
}

const styles = StyleSheet.create({
  centered: {alignItems: 'center', flex: 1, gap: spacing.md, justifyContent: 'center'},
  list: {paddingBottom: spacing.xl}, separator: {height: spacing.md},
  title: {fontSize: 20, fontWeight: typography.weights.bold, textAlign: 'center'},
});
