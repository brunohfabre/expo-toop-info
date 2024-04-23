import * as React from 'react';

import { ExpoToopInfoViewProps } from './ExpoToopInfo.types';

export default function ExpoToopInfoView(props: ExpoToopInfoViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
