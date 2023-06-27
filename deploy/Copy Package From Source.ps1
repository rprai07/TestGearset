<#
.SYNOPSIS
This script is used in SFDC builds to copy files from "src" to
"retrieveUnpackaged_src", based on the structure of "retrieve" (result of
retrieve from the dev box). It is a modification of the
Copy-FilesBasedOnTemplate script that allows for specifying "package.xml" in
any location, and with any name.

.PARAMETER retrieveDirectory
directory to base the structure on

.PARAMETER srcDirectory
directory to copy files from

.PARAMETER destinationDirectory
directory to copy into

.PARAMETER fullPackageManifestPath
path to package.xml to copy from, instead of the one located in srcDirectory

.EXAMPLE
Copy files from "SFDC/src" to intermediate "retrieveUnpackaged_src" folder,
using the stucture of intermediate "retrieve folder".
Take package.xml from "SFDC/SIT/sitpackage.xml"
Assume running from TF build with build variables setup.

Copy-FilesBasedOnTemplate -retrieveDirectory $(Build.StagingDirectory)\Packages\retrieve 
                          -srcDirectory $(Build.SourcesDirectory)\src
                          -destinationDirectory $(Build.StagingDirectory)\Packages\retrieveUnpackaged_src
                          -fullPackageManifestPath $(Build.SourcesDirectory)\SIT\sitpackage.xml
#>

[CmdletBinding()]
Param(
    [Parameter(Mandatory=$True)]
    [ValidateScript({Test-Path $_})]
    [string]$retrieveDirectory,
    
    [Parameter(Mandatory=$True)]
    [ValidateScript({Test-Path $_})]
    [string]$srcDirectory,

    [Parameter(Mandatory=$True)]
    [string]$destinationDirectory,

    [Parameter(Mandatory=$True)]
    [ValidateScript({Test-Path $_})]
    [string]$fullPackageManifestPath
)
# Uncomment below to exit script on first error encountered.
# $ErrorActionPreference = "Stop"

try {
    # This gets the file paths relative to $retrieveDirectory, not full paths. Perfect.
    $files = Get-ChildItem -Recurse -File -Name $retrieveDirectory

    foreach ($file in $files) {
        # For the package manifest (called "package.xml" in an SFDC
        # retrieve), we want to be able to copy the contents of any
        # file location. This allows for a manifest stored in FT/package.xml,
        # SIT/sitpackage.xml, or any other location.
        if ($file -eq "package.xml") {
            # take package manifest from location specified in
            # $fullPackageManifestPath command-line argument, instead of from src.
            # Regardless of source name, the manifest will be copied as "package.xml"
            $fullFileToCopyPath = $fullPackageManifestPath
        } else {
            # concatenate paths to get full source path
            $fullFileToCopyPath = Join-Path $srcDirectory $file
        }

        $fullFileDestinationPath = Join-Path $destinationDirectory $file

        # Create destination parent directory if it doesn't exist
        $destinationParent = Split-Path -Path $fullFileDestinationPath
        if(!(Test-Path $destinationParent)) {
            New-Item -ItemType Directory -Force -Path $destinationParent
        }

        Copy-Item -Verbose $fullFileToCopyPath $fullFileDestinationPath
    }

}
catch {
    Write-Error "Exception while copying package: $_.Exception.Message"
    throw
}