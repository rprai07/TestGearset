GHI_UTF_ProstateARV7OrderProcessing.createOrder();
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId); 
List<Case> missingcase = [Select Id from Case where Type = 'Missing Data' and OSM_Primary_Order__c= :$orderId];
System.assert(missingcase.size()>0);


