



cd   C:\Repos\Poppy


Echo --> Change to the Poppy Repo


git config --global credential.helper wincred
git config --global push.default simple


git checkout poppy

git pull tfsSFDC poppy


git pull tfsSFDC poppy

Echo --> Get the files from build and put git repo
xcopy /s  %1 C:\Repos\Poppy\Build\SFDC_Poppy_SIT\retrieveUnpackaged\ /Y /I

git add .


git commit -a -m"auto add retrieveUnpackaged for Build"


git push tfsSFDC poppy


