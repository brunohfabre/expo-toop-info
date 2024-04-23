import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoToopInfo.web.ts
// and on native platforms to ExpoToopInfo.ts
import ExpoToopInfoModule from './ExpoToopInfoModule';
import ExpoToopInfoView from './ExpoToopInfoView';
import { ChangeEventPayload, ExpoToopInfoViewProps } from './ExpoToopInfo.types';

// Get the native constant value.
export const PI = ExpoToopInfoModule.PI;

export function hello(): string {
  return ExpoToopInfoModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoToopInfoModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoToopInfoModule ?? NativeModulesProxy.ExpoToopInfo);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoToopInfoView, ExpoToopInfoViewProps, ChangeEventPayload };
