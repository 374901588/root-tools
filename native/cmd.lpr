library cmd;

{$mode objfpc}{$H+}

uses
  cthreads, Classes, sysutils, JNI2, math, unt_ljava, unt_clean, unt_freeze;

const
  BASE_URL: string = 'http://rarnu.xyz/root/';

// JNI exchange method
function Java_com_rarnu_tools_neo_api_NativeAPI_mount(env: PJNIEnv; obj: jobject): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := Mount();
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_isSystemRW(env: PJNIEnv; obj: jobject): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := IsSystemRW();
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

procedure Java_com_rarnu_tools_neo_api_NativeAPI_makePreferenceReadable(env: PJNIEnv; obj: jobject; sdk: jint; packageName: jstring); stdcall;
begin
  MakePreferenceReadable(sdk, TJNIEnv.jstringToString(env, packageName));
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_freezeApplication(env: PJNIEnv; obj: jobject; packageName: jstring; isFreezed: jboolean): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := FreezeApplication(TJNIEnv.jstringToString(env, packageName), isFreezed = JNI_TRUE);
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_freezeComponent(env: PJNIEnv; obj: jobject; packageName: jstring; componentName: jstring; isFreezed: jboolean): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := FreezeComponent(TJNIEnv.jstringToString(env, packageName), TJNIEnv.jstringToString(env, componentName), isFreezed = JNI_TRUE);
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_freezeComponents(env: PJNIEnv; obj: jobject; packageName: jstring; components: jarray; isFreezed: jboolean): jboolean; stdcall;
var
  strArr: TStringArray;
  ret: Boolean;
begin
  strArr := TJNIEnv.jstringArrayToStringArray(env, components);
  ret := FreezeComponents(TJNIEnv.jstringToString(env, packageName), strArr, isFreezed = JNI_TRUE);
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

procedure Java_com_rarnu_tools_neo_api_NativeAPI_systemClean(env: PJNIEnv; obj: jobject; ctx: jobject); stdcall;
begin
  CleanData(env, ctx);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_writeFile(env: PJNIEnv; obj: jobject; ctx: jobject; filePath: jstring; text: jstring; perm: jint): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := WriteFile(TJNIEnv.jstringToString(env, filePath), TJNIEnv.jstringToString(env, text), perm);
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_catFile(env: PJNIEnv; obj: jobject; src: jstring; dest: jstring; perm: jint): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := CatFile(TJNIEnv.jstringToString(env, src), TJNIEnv.jstringToString(env, dest), perm);
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_deleteFile(env: PJNIEnv; obj: jobject; src: jstring): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := DeleteFile(TJNIEnv.jstringToString(env, src));
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

procedure Java_com_rarnu_tools_neo_api_NativeAPI_forceDeleteFile(env: PJNIEnv; obj: jobject; path: jstring); stdcall;
begin
  ForceDeleteFile(TJNIEnv.jstringToString(env, path));
end;

procedure Java_com_rarnu_tools_neo_api_NativeAPI_forceDropCache(env: PJNIEnv; obj: jobject); stdcall;
begin
  ForceDropCache();
end;

procedure Java_com_rarnu_tools_neo_api_NativeAPI_killProcess(env: PJNIEnv; obj: jobject); stdcall;
begin
  KillProcess();
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_deleteSystemApp(env: PJNIEnv; obj: jobject; pkgName: jstring): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := DeleteSystemApp(TJNIEnv.jstringToString(env, pkgName));
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

function Java_com_rarnu_tools_neo_api_NativeAPI_isAppRequiredBySystem(env: PJNIEnv; obj: jobject; pkgName: jstring): jboolean; stdcall;
var
  ret: Boolean;
begin
  ret := IsAppRequiredBySystem(TJNIEnv.jstringToString(env, pkgName));
  Result := ifthen(ret, JNI_TRUE, JNI_FALSE);
end;

// ======================================
// URL
// ======================================

function Java_com_rarnu_tools_neo_api_NativeAPI_getBaseURL(env: PJNIEnv; obj: jobject): jstring; stdcall;
begin
  Result := TJNIEnv.stringToJString(env, BASE_URL);
end;

// ======================================
// freezed apps
// ======================================

procedure Java_com_rarnu_tools_neo_api_NativeAPI_freezeUpdateList(env: PJNIEnv; obj: jobject; pkg: jstring; comp: jstring; enabled: jboolean); stdcall;
begin
  updateFreezeList(
    TJNIEnv.jstringToString(env, pkg),
    TJNIEnv.jstringToString(env, comp),
    enabled = JNI_TRUE
  );
end;

procedure Java_com_rarnu_tools_neo_api_NativeAPI_freezeOnLoad(env: PJNIEnv; obj: jobject); stdcall;
begin
  freezeOnLoad();
end;

exports
  Java_com_rarnu_tools_neo_api_NativeAPI_mount,
  Java_com_rarnu_tools_neo_api_NativeAPI_isSystemRW,
  Java_com_rarnu_tools_neo_api_NativeAPI_makePreferenceReadable,
  Java_com_rarnu_tools_neo_api_NativeAPI_freezeApplication,
  Java_com_rarnu_tools_neo_api_NativeAPI_freezeComponent,
  Java_com_rarnu_tools_neo_api_NativeAPI_freezeComponents,
  Java_com_rarnu_tools_neo_api_NativeAPI_systemClean,
  Java_com_rarnu_tools_neo_api_NativeAPI_writeFile,
  Java_com_rarnu_tools_neo_api_NativeAPI_catFile,
  Java_com_rarnu_tools_neo_api_NativeAPI_deleteFile,
  Java_com_rarnu_tools_neo_api_NativeAPI_forceDeleteFile,
  Java_com_rarnu_tools_neo_api_NativeAPI_forceDropCache,
  Java_com_rarnu_tools_neo_api_NativeAPI_killProcess,
  Java_com_rarnu_tools_neo_api_NativeAPI_deleteSystemApp,
  Java_com_rarnu_tools_neo_api_NativeAPI_isAppRequiredBySystem,

  // ======================================
  // URL
  // ======================================
  Java_com_rarnu_tools_neo_api_NativeAPI_getBaseURL,

  // ======================================
  // freezed apps
  // ======================================
  Java_com_rarnu_tools_neo_api_NativeAPI_freezeUpdateList,
  Java_com_rarnu_tools_neo_api_NativeAPI_freezeOnLoad;

begin

end.

