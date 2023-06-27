echo %1 destination

echo %2 BranchLowerCase


cd /d E:\Repos\%2

 %@Try%
  del /Q /F /A ".git\index.lock"
%@EndTry%
:@Catch
  REM do nothing - index.lock does not exist
:@EndCatch

Echo --> Change to the %2 Repo


git config --global credential.helper wincred
git config --global push.default simple


git checkout %2

git pull tfsSFDC %2



Echo Delete all files in the $(Build.SourcesDirectory)\SIT
rd /s /q %1


Echo Copy the files from the E:\Repos\%2\SIT to $(Build.SourcesDirectory)\SIT Folder

xcopy /s  E:\Repos\%2\SIT  %1 /Y /I



