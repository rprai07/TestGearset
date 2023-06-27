Param (
    [string]$TFSUri = "http://tfs:8080/tfs/IT/",
	[string]$Project = "SFDC",
    [string]$BuildNumber,
    [string]$MD5HashFile 
)





 function  Generate-DynamicHash{

   Param (             
            [String] $droplocation)

   $returnVal = ""
   $SourceHash = Get-ChildItem -recurse $droplocation | Get-FileHash -Algorithm "MD5"
   $dynamicArray=($SourceHash.Hash)
   foreach ($Word in $dynamicArray)
   {
     $returnVal = $returnVal + " " + $Word
   }
  
    return $returnVal.Trim()

 }


  function  Get-HashFromStaticFile {

   Param (
            [String] $staticMD5File)
    $returnVal = ""

    $staticArray=(Get-Content -Path $staticMD5File).Trim()
 
   
    foreach ($Word in $staticArray)
   {
     $returnVal = $returnVal + " " + $Word
   }
  
   return $returnVal.Trim()

 }

 function Get-BuildID {
    Param (
              [String] $buildNumber,
              [String] $tfsurl,
              [String] $project, 
              [String] $apiVersion,
              [String] $credentials)

 
    $apiPathBuildName = "{0}{1}/_apis/build/builds?api-version={2}&buildNumber={3}" -f ($TFSUri, $Project, $apiVersion,$buildNumber )

    # First option: add the credentials to the header
    
    $jsonFromTfs = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f $credentials)} -Uri $apiPathBuildName
	
    if ($jsonFromTfs.value[0] -ne $null)
    {

	    $BuildID = $jsonFromTfs.value[0].id  			
	    #Write-Output "We found the TFS Build Id: $BuildID "   	This causes a wierd side effect of assigning to the build ID
        
      return $BuildID 
	
    }
    else
    {
        return $null        

    }

 }


 function Get-DropLocation{
                    Param (
                            [String] $buildId,                             
                            [String] $tfsurl,
                            [String] $project,
                            [String] $credentials )
 
    $apiPath = "{0}{1}/_apis/build/builds/{2}/artifacts" -f ($tfsurl, $project, $buildId)

    $jsonFromTfsArtifacts = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f $credentials)} -Uri $apiPath
    if ($jsonFromTfsArtifacts.value[0] -ne $null)
    {
       
	    $artifactName = $jsonFromTfsArtifacts.value[0].name
	    $artifactPath = $jsonFromTfsArtifacts.value[0].resource.data        
        return $artifactPath
    }
    else
    {
     return $null
    }

 }


 function Get-SVC-Build-Password
 {

    #get the encryped password
    $userPwdTxt= Get-Content "\\fileshare\Private\IT\EO\Encryption\svcTFSBuild\encrypted.txt"
    #get the key -- list of numbers used to encrypt
    $keyString = Get-Content "\\fileshare\Private\it\eo\Encryption\prod\User\key.txt" 
    #convert this list of numbers to an array
    $key =  $keyString.split(',') | % {iex $_} #see: https://stackoverflow.com/questions/22925598/convert-string-to-int-array-in-powershell

    #convert the encrypted user password string to a secure string
    $userPw =$userPwdSecuerString = $userPwdTxt | ConvertTo-SecureString -key $key
    #now to plain text
    return [System.Runtime.InteropServices.marshal]::PtrToStringAuto([System.Runtime.InteropServices.marshal]::SecureStringToBSTR($userPwdSecuerString))
}




Try
{

[string]$userName = "svcTFSBuild" 

$DropLocation =""

$userPw= Get-SVC-Build-Password


$credentials = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes(("{0}:{1}" -f "Genomichealth\$userName", "$userPw")))



$BuildID = Get-BuildID -buildNumber $BuildNumber -tfsurl $TFSUri -project $Project -apiVersion "2" -credentials $credentials
           

 if ($BuildID  -ne $null)
 {
   $DropLocation = Get-DropLocation -buildId $BuildID  -tfsurl $TFSUri -project $Project -credentials $credentials
             
   if ($DropLocation  -ne $null)
   {
        $calchash=    Generate-DynamicHash -droplocation $DropLocation
        $ProvidedHash  = Get-HashFromStaticFile -staticMD5File $MD5HashFile

        if ( $calchash -eq $ProvidedHash ) 
        {
            Write-Output "**************Hashes Match**************" 
         }
        else 
        {
            Write-Output  "**************Hashes Do nNt match**************" 
            write-Error "Provided: $ProvidedHash Calculated:  $calchash"
        }
   }
 }
 else
  {
     Write-Error "The Build: $BuildNumber was not found."
  }




}#end Try
Catch
{
    Write-Error $_.Exception.Message

 }

