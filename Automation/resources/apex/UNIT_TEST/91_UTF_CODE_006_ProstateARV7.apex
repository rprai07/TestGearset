GHI_UTF_ProstateARV7OrderProcessing.createOrder();
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createPackage($orderId); 
List<Case> arrivalcase = [Select Id from Case where Type = 'Specimen Arrival' and OSM_Primary_Order__c= :$orderId];
System.assert(arrivalcase.size()>0);