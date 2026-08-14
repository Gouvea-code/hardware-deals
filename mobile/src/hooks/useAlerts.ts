import {useMutation,useQuery,useQueryClient} from '@tanstack/react-query';
import {getAlerts,removeAlert,saveAlert} from '../services/alertService'; import {useSessionStore} from '../store/sessionStore';
export function useAlerts(){
 const accessToken=useSessionStore(s=>s.accessToken); const client=useQueryClient();
 const query=useQuery({enabled:Boolean(accessToken),queryFn:getAlerts,queryKey:['alerts']});
 const invalidate=()=>client.invalidateQueries({queryKey:['alerts']});
 const save=useMutation({mutationFn:({productId,targetPrice}:{productId:string;targetPrice:number})=>saveAlert(productId,targetPrice),onSuccess:invalidate});
 const remove=useMutation({mutationFn:removeAlert,onSuccess:invalidate});
 return {...query,accessToken,alerts:query.data??[],isUpdating:save.isPending||remove.isPending,
  remove:remove.mutateAsync,save:save.mutateAsync};
}
