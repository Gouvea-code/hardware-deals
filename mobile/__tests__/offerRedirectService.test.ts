import {apiClient} from '../src/services/apiClient';
import {registerOfferClick} from '../src/services/offerRedirectService';

jest.mock('../src/services/apiClient',()=>({apiClient:{post:jest.fn()}}));

test('registers the click before returning the trusted URL',async()=>{
  const response={clickId:'click-1',redirectUrl:'https://shop.example/item',clickedAt:'2026-08-13T12:00:00'};
  (apiClient.post as jest.Mock).mockResolvedValue({data:response});
  await expect(registerOfferClick('offer-1')).resolves.toEqual(response);
  expect(apiClient.post).toHaveBeenCalledWith('/offers/offer-1/click');
});
