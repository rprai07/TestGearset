GHI_UTF_DCISOrderProcessing.createOrder(); 
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId); 
GHI_UTF_DCISOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_DCISOrderProcessing.createPackage($orderId);
List<Case> srcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Specimen Retrieval' AND OSM_Tracking_Number_lookup__c != null];
System.assertEquals('Closed', srcase[0].Status);
