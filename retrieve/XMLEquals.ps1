[CmdletBinding()]
Param(
   [Parameter(Mandatory=$True)]
   [string]$pathA,
	
   [Parameter(Mandatory=$True)]
   [string]$pathB
)

[Reflection.Assembly]::LoadWithPartialName("System.Xml.Linq") | Out-Null
$docA = [System.Xml.Linq.XDocument]::Load($pathA)
$docB = [System.Xml.Linq.XDocument]::Load($pathB)

#Get and remove all comments; we don't care if they match
$docAComments = $docA.DescendantNodes() | Where-Object { $_.NodeType -eq [System.Xml.XmlNodeType]::Comment}
$docBComments = $docB.DescendantNodes() | Where-Object { $_.NodeType -eq [System.Xml.XmlNodeType]::Comment}
$docAComments | foreach { $_.Remove()}
$docBComments | foreach { $_.Remove()}

$result = [System.Xml.Linq.XNode]::DeepEquals($docA.FirstNode, $docB.FirstNode)
if (-not $result) {
    throw "Xml files do not match"
} 

else {
    "Xml files are equivalent"
}