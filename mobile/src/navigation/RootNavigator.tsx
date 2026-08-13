import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

import {HomeScreen} from '../screens/HomeScreen';
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
        <Stack.Screen
          component={HomeScreen}
          name="Home"
          options={{title: 'Hardware Deals'}}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
