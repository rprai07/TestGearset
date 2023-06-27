GHI_UTF_IBCOrderProcessing.createOrderIncorrectMethod(); 
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' AND  OSM_Primary_Order__c= :$orderId AND Status != 'Closed'];
System.assert(missingcase.size()==0);


