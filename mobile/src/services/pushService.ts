import {AuthorizationStatus,getInitialNotification,getMessaging,getToken,onNotificationOpenedApp,onTokenRefresh,requestPermission} from '@react-native-firebase/messaging';
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
export function listenForNotificationOpen(listener:(notificationId:string)=>unknown){
 const messaging=getMessaging();
 getInitialNotification(messaging).then(message=>{const id=message?.data?.notificationId;if(typeof id==='string')listener(id);}).catch(()=>undefined);
 return onNotificationOpenedApp(messaging,message=>{const id=message.data?.notificationId;if(typeof id==='string')listener(id);});
}
