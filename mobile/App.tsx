import {QueryClientProvider} from '@tanstack/react-query';
import {StatusBar} from 'react-native';
import {SafeAreaProvider} from 'react-native-safe-area-context';

import {RootNavigator} from './src/navigation/RootNavigator';
import {PushRegistration} from './src/components/PushRegistration';
import {queryClient} from './src/services/queryClient';

function App() {
  return (
    <SafeAreaProvider>
      <QueryClientProvider client={queryClient}>
        <PushRegistration />
        <StatusBar barStyle="dark-content" />
        <RootNavigator />
      </QueryClientProvider>
    </SafeAreaProvider>
  );
}

export default App;
