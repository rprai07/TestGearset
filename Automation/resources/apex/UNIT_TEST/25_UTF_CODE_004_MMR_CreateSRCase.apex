GHI_UTF_MMROrderProcessing.createOrder();
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
List<Case> retrievalcase = [Select Id from Case where Type = 'Specimen Retrieval' and OSM_Primary_Order__c= :$orderId];
System.assert(retrievalcase.size()>0);


