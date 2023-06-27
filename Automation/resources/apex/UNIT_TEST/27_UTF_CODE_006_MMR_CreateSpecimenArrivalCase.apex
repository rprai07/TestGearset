GHI_UTF_MMROrderProcessing.createOrder();
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId);
GHI_UTF_MMROrderProcessing.createPackage($orderId); 
List<Case> arrivalcase = [Select Id from Case where Type = 'Specimen Arrival' and OSM_Primary_Order__c= :$orderId];
System.assert(arrivalcase.size()>0);