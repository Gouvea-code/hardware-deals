import {apiClient} from '../src/services/apiClient';
import {trackEvent,trackEventSafely} from '../src/services/analyticsService';

jest.mock('../src/services/apiClient',()=>({apiClient:{post:jest.fn()}}));

test('sends only the event and its typed context',async()=>{
 (apiClient.post as jest.Mock).mockResolvedValue({data:{}});
 await trackEvent('PRODUCT_VIEW',{productId:'product-1'});
 expect(apiClient.post).toHaveBeenCalledWith('/analytics/events',{eventType:'PRODUCT_VIEW',productId:'product-1'});
});

test('safe tracking never interrupts the user flow',async()=>{
 (apiClient.post as jest.Mock).mockRejectedValue(new Error('offline'));
 await expect(trackEventSafely('SEARCH')).resolves.toBeUndefined();
});
