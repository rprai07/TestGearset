


#get the encryped password
$userTxt= Get-Content "\\fileshare\Private\it\eo\Encryption\prod\User\encrypted.txt"
$userPwdTxt= Get-Content "\\fileshare\Private\it\eo\Encryption\prod\pw\encrypted.txt"
#get the key -- list of numbers used to encrypt
$keyString = Get-Content "\\fileshare\Private\it\eo\Encryption\prod\User\key.txt" 
#convert this list of numbers to an array
$key =  $keyString.split(',') | % {iex $_} #see: https://stackoverflow.com/questions/22925598/convert-string-to-int-array-in-powershell
#convert the encrypted user name string to a secure string
$userSecuerString = $userTxt | ConvertTo-SecureString -key $key
#now to plain text
$userName = [System.Runtime.InteropServices.marshal]::PtrToStringAuto([System.Runtime.InteropServices.marshal]::SecureStringToBSTR($userSecuerString))

#convert the encrypted user password string to a secure string
$userPw =$userPwdSecuerString = $userPwdTxt | ConvertTo-SecureString -key $key
#now to plain text
$userPw= [System.Runtime.InteropServices.marshal]::PtrToStringAuto([System.Runtime.InteropServices.marshal]::SecureStringToBSTR($userPwdSecuerString))

#update the build definition variables
Write-Host "##vso[task.setvariable variable=productionProfileUserName;issecret=true]$userName"
Write-Host "##vso[task.setvariable variable=productionProfilePassword;issecret=true]$userPw"
