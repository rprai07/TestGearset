GHI_UTF_ProstateARV7OrderProcessing.createOrder(); 
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId); 
GHI_UTF_ProstateARV7OrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createPackage($orderId);
List<Case> srcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Specimen Retrieval' AND OSM_Tracking_Number_lookup__c != null];
System.assertEquals('Closed', srcase[0].Status);
