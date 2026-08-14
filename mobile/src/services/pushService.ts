import {AuthorizationStatus,getMessaging,getToken,onTokenRefresh,requestPermission} from '@react-native-firebase/messaging';
import {PermissionsAndroid,Platform} from 'react-native';

export async function requestPushToken(){
 if(Platform.OS==='android'&&Number(Platform.Version)>=33){
  const granted=await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS);
  if(granted!==PermissionsAndroid.RESULTS.GRANTED)return null;
 }
 const messaging=getMessaging(); const status=await requestPermission(messaging);
 if(status!==AuthorizationStatus.AUTHORIZED&&status!==AuthorizationStatus.PROVISIONAL)return null;
 return getToken(messaging);
}
export function listenForPushTokenRefresh(listener:(token:string)=>unknown){return onTokenRefresh(getMessaging(),listener);}
