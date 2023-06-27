/*
    Date: 07 April 2023
    Developer: Davinder Syall
    Description :  303503: OM: EuroMove - [Integrations] - Outbound Source Identification Custom Setting
*/
Record_Type__c systemSettings = Record_Type__c.getOrgDefaults();
try{
    if (systemSettings !=null && String.isBlank(systemSettings.OSM_SFDC_Org_Origin__c)){
        systemSettings.OSM_SFDC_Org_Origin__c='SFDC';
        update systemSettings;
    }
}catch(Exception e){
    throw new OSM_Exception('Exception: '+e.getMessage());
}
