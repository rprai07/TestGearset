GHI_UTF_MMROrderProcessing.createOrder(); 
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
GHI_UTF_MMROrderProcessing.closePreBillingCase($orderId);
GHI_UTF_MMROrderProcessing.createPackage($orderId);
List<Case> srcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Specimen Retrieval' AND OSM_Tracking_Number_lookup__c != null];
System.assertEquals('Closed', srcase[0].Status);
