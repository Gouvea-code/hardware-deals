import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';

import {addFavorite, getFavorites, removeFavorite} from '../services/favoriteService';
import {useSessionStore} from '../store/sessionStore';
import {trackEventSafely} from '../services/analyticsService';

export function useFavorites() {
  const accessToken = useSessionStore(state => state.accessToken);
  const queryClient = useQueryClient();
  const query = useQuery({enabled: Boolean(accessToken), queryFn: getFavorites, queryKey: ['favorites']});
  const invalidate = () => queryClient.invalidateQueries({queryKey: ['favorites']});
  const add = useMutation({mutationFn: addFavorite, onSuccess:(_,productId)=>{invalidate();trackEventSafely('FAVORITE',{productId});}});
  const remove = useMutation({mutationFn: removeFavorite, onSuccess: invalidate});
  return {...query, accessToken, add: add.mutateAsync, favorites: query.data ?? [],
    isUpdating: add.isPending || remove.isPending, remove: remove.mutateAsync};
}
