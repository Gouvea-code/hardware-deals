import {Platform} from 'react-native';import {apiClient} from '../src/services/apiClient';import {deactivateDevice,registerDevice} from '../src/services/deviceService';
jest.mock('../src/services/apiClient',()=>({apiClient:{delete:jest.fn(),put:jest.fn()}}));
test('registers and deactivates the FCM token',async()=>{(apiClient.put as jest.Mock).mockResolvedValue({data:{}});(apiClient.delete as jest.Mock).mockResolvedValue({});
 await registerDevice('token');await deactivateDevice('token');expect(apiClient.put).toHaveBeenCalledWith('/devices',{platform:Platform.OS,token:'token'});
 expect(apiClient.delete).toHaveBeenCalledWith('/devices',{params:{token:'token'}});});
