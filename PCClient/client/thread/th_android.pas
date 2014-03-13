unit th_android;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, basethread, unt_android;

type

  { TAndroidThread }

  TAndroidThread = class(TThreadBase)
  private
    // 0: screenshot
    // 1: build.prop
    // 2: root tools version code
    FDeviceId: string;
    FCmdType: integer;
    FCmdParam: array of string;
    // screenshot
    // build.prop
    FPath: string;
    // root tools version code
    FVersionCode: integer;
  protected
    procedure Execute; override;
    function MakeNotifyMap: TStringList; override;
  public
    procedure SetDeviceId(ADevice: string);
    procedure SetCommand(AType: integer; AParams: array of string);
  end;

implementation

{ TAndroidThread }

procedure TAndroidThread.Execute;
begin
  case FCmdType of
    0:
    begin
      FPath := GetScreenshot(FDeviceId);
    end;
    1:
    begin
      FPath := GetBuildProp(FDeviceId);
    end;
    2:
    begin
      FVersionCode := GetRootToolsVersion(FDeviceId);
    end;
  end;
end;

function TAndroidThread.MakeNotifyMap: TStringList;
begin
  Result := TStringList.Create;
  case FCmdType of
    0, 1:
    begin
      Result.Add('path=' + FPath);
    end;
    2:
    begin
      Result.Add('version=' + IntToStr(FVersionCode));
    end;
  end;
end;

procedure TAndroidThread.SetDeviceId(ADevice: string);
begin
  FDeviceId := ADevice;
end;

procedure TAndroidThread.SetCommand(AType: integer; AParams: array of string);
var
  i: integer;
begin
  FCmdType := AType;
  SetLength(FCmdParam, Length(AParams));
  for i := 0 to Length(AParams) - 1 do
  begin
    FCmdParam[i] := AParams[i];
  end;
end;

end.

