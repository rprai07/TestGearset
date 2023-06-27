System.debug('Test Case for Pre billing case is closed');
GHI_UTF_MMROrderProcessing.createOrder(); 
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
GHI_UTF_MMROrderProcessing.closePreBillingCase($orderId);
List<Case> prebill = [Select Id , Status from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assertEquals('Closed', prebill[0].Status);
