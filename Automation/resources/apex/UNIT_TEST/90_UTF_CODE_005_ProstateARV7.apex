GHI_UTF_ProstateARV7OrderProcessing.createOrder(); 
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId); 
GHI_UTF_ProstateARV7OrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createPackage($orderId);
List<Case> srcase = [select  CaseNumber from Case where OSM_Primary_Order__c   = :$orderId AND Type = 'Specimen Retrieval'];
List<OSM_Package__c> pkgList = [select ID from OSM_Package__c where OSM_SR_Barcode__c  =: srcase[0].CaseNumber];
System.assert(pkgList.size()>0);