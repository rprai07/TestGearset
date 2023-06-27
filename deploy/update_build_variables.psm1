<#
    check_build variable

    .Synopsis
    This function is used to check the contents of a build variable found in the XML during parsing.
    If a build variable is found with Null or Whitespace the program will terminate letting the user
    know about the syntax error.

    .Parameter manifest_build_variable
    This is the build variable value found in the manifest file's XML. 

    .Parameter build_variable_name
    This is the name of the build variable node in the XML. It is used for error logging.

    .Parameter manifest_file_name
    This is the name of the XML manifest file. It is used for error logging.

#>
function check_build_variable
{
    param
    (
        $manifest_build_variable,
        $build_variable_name,
        $manifest_file_name
     )
    
    $check_build_code_name = [string]::IsNullOrWhiteSpace($manifest_build_variable)
    
    if ($check_build_code_name) 
    {
        $manifest_build_variable = ""
        return $manifest_build_variable 
    }
    else
    {
        $manifest_build_variable = $manifest_build_variable.trim()
    }
}
<#
    update_build_variable

    .Synopsis
    This function is used to update the build definition variable with the one supplied
    in the XML after being parsed. It will also trim the build variable value as it is being updated.

    .Parameter manifest_build_variable
    This is the build variable value found in the XML manifest file. It is used to update the build
    definition variable.

    .Parameter build_variable_name
    This is the build variable from the build definition. This is used to reference the build
    definition variable when running the update command. It is also used for error logging.

    .Parameter manifest_file_name
    This is the name of the XML manifest file. It is used for error logging.

#>
function update_build_variable
{
    param
    (
        $build_variable_name,
        $manifest_build_variable,
        $manifest_file_name
    )

    check_build_variable $manifest_build_variable $build_variable_name $manifest_file_name
    $update_variable = "##vso[task.setvariable variable=" + $build_variable_name + ";]$($manifest_build_variable)" 
    Write-Host $update_variable
}

<#
    update_build_variables

    .Synopsis
    This function is used to update all build definition variables with the values found in an manifest XML file that
    have the corresponding build definition variables as nodes.

    .Parameter parsed_build_data
    This is an array of parsed XML values from the manifest file.

    .Parameter manifest_file_name
    This is the name of the XML manifest file. It is used for error logging.
#>
function update_build_variables 
{
    param 
    (
        $parsed_build_data,
        $manifest_file_name
    )

    # Initialize Build Information
    $dynamic_build_data_array = @()

    # We can use this in the event that we want to parse the Build Definition Variable Names from the XML.
    # However that means that the XML Nodes and the Build Definition Nodes must be the same
    $build_data_array = @()

    # Parse Build Information from Manifest File
    $number_of_build_vars = $parsed_build_data.Length

    for($index = 0; $index -lt $number_of_build_vars; $index++)
    {
        $build_data_array +=  $parsed_build_data[$index].Name
        $dynamic_build_data_array += $parsed_build_data[$index].'#text'
    }
    
    for($index = 0; $index -lt $number_of_build_vars; $index++)
    {
        update_build_variable $build_data_array[$index] $dynamic_build_data_array[$index] $manifest_file_name
    }

}

Export-ModuleMember -Function update_build_variables
Export-ModuleMember -Function update_build_variable