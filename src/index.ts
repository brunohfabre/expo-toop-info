import type { Subscription } from "expo-modules-core";
import { uniqBy } from 'lodash'

import ExpoToopInfoModule from "./ExpoToopInfoModule";

export type Application = {
  name: string;
  packageName: string;
  versionName: string;
  versionCode: number;
  firstInstallTime: string;
  lastUpdateTime: string;
  icon?: string;
}

function returnResponse(result: any): any {
  if (result === 'not-permitted') {
    throw new Error('toop-info-not-permitted-error')
  }

  return result
}

export function openedByDpc(): string {
  return returnResponse(ExpoToopInfoModule.openedByDpc());
}

export function getImei(): string {
  return returnResponse(ExpoToopInfoModule.getImei());
}

export function getSerialNumber(): string {
  return returnResponse(ExpoToopInfoModule.getSerialNumber());
}

export interface GetInstalledPackagesData {
  supressPackages?: string[]
  getIcon?: boolean
}

export function getInstalledPackages(data?: GetInstalledPackagesData): Application[] {
  const items = returnResponse(ExpoToopInfoModule.getInstalledPackages(!!data?.getIcon));

  const applications: Application[] = items.map((item: string) => {
    const [
      name,
      packageName,
      versionName,
      versionCode,
      firstInstallTime,
      lastUpdateTime,
      icon,
    ] = item.split(';')

    return {
      name,
      packageName,
      versionCode: Number(versionCode),
      versionName,
      firstInstallTime,
      lastUpdateTime,
      icon: icon ? `data:image/png;base64,${icon}` : ''
    }
  })
  
  if(!data?.supressPackages) {
    return uniqBy(applications, 'packageName') 
  }

  return uniqBy(applications, 'packageName').filter((application: Application) => !data.supressPackages?.some(packageName => packageName === application.packageName))
}

export function sendActivityResultOk(): string {
  return returnResponse(ExpoToopInfoModule.sendActivityResultOk());
}

export function launchApplication(packageName: string): string {
  return returnResponse(ExpoToopInfoModule.launchApplication(packageName));
}

export function getEnrollmentSpecificId(): string {
  return returnResponse(ExpoToopInfoModule.getEnrollmentSpecificId());
}

export function getDeviceId(): string {
  return returnResponse(ExpoToopInfoModule.getDeviceId());
}

export function setWallpaper(uri: string): string {
  return returnResponse(ExpoToopInfoModule.setWallpaper(uri));
}

export function addPackageChangeListener(
  listener: (event: any) => void
): Subscription {
  return ExpoToopInfoModule.addListener('onPackageChange', listener);
}