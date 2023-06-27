
param
(
    [string] $rootPackagingPath, 
    [string] $dropPath
)

$sourceDir = $rootPackagingPath +"\retrieveUnpackaged"
$destinationDirectory = $dropPath 
$ErrorsPath =$rootPackagingPath +"\PackagingErrors"

dir -r $destinationDirectory |% {if ($_.PSIsContainer -ne $true){$_.IsReadOnly = $false}}




Try
{
  $tempDestPathArg = $destinationDirectory + "\retrieveUnpackaged\*"
Remove-Item $tempDestPathArg -recurse




  Try
  {
      Copy-Item   $sourceDir $destinationDirectory -Verbose -recurse -force
  }
  
   Catch
   
   {
   $_.Exception.Message | Out-File $ErrorsPath\errors.txt -Append

   }  

dir -r $destinationDirectory |% {if ($_.PSIsContainer -ne $true){$_.IsReadOnly = $false}}
}

Catch
   {
   $_.Exception.Message | Out-File $ErrorsPath\errors.txt -Append

   } 
