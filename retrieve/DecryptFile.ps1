[CmdletBinding()]
Param(
    [Parameter(Mandatory=$True)]
    [ValidateScript({Test-Path $_})]
    [string]$encryptedTextFile,
    
    [Parameter(Mandatory=$True)]
    [ValidateScript({Test-Path $_})]
    [string]$keyFile,

    [Parameter()]
    [string]$destinationPath,

    [Parameter()]
    [string]$outBuildVariable

)
# Uncomment below to exit script on first error encountered.
$ErrorActionPreference = "Stop"

#get Key from File. File must contain exactly 16, 24 or 32 characters
[byte[]]$key = Get-Content $keyFile -Encoding Byte

#decrypt to get secure string
$secureString = Get-Content $encryptedTextFile | ConvertTo-SecureString -key $key

#secure string to plaintext
$plainText = [System.Runtime.InteropServices.marshal]::PtrToStringAuto([System.Runtime.InteropServices.marshal]::SecureStringToBSTR($secureString))

if($destinationPath) { $plainText | Out-File -NoNewline $destinationPath }

if($outBuildVariable) { Write-Host "##vso[task.setvariable variable=$outBuildVariable;issecret=true]$plainText" }