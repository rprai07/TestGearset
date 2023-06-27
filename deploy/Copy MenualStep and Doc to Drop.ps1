
param
(
    [string] $rootPackagingPath, 
    [string] $dropPath,
	[string] $ManualSteps,
	[string] $ManualStepsDoc
)

$sourceDir = $rootPackagingPath +"\doc"
$ErrorsPath = $rootPackagingPath +"\PackagingErrors"

dir -r $destinationDirectory |% {if ($_.PSIsContainer -ne $true){$_.IsReadOnly = $false}}

Try
{
  if (Test-Path $dropPath) 
  {
    Remove-Item "$dropPath\$ManualSteps"
	Remove-Item "$dropPath\$ManualStepsDoc" 
   }

    
  Try
  {
      Copy-Item -Path  "$sourceDir\$ManualSteps" -Destination $dropPath -Verbose -Force
	  Copy-Item -Path  "$sourceDir\$ManualStepsDoc" -Destination $dropPath -Verbose -Force
  }
  
  Catch
   
  {
  $_.Exception.Message | Out-File $ErrorsPath\errors.txt -Append

  }  

dir -r $dropPath |% {if ($_.PSIsContainer -ne $true){$_.IsReadOnly = $false}}
}

Catch
   {
   $_.Exception.Message | Out-File $ErrorsPath\errors.txt -Append

   } 
