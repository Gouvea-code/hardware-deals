import {ActivityIndicator,StyleSheet,View} from 'react-native'; import {AppButton,AppText,Screen} from '../components';
import {useAlerts} from '../hooks/useAlerts'; import {colors,spacing,typography} from '../theme'; import {formatCurrency} from '../utils/formatCurrency';
export function AlertsScreen(){const {accessToken,alerts,error,isLoading,isUpdating,remove}=useAlerts();
 if(!accessToken)return <State text="Entre na sua conta para gerenciar alertas."/>; if(isLoading)return <State loading text="Carregando alertas..."/>;
 if(error)return <State text="Não foi possível carregar seus alertas."/>; if(!alerts.length)return <State text="Você ainda não criou alertas."/>;
 return <Screen><View style={styles.list}>{alerts.map(alert=><View key={alert.id} style={styles.card}>
  <View style={styles.info}><AppText style={styles.name}>{alert.productName}</AppText><AppText style={styles.target}>≤ {formatCurrency(alert.targetPrice)}</AppText></View>
  <AppButton disabled={isUpdating} label="Remover" onPress={()=>remove(alert.productId)}/></View>)}</View></Screen>;
}
function State({loading,text}:{loading?:boolean;text:string}){return <Screen><View style={styles.center}>{loading?<ActivityIndicator color={colors.primary}/>:null}<AppText style={styles.name}>{text}</AppText></View></Screen>}
const styles=StyleSheet.create({card:{backgroundColor:colors.surface,borderColor:colors.border,borderRadius:12,borderWidth:1,gap:spacing.md,padding:spacing.md},center:{alignItems:'center',flex:1,gap:spacing.md,justifyContent:'center'},info:{flex:1},list:{gap:spacing.md},name:{fontWeight:typography.weights.bold,textAlign:'center'},target:{color:colors.primary,fontSize:20,fontWeight:typography.weights.bold,marginTop:spacing.sm}});
