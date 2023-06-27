param
(
  [string] $fileName,
  [string] $destinationProfilePackagePath,
  [string] $errorsPath,
  [string] $rootDirectory
 )



 Try
  {

 if( Test-Path $destinationProfilePackagePath)
   {
       Remove-Item "$destinationProfilePackagePath\*.*" | Where { ! $_.PSIsContainer }
   }
  


   $TargetFile = "$rootDirectory\$fileName"
   $DestFile =  "$destinationProfilePackagePath\$fileName"
   if( Test-Path $DestFile)
   {
      
   }
   else
   {
   if ((Get-Item $TargetFile) -is [System.IO.DirectoryInfo] )
    {
     #   $null = New-Item -Path  $DestFile -Type Directory 
      #  Copy-Item -Path  $TargetFile -Destination $DestFile  -Container -Force 
    }
    else
    {
     $null = New-Item -Path  $DestFile -Type File -Force
      Copy-Item -Path  $TargetFile -Destination $DestFile -Verbose -Force
    }
    }
   }
   Catch
   
   {
   $_.Exception.Message | Out-File $errorsPath\errors.txt -Append

   }  