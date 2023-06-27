
param
(
    [string] $DestinationDirectory, 
    [string] $BuildDropPath,
    [string] $BuildName
)

Try
{

dir -r $DestinationDirectory |% {if ($_.PSIsContainer -ne $true){$_.IsReadOnly = $false}}

$SourceHash = Get-ChildItem -recurse $BuildDropPath | Get-FileHash -Algorithm "MD5"


$targetFile =$DestinationDirectory+"\"+ $BuildName +"_MD5_Hash_File.txt"
 if (Test-Path $targetFile) 
  {
    Remove-Item $targetFile 
   }

 #save the file
 $SourceHash.Hash | Out-File $targetFile -Force

 }
 Catch
 {
  Write-Error (" $_.Exception.Message")

 }