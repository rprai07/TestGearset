Param([switch] $FtBuild)

$ErrorMessages = ""

# The BranchLowerCase varaiable must match the 
# name of the source branch for the build (e.g. "cheers")
if ($env:BRANCHLOWERCASE -cne $env:BUILD_SOURCEBRANCHNAME) {
    $ErrorMessages += "`$(BranchLowerCase) does not match source branch name`n"
}

# The BranchPropercase variable should match the BranchLowerCase,
# except with the first letter capitalized (e.g. "Cheers")
if ($env:BRANCHPROPERCASE -ne $env:BRANCHLOWERCASE) {
    $ErrorMessages += "`$(BranchProperCase) does not match `$(BranchLowerCase)`n"
}

# DevOrgName variable must be defined (e.g. "dev10")
if (!$env:DEVORGNAME) {
    $ErrorMessages += "`$(DevOrgName) is not defined`n"
}

# BuildOrgName variable must be defined (e.g. "dev10")
if (!$env:BUILDORGNAME) {
    $ErrorMessages += "`$(BuildOrgName) is not defined`n"
}

# if $FtBuild switch is set, FtOrgName variable must be defined (e.g. "dev10")
if ($FtBuild -and !$env:FTORGNAME) {
    $ErrorMessages += "`$(FtOrgName) is not defined`n"
}

if ($ErrorMessages) {
    throw $ErrorMessages
} else {
    'All Build Variables validated successfully'
}