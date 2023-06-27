REM Command-line Arguments:
REM %1: path to the MD5 File
REM %2: branch name all lower-case
REM %3: Branch Name Proper Case
REM %4: Name of target file that is copied
REM %5: Name of File to check into Source
REM %6: Build Number

Echo --> Change to the %3 Repo

cd  /d C:\Repos\%2

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




Echo Delete all files in the SFDC_%3\MD5\ Folder
rd /s /q "C:\Repos\%2\MD5Hash\XAML"

Echo --> Get the files from build and put in the git repo
xcopy /s  %1 C:\Repos\%2\MD5Hash\XAML\ /Y /I

ren C:\Repos\%2\MD5Hash\XAML\%4 %5

git add .


git commit -a -m"auto add MD5 hash file for Build %6"
 

git push tfsSFDC %2
