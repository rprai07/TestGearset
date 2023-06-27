GHI_UTF_ColonOrderProcessing.createOrder(); 
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
GHI_UTF_ColonOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ColonOrderProcessing.createPackage($orderId);
List<Case> srcase = [select  CaseNumber from Case where OSM_Primary_Order__c   = :$orderId AND Type = 'Specimen Retrieval'];
List<OSM_Package__c> pkgList = [select ID from OSM_Package__c where OSM_SR_Barcode__c  =: srcase[0].CaseNumber];
System.assert(pkgList.size()>0);