GHI_UTF_ProstateARV7OrderProcessing.createOrder();
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId); 
List<Case> retrievalcase = [Select Id from Case where Type = 'Specimen Retrieval' and OSM_Primary_Order__c= :$orderId];
System.assert(retrievalcase.size()>0);


