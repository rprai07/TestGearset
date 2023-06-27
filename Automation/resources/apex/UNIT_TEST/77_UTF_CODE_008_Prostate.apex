GHI_UTF_ProstateOrderProcessing.createOrder(); 
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' AND  OSM_Primary_Order__c= :$orderId AND Status != 'Closed'];
System.assert(missingcase.size()==0);


