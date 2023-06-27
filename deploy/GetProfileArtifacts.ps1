

param
(
  [string] $fileName,
  [string] $destinationProfilePackagePath,
  [string] $errorsPath,
  [string] $rootDirectory,
  [string] $buildProfileFolder
 )






 #Let's generate a random name for our temporary folder
$TempDir = [System.Guid]::NewGuid().ToString()
# Now we can create our temporary folder
New-Item -Type Directory -Name $TempDir
# Let's go in our newly created temporary folder
Set-Location $TempDir

 Try
  {

 if( Test-Path $destinationProfilePackagePath)
   {
       Remove-Item "$destinationProfilePackagePath\*.*" | Where { ! $_.PSIsContainer }
   }
  


   $TargetFile = "$buildProfileFolder\$fileName"
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
 
 #read XML file and get the profiles names to copy
 # Get the content of the config file and cast it to XML and save a backup copy labeled .bak followed by the date
     $xml = (get-content $DestFile)  -as [Xml]

     $namespaceMgr = New-Object System.Xml.XmlNamespaceManager $xml.NameTable
    $namespace = $xml.DocumentElement.NamespaceURI
    $namespaceMgr.AddNamespace("http://soap.sforce.com/2006/04/metadata", $namespace)
    
 
     $namespace = @{ns="http://soap.sforce.com/2006/04/metadata"}    
    [string]$xpath='//ns:Package/ns:types[ns:name[text()="Profile"]]'
  
     $TypeNodeWithProfiles = Select-Xml -Xml $xml -XPath $xpath -Namespace $namespace | Select-Object –ExpandProperty “node”
     
     $members = $TypeNodeWithProfiles.members

     #copy all the profiles from source to the target location

     foreach ($currentProfile in $members) {

  Try
  {
  
    $TargetFile = "$rootDirectory\src\profiles\$currentProfile.profile"
    $DestinationFile =   "$destinationProfilePackagePath\$currentProfile.profile"
   
   if( Test-Path $DestinationFile)
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
     
     $null = New-Item -Path  $DestinationFile -Type File -Force
     #copy to the drop location
      Copy-Item -Path  $TargetFile -Destination $DestinationFile -Verbose -Force
   
    }
    }
   }
   Catch
   
   {
   $_.Exception.Message | Out-File $errorsPath\errors.txt -Append

   }  
   }#end for each
 
# Let's remove everything we put in our temporary folder
Remove-Item *.* -Force
# Let's go one level up
Set-Location ..
# And delete our temporary folder
Remove-Item $TempDir



