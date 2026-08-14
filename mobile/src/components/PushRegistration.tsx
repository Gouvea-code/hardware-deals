import {useEffect} from 'react'; import {registerDevice} from '../services/deviceService';
import {listenForPushTokenRefresh,requestPushToken} from '../services/pushService'; import {useSessionStore} from '../store/sessionStore';

export function PushRegistration(){const accessToken=useSessionStore(state=>state.accessToken);
 useEffect(()=>{if(!accessToken)return;let active=true;let unsubscribe:undefined|(()=>void);
  requestPushToken().then(token=>{if(!active||!token)return;return registerDevice(token);}).catch(()=>undefined);
  try{unsubscribe=listenForPushTokenRefresh(token=>registerDevice(token).catch(()=>undefined));}catch{}
  return()=>{active=false;unsubscribe?.();};},[accessToken]);return null;}
