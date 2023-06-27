REM Command-line Arguments:
REM %1: path to build drop location
REM %2: branch name all lower-case
REM %3: Branch Name Proper Case

Echo --> Change to the %3 Repo

cd  /d E:\Repos\%2

 %@Try%
  del /Q /F /A ".git\index.lock"
%@EndTry%
:@Catch
  REM do nothing - index.lock does not exist
:@EndCatch


git config --global credential.helper wincred
git config --global push.default simple


git checkout %2

git pull tfsSFDC %2




Echo Delete all files in the SFDC_%3_SIT\Profiles\ Folder
rd /s /q "E:\Repos\%2\Build\SFDC_%3_SIT\Profiles"

Echo --> Get the files from build and put in the git repo
xcopy /s  %1 E:\Repos\%2\Build\SFDC_%3_SIT\Profiles\ /Y /I

Echo --> Copy SIT profile package to SFDC_%3_SIT Folder
if exist "E:\Repos\%2\Build\SFDC_%3_SIT\Profiles\sitprofilepackage.xml" del /f /q "E:\Repos\%2\Build\SFDC_%3_SIT\Profiles\sitprofilepackage.xml"
if exist "E:\Repos\%2\sit\sitprofilepackage.xml" copy "E:\Repos\%2\sit\sitprofilepackage.xml" "E:\Repos\%2\Build\SFDC_%3_SIT\Profiles\profilepackage.xml"
if exist "%1\sitprofilepackage.xml" del "%1\sitprofilepackage.xml"
if exist "E:\Repos\%2\sit\sitprofilepackage.xml" copy "E:\Repos\%2\sit\sitprofilepackage.xml" "%1\profilepackage.xml"

git add .


git commit -a -m"auto add profile for Build"


git push tfsSFDC %2
