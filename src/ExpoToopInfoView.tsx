import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoToopInfoViewProps } from './ExpoToopInfo.types';

const NativeView: React.ComponentType<ExpoToopInfoViewProps> =
  requireNativeViewManager('ExpoToopInfo');

export default function ExpoToopInfoView(props: ExpoToopInfoViewProps) {
  return <NativeView {...props} />;
}
