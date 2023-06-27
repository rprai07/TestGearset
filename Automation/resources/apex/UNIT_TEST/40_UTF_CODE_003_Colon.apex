GHI_UTF_ColonOrderProcessing.createOrder();
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' and OSM_Primary_Order__c= :$orderId];
System.assert(missingcase.size()>0);


