GHI_UTF_ProstateOrderProcessing.createOrder();
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId);
GHI_UTF_ProstateOrderProcessing.createPackage($orderId); 
List<Case> arrivalcase = [Select Id from Case where Type = 'Specimen Arrival' and OSM_Primary_Order__c= :$orderId];
System.assert(arrivalcase.size()>0);