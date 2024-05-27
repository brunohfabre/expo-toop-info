import ExpoToopInfoModule from "./ExpoToopInfoModule";

export type Application = {
  name: string;
  packageName: string;
  versionName: string;
  versionCode: number;
  firstInstallTime: string;
  lastUpdateTime: string;
  icon: string;
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

export function getInstalledPackages(supressPackages: string[]): Application[] {
  const items = returnResponse(ExpoToopInfoModule.getInstalledPackages());

  const applications = items.map((item: string) => {
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
      icon: `data:image/png;base64,${icon}`
    }
  }).filter((application: Application) => !supressPackages.some(packageName => packageName === application.packageName))

  return applications
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
