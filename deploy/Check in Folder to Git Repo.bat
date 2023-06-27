REM Command-line Arguments:
REM %1: path to build drop location
REM %2: branch name all lower-case
REM %3: Branch Name Proper Case


cd  /d E:\Repos\%2 


Echo --> Change to the %3 Repo
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


Echo Remove Build\SFDC_%3
rd /s /q "E:\Repos\%2\Build\SFDC_%3"


Echo --> Copy folder to Repo
xcopy /s "%1" "E:\Repos\%2\Build\SFDC_%3" /Y /I



git add .

git commit -a -m"auto add retrieveUnpackaged for Build"


git push tfsSFDC %2


