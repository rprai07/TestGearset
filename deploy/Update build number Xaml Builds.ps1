param
(
    [string] $BuildDestinationFolder
)

#Try

#{


$BuildNo = $Env:TF_BUILD_BUILDNUMBER

$DropLocation = $Env:TF_BUILD_DROPLOCATION

$BuildDefinitionName = $Env:TF_BUILD_BUILDDEFINITIONNAME


$rootPackagingPath  = $DropLocation + "\"  + $BuildDestinationFolder + "\retrieveUnpackaged"
	

$path = $rootPackagingPath + '\objects\Record_Type__c.object'



# Make sure there is a build number
if (-not  $Env:TF_BUILD_BUILDNUMBER)
{
	Write-Error ("BUILD_BUILDNUMBER environment variable is missing.")
	exit 1
}

$BuildNo =  $Env:TF_BUILD_BUILDNUMBER

$myXpath = '//fields[fullName ="Current_Release__c"]'
	

$xml = [xml] (Get-Content $path)


$nodes = $xml.CustomObject.ChildNodes

For ($i=0; $i -lt $nodes.Count; $i++)  
{
      $node = $nodes.Item($i)
      if ($node.fullName  -eq    'Current_Release__c')
      {
         $node.defaultValue = $BuildNo
      } 
    }

$xml.Save($path)

#replace the build number with escaped values as this seems to be impossible to do with xml 


(Get-Content $path).replace($BuildNo, '&quot;'+$BuildNo+'&quot;') | Set-Content $path


#}

#Catch
#{
#	 $_.Exception.Message | Out-File $ErrorsPath\errors.txt -Append
#}