GHI_UTF_DCISOrderProcessing.createOrder();
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' and OSM_Primary_Order__c= :$orderId];
System.assert(missingcase.size()>0);


