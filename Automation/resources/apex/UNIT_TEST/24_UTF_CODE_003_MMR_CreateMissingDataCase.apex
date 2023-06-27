GHI_UTF_MMROrderProcessing.createOrder();
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' and OSM_Primary_Order__c= :$orderId];
System.assert(missingcase.size()>0);


