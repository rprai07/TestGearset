REM Command-line Arguments:
REM %1: path to retrived Profiles
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




Echo Delete all files in the ProductionProfiles\ Folder
rd /s /q "E:\Repos\%2\ProductionProfiles\profiles"

Echo --> Get the files from build and put in the git repo
xcopy /s  %1 E:\Repos\%2\ProductionProfiles\profiles\ /Y /I

git add .


git commit -a -m"auto add profile for Build"


git push tfsSFDC %2
