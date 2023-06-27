GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' and OSM_Primary_Order__c= :$orderId];
System.assert(missingcase.size()>0);


