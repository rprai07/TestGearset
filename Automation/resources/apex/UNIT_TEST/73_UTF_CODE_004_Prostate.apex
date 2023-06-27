GHI_UTF_ProstateOrderProcessing.createOrder();
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
List<Case> retrievalcase = [Select Id from Case where Type = 'Specimen Retrieval' and OSM_Primary_Order__c= :$orderId];
System.assert(retrievalcase.size()>0);


