import {PropsWithChildren,useEffect} from 'react';import {ActivityIndicator,StyleSheet,View} from 'react-native';
import {useSessionStore} from '../store/sessionStore';import {colors} from '../theme';
export function SessionBootstrap({children}:PropsWithChildren){const hydrated=useSessionStore(s=>s.hydrated);const hydrate=useSessionStore(s=>s.hydrate);
 useEffect(()=>{hydrate().catch(()=>undefined);},[hydrate]);if(!hydrated)return <View style={styles.root}><ActivityIndicator color={colors.primary} size="large"/></View>;return children;}
const styles=StyleSheet.create({root:{alignItems:'center',backgroundColor:colors.background,flex:1,justifyContent:'center'}});
