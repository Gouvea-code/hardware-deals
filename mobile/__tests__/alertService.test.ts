import {apiClient} from '../src/services/apiClient'; import {getAlerts,removeAlert,saveAlert} from '../src/services/alertService';
jest.mock('../src/services/apiClient',()=>({apiClient:{delete:jest.fn(),get:jest.fn(),put:jest.fn()}}));
test('uses alert endpoints',async()=>{(apiClient.get as jest.Mock).mockResolvedValue({data:[]});(apiClient.put as jest.Mock).mockResolvedValue({data:{}});(apiClient.delete as jest.Mock).mockResolvedValue({});
 await getAlerts();await saveAlert('p1',3700);await removeAlert('p1');expect(apiClient.put).toHaveBeenCalledWith('/alerts/p1',{targetPrice:3700});});
