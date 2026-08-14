import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

import {HomeScreen} from '../screens/HomeScreen';
import {FavoritesScreen} from '../screens/FavoritesScreen';
import {AlertFormScreen} from '../screens/AlertFormScreen';
import {AlertsScreen} from '../screens/AlertsScreen';
import {AccountScreen} from '../screens/AccountScreen';
import {ForgotPasswordScreen} from '../screens/ForgotPasswordScreen';
import {LoginScreen} from '../screens/LoginScreen';
import {RegisterScreen} from '../screens/RegisterScreen';
import {ResetPasswordScreen} from '../screens/ResetPasswordScreen';
import {VerifyEmailScreen} from '../screens/VerifyEmailScreen';
import {ProductDetailsScreen} from '../screens/ProductDetailsScreen';
import {SearchScreen} from '../screens/SearchScreen';
import {colors} from '../theme';
import {RootStackParamList} from './types';

const Stack = createNativeStackNavigator<RootStackParamList>();

export function RootNavigator() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        screenOptions={{
          contentStyle: {backgroundColor: colors.background},
          headerShadowVisible: false,
          headerStyle: {backgroundColor: colors.surface},
          headerTintColor: colors.text,
        }}>
        <Stack.Screen component={AccountScreen} name="Account" options={{title: 'Conta'}} />
        <Stack.Screen component={AlertFormScreen} name="AlertForm" options={{title: 'Criar alerta'}} />
        <Stack.Screen component={AlertsScreen} name="Alerts" options={{title: 'Meus alertas'}} />
        <Stack.Screen
          component={FavoritesScreen}
          name="Favorites"
          options={{title: 'Favoritos'}}
        />
        <Stack.Screen component={LoginScreen} name="Login" options={{title: 'Entrar'}} />
        <Stack.Screen
          component={ProductDetailsScreen}
          name="ProductDetails"
          options={{title: 'Detalhes do produto'}}
        />
        <Stack.Screen component={RegisterScreen} name="Register" options={{title: 'Criar conta'}} />
        <Stack.Screen component={ForgotPasswordScreen} name="ForgotPassword" options={{title: 'Recuperar senha'}} />
        <Stack.Screen component={ResetPasswordScreen} name="ResetPassword" options={{title: 'Redefinir senha'}} />
        <Stack.Screen component={VerifyEmailScreen} name="VerifyEmail" options={{title: 'Verificar e-mail'}} />
        <Stack.Screen
          component={HomeScreen}
          name="Home"
          options={{title: 'Hardware Deals'}}
        />
        <Stack.Screen
          component={SearchScreen}
          name="Search"
          options={{title: 'Buscar produtos'}}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
