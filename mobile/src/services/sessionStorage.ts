import * as Keychain from 'react-native-keychain'; import {StoredSession} from '../types/auth';
const SERVICE='com.hardwaredeals.session';
export async function saveSession(session:StoredSession){await Keychain.setGenericPassword('session',JSON.stringify(session),{service:SERVICE});}
export async function loadSession():Promise<StoredSession|null>{const value=await Keychain.getGenericPassword({service:SERVICE});
 if(!value)return null;try{return JSON.parse(value.password) as StoredSession;}catch{await clearStoredSession();return null;}}
export async function clearStoredSession(){await Keychain.resetGenericPassword({service:SERVICE});}
