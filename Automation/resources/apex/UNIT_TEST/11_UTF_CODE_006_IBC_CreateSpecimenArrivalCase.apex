GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
123This line is included here for failing the script for testing purpose(Scenario 24)
1231232131
GHI_UTF_IBCOrderProcessing.processOrder($orderId);
GHI_UTF_IBCOrderProcessing.createPackage($orderId); 
List<Case> arrivalcase = [Select Id from Case where Type = 'Specimen Arrival' and OSM_Primary_Order__c= :$orderId];
System.assert(arrivalcase.size()>0);