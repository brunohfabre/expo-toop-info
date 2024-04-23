import ExpoToopInfoModule from "./ExpoToopInfoModule";

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

export function getInstalledPackages(): string {
  return returnResponse(ExpoToopInfoModule.getInstalledPackages());
}

export function sendActivityResultOk(): string {
  return returnResponse(ExpoToopInfoModule.sendActivityResultOk());
}

export function launchApplication(packageName: string): string {
  return returnResponse(ExpoToopInfoModule.launchApplication(packageName));
}
