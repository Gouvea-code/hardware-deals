import fs from 'fs';
import path from 'path';

describe('Android distribution configuration', () => {
  it('targets API 36 and keeps the public application id stable', () => {
    const rootGradle=fs.readFileSync(path.join(__dirname,'../android/build.gradle'),'utf8');
    const appGradle=fs.readFileSync(path.join(__dirname,'../android/app/build.gradle'),'utf8');
    expect(rootGradle).toContain('targetSdkVersion = 36');
    expect(appGradle).toContain('applicationId "com.hardwaredeals"');
    expect(appGradle).toContain('versionName "1.0.0"');
  });

  it('requests only internet and notification permissions', () => {
    const manifest=fs.readFileSync(path.join(__dirname,'../android/app/src/main/AndroidManifest.xml'),'utf8');
    const permissions=[...manifest.matchAll(/uses-permission android:name="([^"]+)"/g)].map(match=>match[1]);
    expect(permissions).toEqual(['android.permission.INTERNET','android.permission.POST_NOTIFICATIONS']);
    expect(manifest).not.toMatch(/READ_CONTACTS|ACCESS_FINE_LOCATION|RECORD_AUDIO|CAMERA|READ_SMS/);
  });
});
