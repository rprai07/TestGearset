function Validate-XmlSchema
{
    param
    (
        [Parameter(Mandatory = $true)]
        [String] $XmlPath,
       
        [Parameter(Mandatory = $true)]
        [String] $SchemaPath
    )

       $schemas = New-Object System.Xml.Schema.XmlSchemaSet
       $schemas.CompilationSettings.EnableUpaCheck = $false
       $schema = ReadSchema $SchemaPath
       [void]($schemas.Add($schema))
       $schemas.Compile()
      
       try
       {
              [xml]$xmlData = Get-Content $XmlPath
              $xmlData.Schemas = $schemas

              #Validate the schema. This will fail if is invalid schema
              $xmlData.Validate($null)
       }

       catch [System.Xml.Schema.XmlSchemaValidationException]
       {      
              $error_message = "The manifest file " + $manifest_file + " does not follow the correct Schema. `n"
              $error_message += "Please correct the manifest file's schema. Please follow the example in this location <location>"
              Write-Error $error_message -ErrorAction Stop
       }
}

function ReadSchema
{
       param($SchemaPath)
       try
       {  
              $schemaFile = Get-Item $SchemaPath
              $stream = $schemaFile.OpenRead()
              $schema = [Xml.Schema.XmlSchema]::Read($stream, $null)
              return $schema
       }
       
       # If there is something wrong with the Schema File, if the schema is invalid or empty or syntactically wrong.
       catch [System.Xml.XmlException]
       {
              $error_message = "There was an error when trying to parse the XML schema. Please check the schema file for syntatical or logical errors."
              Write-Error $error_message -ErrorAction Stop
       }
       # If something went wrong with the filestream e.g. file became inaccessible or got deleted or something
       catch [System.ObjectDisposedException]
       {
              $error_message = "The schema file's filestream has closed unexpectedly."
              throw $error_message
       }
       finally
       {
              if($stream)
              {
                     $stream.Close()
              }
       }
}

function verifyFiles
{
    param($xml_file, $schema_file)

     # Verify that the files supplied exist
     $check_manifest = Test-Path $xml_file -PathType Leaf
     $check_schema = Test-Path $schema_file -PathType Leaf
 
     if ($check_manifest -eq $false)
     {
         Write-Error "Manifest file could not be found." -ErrorAction Stop
     }
 
     if ($check_schema -eq $false)
     {
         Write-Error "Schema file could not be found" -ErrorAction Stop
     }

     # Verify that both files have the proper extensions.
     $xml_file_extension = [IO.Path]::GetExtension($xml_file)
     $schema_file_extension = [IO.Path]::GetExtension($schema_file)

     if ($xml_file_extension -ne ".xml")
     {
         Write-Error "Supplied manifest file's extension must be in XML." -ErrorAction Stop
     }

     if ($schema_file_extension -ne ".xsd")
     {
         Write-Error "Supplied schema file's extension must be in XSD." -ErrorAction Stop
     }
}

Export-ModuleMember -Function Validate-XmlSchema
Export-ModuleMember -Function verifyFiles