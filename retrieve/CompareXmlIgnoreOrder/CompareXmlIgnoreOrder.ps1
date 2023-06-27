[CmdletBinding()]
Param(
   [Parameter(Mandatory=$True)]
   [string]$pathA,
    
   [Parameter(Mandatory=$True)]
   [string]$pathB,

   [Parameter(Mandatory=$True)]
   [string]$normalizerDllPath
)
# Add Dependencies
Add-Type -Path $normalizerDllPath
[Reflection.Assembly]::LoadWithPartialName("System.Xml.Linq") | Out-Null

$docA = [System.Xml.Linq.XDocument]::Load($pathA)
$docB = [System.Xml.Linq.XDocument]::Load($pathB)

$result = [DeepEqualsNormalizer.XmlNormalizer]::DeepEqualsWithNormalization($docA, $docB)

if (-not $result) {
    throw "Xml files do not match"
} 

else {
    "Xml files are equivalent"
}