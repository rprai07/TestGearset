GHI_UTF_ColonOrderProcessing.createOrder(); 
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
GHI_UTF_ColonOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ColonOrderProcessing.createPackage($orderId);
List<Case> srcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Specimen Retrieval' AND OSM_Tracking_Number_lookup__c != null];
System.assertEquals('Closed', srcase[0].Status);
