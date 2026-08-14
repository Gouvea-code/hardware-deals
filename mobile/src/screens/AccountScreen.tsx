import {useState} from 'react';
import {NativeStackScreenProps} from '@react-navigation/native-stack';
import {Alert,StyleSheet,TextInput,View} from 'react-native';
import {AppButton,AppText,Screen} from '../components';
import {RootStackParamList} from '../navigation/types';
import {deleteAccount,logout} from '../services/authService';
import {deactivateDevice} from '../services/deviceService';
import {clearPushToken,loadPushToken} from '../services/pushTokenStorage';
import {useSessionStore} from '../store/sessionStore';
import {colors,spacing,typography} from '../theme';
import {apiErrorMessage} from '../utils/apiErrorMessage';

type Props=NativeStackScreenProps<RootStackParamList,'Account'>;

export function AccountScreen({navigation}:Props){
 const accessToken=useSessionStore(s=>s.accessToken);const refreshToken=useSessionStore(s=>s.refreshToken);const clear=useSessionStore(s=>s.clearSession);
 const[loading,setLoading]=useState(false);const[password,setPassword]=useState('');const[error,setError]=useState<string|null>(null);
 if(!accessToken)return <Screen><View style={styles.center}><AppText style={styles.title}>Sua conta</AppText><AppText>Entre para sincronizar favoritos, alertas e notificações.</AppText><AppButton label="Entrar" onPress={()=>navigation.navigate('Login')}/></View></Screen>;
 const clearLocalSession=async()=>{await clearPushToken();await clear();navigation.popToTop();};
 const signOut=async()=>{setLoading(true);try{const pushToken=await loadPushToken();if(pushToken)await deactivateDevice(pushToken);if(refreshToken)await logout(refreshToken);}finally{await clearLocalSession();setLoading(false);}};
 const remove=()=>Alert.alert('Excluir conta','Esta ação elimina permanentemente seus dados pessoais.',[
  {text:'Cancelar',style:'cancel'},
  {text:'Excluir',style:'destructive',onPress:async()=>{setLoading(true);setError(null);try{await deleteAccount(password);await clearLocalSession();}catch(e){setError(apiErrorMessage(e,'Não foi possível excluir a conta.'));}finally{setLoading(false);}}},
 ]);
 return <Screen><View style={styles.center}><AppText style={styles.title}>Conta conectada</AppText><AppText>Favoritos, alertas e notificações estão sincronizados.</AppText><AppButton disabled={loading} label={loading?'Saindo...':'Sair'} onPress={signOut}/><View style={styles.danger}><AppText style={styles.dangerTitle}>Excluir conta</AppText><AppText>Confirme sua senha para apagar conta, tokens, favoritos, alertas e notificações.</AppText><TextInput accessibilityLabel="Senha para excluir conta" autoCapitalize="none" onChangeText={setPassword} placeholder="Senha" placeholderTextColor={colors.textMuted} secureTextEntry style={styles.input} value={password}/>{error?<AppText style={styles.error}>{error}</AppText>:null}<AppButton disabled={loading||!password} label="Excluir permanentemente" onPress={remove}/></View></View></Screen>;
}

const styles=StyleSheet.create({center:{alignItems:'stretch',flex:1,gap:spacing.md,justifyContent:'center'},danger:{borderColor:colors.danger,borderRadius:12,borderWidth:1,gap:spacing.sm,marginTop:spacing.lg,padding:spacing.md},dangerTitle:{color:colors.danger,fontWeight:typography.weights.bold},error:{color:colors.danger},input:{backgroundColor:colors.surface,borderColor:colors.border,borderRadius:12,borderWidth:1,color:colors.text,padding:spacing.md},title:{fontSize:24,fontWeight:typography.weights.bold,textAlign:'center'}});
