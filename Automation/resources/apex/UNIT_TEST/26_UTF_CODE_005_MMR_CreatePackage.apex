GHI_UTF_MMROrderProcessing.createOrder(); 
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
GHI_UTF_MMROrderProcessing.closePreBillingCase($orderId);
GHI_UTF_MMROrderProcessing.createPackage($orderId);
List<Case> srcase = [select  CaseNumber from Case where OSM_Primary_Order__c   = :$orderId AND Type = 'Specimen Retrieval'];
List<OSM_Package__c> pkgList = [select ID from OSM_Package__c where OSM_SR_Barcode__c  =: srcase[0].CaseNumber];
System.assert(pkgList.size()>0);