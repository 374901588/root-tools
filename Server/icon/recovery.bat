@echo off
title ��ΪC8825D/Ascend G330C ˢRecoveryר�ù��� Neekh Huayi550(����ؾ�)
@color 0A
:check
@echo off
@if not exist "fastboot.exe" goto nofsb
@if not exist "adb.exe" goto nofsb
@if not exist "g330c-recovery-cn.img" goto romfsb
@echo ---------------------------------------------------------------
@echo	      ��ΪC8825D/Ascend G330C ˢRecoveryר�ù���
@echo.
@echo               Recovery 6.0.1.2���İ�
@echo ---------------------------------------------------------------
@echo ���ܽ��ܣ�
@echo     (1) �Ѱ���Recovery 6.0.1.2���İ�
@echo     (2) ���Ϲ��ߺ�RECΪһ������
@echo     (3) ר��Ϊ��ΪC8825D/Ascend G330C��ˢ���ߣ�
@echo ʹ�ò��裺
@echo     (1) ȷ������ȷ��װ��ADB������
@echo.
@echo     (2) ���Ƚ���FASTBOOTģʽ���������º͵�Դ����
@echo.
@echo     (3) ����10�����ң�ͣ�ھջ��������������ɹ���
@echo.
@echo     (4) ����USB�����ߣ����ӵ��ԡ�
@echo.
@echo                            2012��9��8�� Neekh Huayi550
@echo ---------------------------------------------------------------
@echo ��ȷ�����ϲ��衣Ȼ��ʼˢ��...
@pause >nul
goto fsb

:nofsb
@echo ȱ��fastboot.exe��adb.exe�ȳ���
@pause
:romfsb
echo ȱ��recoveryӳ���ļ�������������recovery�ļ�����
@pause
@cls
goto check

:wait3
@echo ============================================================
@echo û�ҵ��豸����ȷ���������ӡ�
@echo.
@echo ��ȷ���Ƿ����FASTBOOTģʽ���Ƿ�װADB������
@echo.
@echo ��⵽�ƺ�δ��װ����,����������.�밲װ����.
@echo.
@echo �����װ�����������²��USB����������.
@echo ============================================================
@echo.
@echo ���롰Y��5����⣬����"N"�˳���
@echo Yes.NO[Y,N]?
@set /p choice=
@if "%choice%"=="Y" goto wait4
@if "%choice%"=="y" goto wait4
@if "%choice%"=="N" exit
@if "%choice%"=="n" exit
@if "%choice%"=="Q" exit
@if "%choice%"=="q" exit
@echo ѡ����������ԡ�
@echo.
@pause
@cls
@goto check

:wait4
@ping 127.0.0.1 /n 5 >nul
@goto check
@cls

:fsb
@cls
@echo ---------------------------------------------------------------
@echo	      ��ΪC8825D/Ascend G330C ˢRecoveryר�ù���
@echo.
@echo               Recovery 6.0.1.2���İ�
@echo ---------------------------------------------------------------
@echo ���ܽ��ܣ�
@echo     (1) �Ѱ���Recovery 6.0.1.2���İ�
@echo     (2) ���Ϲ��ߺ�RECΪһ������
@echo     (3) ר��Ϊ��ΪC8825D/Ascend G330C��ˢ���ߣ�
@echo ע�����
@echo     (1) �����޸�Recovery��imgӳ������
@echo     (2) ȷ��������װ��ȷ��������ˢ�롣
@echo.
@echo                            2012��9��8�� by Neekh Huayi550
@echo ---------------------------------------------------------------
@echo �ѽ���ˢ��ģʽ��׼����ʼˢRecovery.
@pause
@fastboot devices >nul
@echo ����д�����ݣ������ĵȴ�����
@echo ===============================================================
@echo �Ƿ񿴼�����������ʾ��                                      
@echo          "sending 'recovery' <6598>...OKAY"                
@echo          "writing 'recovery'...OKAY"                        
@echo �������������ʾ��֤������ɹ���                            
@echo.                                                           
@echo ��ʱǧ��Ҫ�رմ˴���                                     
@echo.                                                            
@echo ����"< waiting for device >" ��ʾ�����������������δ��������   
@echo ===============================================================
@echo.
@echo.
@fastboot flash recovery g330c-recovery-cn.img >nul
@echo ����д������ɣ���
@echo.
@echo ˢ��REC�ѽ�����������������ֻ����˳����ߣ���
@pause >nul
@fastboot reboot
@exit