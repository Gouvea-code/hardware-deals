import {useState} from 'react'; import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {StyleSheet,TextInput,View} from 'react-native'; import {AppButton,AppText,Screen} from '../components';
import {useAlerts} from '../hooks/useAlerts'; import {RootStackParamList} from '../navigation/types'; import {colors,spacing,typography} from '../theme';
import {formatCurrency} from '../utils/formatCurrency';
import {trackEventSafely} from '../services/analyticsService';
type Props=NativeStackScreenProps<RootStackParamList,'AlertForm'>;
export function AlertFormScreen({navigation,route}:Props){
 const {productId,productName,currentPrice}=route.params; const [value,setValue]=useState(currentPrice?.toFixed(2)??'');
 const [error,setError]=useState<string|null>(null); const alerts=useAlerts();
 const submit=async()=>{const price=Number(value.replace(',','.')); if(!Number.isFinite(price)||price<=0){setError('Informe um preço maior que zero.');return;}
  setError(null); try{await alerts.save({productId,targetPrice:price});await trackEventSafely('ALERT_CREATED',{productId});navigation.goBack();}catch{setError('Não foi possível salvar o alerta.');}};
 return <Screen><View style={styles.content}><AppText style={styles.title}>{productName}</AppText>
  {currentPrice?<AppText style={styles.muted}>Preço atual: {formatCurrency(currentPrice)}</AppText>:null}
  <AppText style={styles.label}>Avise quando o preço for menor ou igual a</AppText>
  <TextInput accessibilityLabel="Preço desejado" keyboardType="decimal-pad" onChangeText={setValue}
   placeholder="3700,00" placeholderTextColor={colors.textMuted} style={styles.input} value={value}/>
  {error?<AppText style={styles.error}>{error}</AppText>:null}
  <AppButton disabled={alerts.isUpdating} label="Salvar alerta" onPress={submit}/></View></Screen>;
}
const styles=StyleSheet.create({content:{gap:spacing.md},error:{color:colors.danger},input:{backgroundColor:colors.surface,borderColor:colors.border,borderRadius:12,borderWidth:1,color:colors.text,fontSize:20,padding:spacing.md},label:{fontWeight:typography.weights.bold,marginTop:spacing.lg},muted:{color:colors.textMuted},title:{fontSize:24,fontWeight:typography.weights.bold}});
