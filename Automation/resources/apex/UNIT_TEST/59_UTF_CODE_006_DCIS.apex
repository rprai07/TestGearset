GHI_UTF_DCISOrderProcessing.createOrder();
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId);
GHI_UTF_DCISOrderProcessing.createPackage($orderId); 
List<Case> arrivalcase = [Select Id from Case where Type = 'Specimen Arrival' and OSM_Primary_Order__c= :$orderId];
System.assert(arrivalcase.size()>0);