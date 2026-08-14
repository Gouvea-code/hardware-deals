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
});
