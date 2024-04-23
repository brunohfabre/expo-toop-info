import { StyleSheet, Text, View } from 'react-native';

import * as ExpoToopInfo from 'expo-toop-info';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoToopInfo.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
