import * as Keychain from 'react-native-keychain';const SERVICE='com.hardwaredeals.push-token';
export async function savePushToken(token:string){await Keychain.setGenericPassword('fcm',token,{service:SERVICE});}
export async function loadPushToken(){const value=await Keychain.getGenericPassword({service:SERVICE});return value?value.password:null;}
export async function clearPushToken(){await Keychain.resetGenericPassword({service:SERVICE});}
