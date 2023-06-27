param
(
    [Parameter(Mandatory=$true)]
    [string] $manifest_file,

    [Parameter(Mandatory=$true)]
    [string] $schema_file
    
)

function update_apex_build_variables
{
        # Import the necessary modules
        Import-Module (Resolve-Path('schema_validation.psm1'))
        Import-Module (Resolve-Path('update_build_variables.psm1'))
        
        # Verify input files for potential IO errors
        verifyFiles -xml_file $manifest_file -schema_file $schema_file

        # Perform schema validation
        Validate-XmlSchema -XmlPath $manifest_file -SchemaPath $schema_file

        # Load Manifest File
        $manifest_file_name = $manifest_file
        $manifest_file = [xml](Get-Content -Path $manifest_file -ErrorAction Stop)

        # Update Build Info Variables
        Write-Output "Updating Build Information variables..."
        [System.Xml.XmlElement[]]$build_information = $manifest_file.SelectNodes("//APEX_Build/Build_Information").ChildNodes
        # Here Update Build Variables
        update_build_variables $build_information $manifest_file_name
}

update_apex_build_variables