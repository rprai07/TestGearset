Param
(
	[string]$retrievePath
)

Get-ChildItem $retrievePath  -Filter “*%28*” -Recurse | Rename-Item -NewName {$_.name -replace ‘%28’,’(’ }
Get-ChildItem $retrievePath  -Filter “*%29*” -Recurse | Rename-Item -NewName {$_.name -replace ‘%29’,’)’ }