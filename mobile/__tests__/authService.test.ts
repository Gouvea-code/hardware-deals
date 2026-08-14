import {apiClient} from '../src/services/apiClient';
import {forgotPassword,login,logout,register,resetPassword,verifyEmail} from '../src/services/authService';

jest.mock('../src/services/apiClient',()=>({apiClient:{post:jest.fn()}}));
beforeEach(()=>{(apiClient.post as jest.Mock).mockResolvedValue({data:{ok:true}});});

test('uses every authentication contract',async()=>{
 await login('a@example.com','password');await register('A','a@example.com','password');await forgotPassword('a@example.com');
 await verifyEmail('verify');await resetPassword('reset','newPassword');await logout('refresh');
 expect(apiClient.post).toHaveBeenNthCalledWith(1,'/auth/login',{email:'a@example.com',password:'password'});
 expect(apiClient.post).toHaveBeenNthCalledWith(2,'/auth/register',{email:'a@example.com',name:'A',password:'password'});
 expect(apiClient.post).toHaveBeenNthCalledWith(3,'/auth/forgot-password',{email:'a@example.com'});
 expect(apiClient.post).toHaveBeenNthCalledWith(4,'/auth/verify-email',{token:'verify'});
 expect(apiClient.post).toHaveBeenNthCalledWith(5,'/auth/reset-password',{newPassword:'newPassword',token:'reset'});
 expect(apiClient.post).toHaveBeenNthCalledWith(6,'/auth/logout',{refreshToken:'refresh'});
});
