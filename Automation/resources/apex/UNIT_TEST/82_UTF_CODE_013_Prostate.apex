System.debug('Test Case for Pre billing case is closed');
GHI_UTF_ProstateOrderProcessing.createOrder(); 
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
GHI_UTF_ProstateOrderProcessing.closePreBillingCase($orderId);
List<Case> prebill = [Select Id , Status from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assertEquals('Closed', prebill[0].Status);
