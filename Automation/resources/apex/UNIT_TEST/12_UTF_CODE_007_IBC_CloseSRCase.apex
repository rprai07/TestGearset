GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_IBCOrderProcessing.createPackage($orderId);
GHI_UTF_IBCOrderProcessing.processOrder($orderId); 
List<Case> srcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Specimen Retrieval' AND OSM_Tracking_Number_lookup__c != null];
System.assertEquals('Closed', srcase[0].Status);