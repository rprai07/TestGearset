REM Command-line Arguments:
REM %1: path to build drop location
REM %2: branch name all lower-case
REM %3: Branch Name Proper Case

cd /d E:\Repos\%2 

 %@Try%
  del /Q /F /A ".git\index.lock"
%@EndTry%
:@Catch
  REM do nothing - index.lock does not exist
:@EndCatch

Echo --> Change to the %3 Repo


git config --global credential.helper wincred
git config --global push.default simple


git checkout %2

git pull tfsSFDC %2



Echo Delete all files in the SFDC_%3_SIT Folder
rd /s /q "E:\Repos\%2\Build\SFDC_%3_SIT"

REM Echo Copy the files from the E:\Repos\%2\Build\SFDC_%3 to E:\Repos\%2\Build\SFDC_%3_SIT\

REM xcopy /s  E:\Repos\%2\Build\SFDC_%3 E:\Repos\%2\Build\SFDC_%3_SIT\ /Y /I

REM Echo Delete all files in the SFDC_%3_SIT\retrieveUnpackaged\ Folder
REM rd /s /q "E:\Repos\%2\Build\SFDC_%3_SIT\retrieveUnpackaged"

Echo --> Get the files from build and put in the git repo
xcopy /s  %1\retrieveUnpackaged E:\Repos\%2\Build\SFDC_%3_SIT\retrieveUnpackaged\ /Y /I

Echo --> Get Documentation files
if exist "E:\Repos\%2\FT\doc" xcopy /s "E:\Repos\%2\FT\doc" "E:\Repos\%2\Build\SFDC_%3_SIT" /Y /I
if exist "E:\Repos\%2\FT\doc" xcopy /s "E:\Repos\%2\FT\doc" "%1" /Y /I

Echo --> Get Additional Build Files
if exist "E:\Repos\%2\FT\Additional_Build_Files" xcopy /s "E:\Repos\%2\FT\Additional_Build_Files" "E:\Repos\%2\Build\SFDC_%3_SIT" /Y /I
if exist "E:\Repos\%2\FT\Additional_Build_Files" xcopy /s "E:\Repos\%2\FT\Additional_Build_Files" "%1" /Y /I

Echo --> Get Additional SIT Build Files
if exist "E:\Repos\%2\SIT\Additional_Build_Files" xcopy /s "E:\Repos\%2\SIT\Additional_Build_Files" "E:\Repos\%2\Build\SFDC_%3_SIT" /Y /I
if exist "E:\Repos\%2\SIT\Additional_Build_Files" xcopy /s "E:\Repos\%2\SIT\Additional_Build_Files" "%1" /Y /I

Echo --> Get Automation files
if exist "E:\Repos\%2\Automation" xcopy /s "E:\Repos\%2\Automation" "E:\Repos\%2\Build\SFDC_%3_SIT\Automation" /Y /I
if exist "E:\Repos\%2\Automation" xcopy /s "E:\Repos\%2\Automation" "%1\Automation" /Y /I

Echo --> Get deploy files
if exist "E:\Repos\%2\deploy" xcopy /s "E:\Repos\%2\deploy" "E:\Repos\%2\Build\SFDC_%3_SIT\deploy" /Y /I
if exist "E:\Repos\%2\deploy" xcopy /s "E:\Repos\%2\deploy" "%1\deploy" /Y /I

git add .


git commit -a -m"auto add retrieveUnpackaged for Build"


git push tfsSFDC %2

