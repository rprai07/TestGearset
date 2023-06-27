param
(
    [string] $rootPackagingPath, 
    [string] $dropPath
)

$destinationDirectory = $dropPath 
$targetFile = "\CommitID.txt"
$ErrorsPath =$rootPackagingPath +"\PackagingErrors"

dir -r $destinationDirectory |% {if ($_.PSIsContainer -ne $true){$_.IsReadOnly = $false}}




Try
{
  $targetFile = $destinationDirectory + $targetFile
  if (Test-Path $targetFile) 
  {
    Remove-Item $targetFile 
   }



  Try
  {

      $message = "The Check id is:`n"
      $checkinID =   (Get-Item Env:\BUILD_SOURCEVERSION).Value
      $message + $checkinID | Out-File $targetFile
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
