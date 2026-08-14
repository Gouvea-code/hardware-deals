import {useEffect} from 'react'; import {registerDevice} from '../services/deviceService';
import {listenForNotificationOpen,listenForPushTokenRefresh,requestPushToken} from '../services/pushService'; import {useSessionStore} from '../store/sessionStore';
import {savePushToken} from '../services/pushTokenStorage';
import {trackEventSafely} from '../services/analyticsService';

export function PushRegistration(){const accessToken=useSessionStore(state=>state.accessToken);
 useEffect(()=>{if(!accessToken)return;let active=true;let unsubscribe:undefined|(()=>void);let unsubscribeOpen:undefined|(()=>void);
  requestPushToken().then(async token=>{if(!active||!token)return;await registerDevice(token);await savePushToken(token);}).catch(()=>undefined);
  try{unsubscribe=listenForPushTokenRefresh(async token=>{await registerDevice(token);await savePushToken(token);});
   unsubscribeOpen=listenForNotificationOpen(notificationId=>trackEventSafely('NOTIFICATION_OPEN',{notificationId}));}catch{}
  return()=>{active=false;unsubscribe?.();unsubscribeOpen?.();};},[accessToken]);return null;}
